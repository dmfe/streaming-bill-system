package com.nc.sbs.eventgenerator;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.nc.sbs.eventgenerator.configs.GeneratorConfig;
import com.nc.sbs.eventgenerator.entities.BillingMessageEntity;
import com.nc.sbs.eventgenerator.generators.AbstractGenerator;
import com.nc.sbs.eventgenerator.providers.VersionProvider;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;
import sun.misc.Signal;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@CommandLine.Command(
        name = "event-generator",
        versionProvider = VersionProvider.class,
        mixinStandardHelpOptions = true,
        description = {"@|bold Event Generator|@ - Rated Events Generator"}
)
@Slf4j
public class GeneratorWorker implements Runnable {

    @CommandLine.Option(names = {"-H", "--host"}, description = {"Kafka host."})
    private String kafkaHost = "localhost:9092";

    @CommandLine.Option(names = {"-r", "--events-rate"}, description = {"Events rate."})
    private Double eventsRate = 100.0;

    @CommandLine.Option(names = {"-s", "--message-size"}, description = {"Events in message count."})
    private Integer eventsCount = 100;

    @CommandLine.Option(names = {"-b", "--buffer"}, description = {"Buffer sizes for internal message queues."})
    private Integer bufferSize = 100;

    @CommandLine.Option(names = {"-t", "--threads"}, description = {"Threads number."})
    private Integer threadsNum = 4;

    @CommandLine.Option(names = {"-v", "--verbose"}, description = {"Be verbose."})
    private Boolean tracing = false;

    private AtomicBoolean isRunning = new AtomicBoolean(true);

    GeneratorWorker() {
        Signal.handle(new Signal("INT"), sig -> {
            System.out.println("\n Interrupt signal received. Stopping...");
            interrupt();
        });
    }

    private void interrupt() {
        isRunning.set(false);
    }

    @Override
    public void run() {
        log.info("Configuration parameters:");
        log.info("Kafka host: {}", kafkaHost);
        log.info("Event rate: {}", eventsRate);
        log.info("Events count: {}", eventsCount);
        log.info("Buffer size: {}", bufferSize);
        log.info("Tracing: {}", tracing);

        GeneratorConfig config = GeneratorConfig.builder()
                .kafkaHost(kafkaHost)
                .eventRate(eventsRate)
                .eventsCount(eventsCount)
                .bufferSize(bufferSize)
                .threadsNum(threadsNum)
                .build();


        AbstractGenerator<BillingMessageEntity> generator = AbstractGenerator.of(config);

        MetricRegistry metricRegistry = new MetricRegistry();
        Meter meter = metricRegistry.meter("Generated events");
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();

        if (tracing) {
            reporter.start(1, TimeUnit.SECONDS);
        }

        generator.run(isRunning, meter);

        reporter.stop();
    }
}
