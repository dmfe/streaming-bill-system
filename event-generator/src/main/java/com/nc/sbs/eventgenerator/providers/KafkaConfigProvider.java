package com.nc.sbs.eventgenerator.providers;

import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Map;
import java.util.Properties;

public final class KafkaConfigProvider {

    private KafkaConfigProvider() {}

    public static Properties getProducerConfig(Map<String, String> properties) {

        Properties props = new Properties();

        props.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, "1000000");
        props.setProperty(ProducerConfig.BUFFER_MEMORY_CONFIG, "1000000");
        props.setProperty(ProducerConfig.LINGER_MS_CONFIG, "15");
        props.setProperty(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, "2000000");
        props.setProperty(ProducerConfig.ACKS_CONFIG, "all");

        properties.forEach(props::setProperty);

        return props;
    }
}
