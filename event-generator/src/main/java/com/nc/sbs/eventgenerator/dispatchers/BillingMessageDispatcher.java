package com.nc.sbs.eventgenerator.dispatchers;

import static com.nc.sbs.eventgenerator.utils.ExceptionHandlers.hadleExecutionException;
import static com.nc.sbs.eventgenerator.utils.ExceptionHandlers.handleInterruptedException;

import com.google.common.collect.ImmutableMap;
import com.nc.sbs.eventgenerator.configs.GeneratorConfig;
import com.nc.sbs.eventgenerator.entities.BillingMessageEntity;
import com.nc.sbs.eventgenerator.providers.KafkaConfigProvider;
import com.nc.sbs.model.BillingMessage;
import com.nc.sbs.model.BillingMessageKey;
import com.nc.sbs.model.serde.KafkaProtobufSerializer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public class BillingMessageDispatcher implements EntityDispatcher<BillingMessageEntity> {

    private static final String INPUT_EVENTS_TOPIC = "InputEvents";
    private static final String CLIENT_ID = "event-generator";

    private final BlockingQueue<BillingMessageEntity> billingMessageQueue;
    private final BlockingQueue<FuturePair> futureBillingMessageQueue;
    private final BlockingQueue<BillingMessageEntity> resendingBillingMessageQueue;

    private final KafkaProducer<BillingMessageKey, BillingMessage> producer;

    public BillingMessageDispatcher(GeneratorConfig config) {

        producer = new KafkaProducer<>(KafkaConfigProvider.getProducerConfig(ImmutableMap.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getKafkaHost(),
                ProducerConfig.CLIENT_ID_CONFIG, CLIENT_ID)),
                new KafkaProtobufSerializer<>(),
                new KafkaProtobufSerializer<>());

        billingMessageQueue = new LinkedBlockingQueue<>(config.getBufferSize());
        futureBillingMessageQueue = new LinkedBlockingQueue<>(config.getBufferSize());
        resendingBillingMessageQueue = new LinkedBlockingQueue<>(config.getBufferSize());
    }

    @Override
    public void start() {

        Thread billingMessagesSendingThread = new Thread(() -> {
            try {
                while (true) {
                    sendBillingMessage(billingMessageQueue);
                }
            } catch (InterruptedException ex) {
                handleInterruptedException(ex);
            }
        }, "billing-messages-sender");
        billingMessagesSendingThread.setDaemon(true);
        billingMessagesSendingThread.start();

        Thread billingMessagesFutureThread = new Thread(() -> {
            try {
                while (true) {
                    FuturePair pair = futureBillingMessageQueue.take();
                    if (pair.getFuture().get() == null) {
                        resendingBillingMessageQueue.put(pair.getEntity());
                    }
                }
            } catch (ExecutionException | InterruptedException ex) {
                if (ex instanceof ExecutionException) {
                    hadleExecutionException((ExecutionException) ex);
                } else {
                    handleInterruptedException((InterruptedException) ex);
                }
            }
        }, "billing-messages-send-checker");
        billingMessagesFutureThread.setDaemon(true);
        billingMessagesFutureThread.start();

        Thread billingMessagesReSendingThread = new Thread(() -> {
            try {
                while (true) {
                    sendBillingMessage(resendingBillingMessageQueue);
                }
            } catch (InterruptedException ex) {
                handleInterruptedException(ex);
            }
        }, "billing-messages-re-sender");
        billingMessagesReSendingThread.setDaemon(true);
        billingMessagesReSendingThread.start();
    }

    private void sendBillingMessage(BlockingQueue<BillingMessageEntity> queue) throws InterruptedException {
        BillingMessageEntity entity = queue.take();
        Future<RecordMetadata> future = producer.send(new ProducerRecord<>(
                INPUT_EVENTS_TOPIC,
                entity.getKey(),
                entity.getMessage()
        ));

        futureBillingMessageQueue.put(new FuturePair(future, entity));
    }

    @Override
    public void stop() {
    }

    @Override
    public void dispatch(BillingMessageEntity entity) {
        try {
            billingMessageQueue.put(entity);
        } catch (InterruptedException ex) {
            handleInterruptedException(ex);
        }
    }

    @RequiredArgsConstructor
    @Getter
    private final class FuturePair {
        private final Future<RecordMetadata> future;
        private final BillingMessageEntity entity;
    }
}
