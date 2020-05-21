package com.nc.sbs.eventgenerator.generators;

import com.codahale.metrics.Meter;
import com.nc.sbs.eventgenerator.builders.BillingMessageBuilder;
import com.nc.sbs.eventgenerator.builders.EntityBuilder;
import com.nc.sbs.eventgenerator.builders.EntityParallelBufferedBuilder;
import com.nc.sbs.eventgenerator.common.Limiter;
import com.nc.sbs.eventgenerator.configs.GeneratorConfig;
import com.nc.sbs.eventgenerator.dispatchers.BillingMessageDispatcher;
import com.nc.sbs.eventgenerator.dispatchers.EntityDispatcher;
import com.nc.sbs.eventgenerator.entities.BillingMessageEntity;
import com.nc.sbs.eventgenerator.entities.Entity;
import lombok.AllArgsConstructor;

import java.util.concurrent.atomic.AtomicBoolean;

@AllArgsConstructor
public abstract class AbstractGenerator<T extends Entity> {

    private Limiter limiter;
    private EntityBuilder<T> builder;
    private EntityDispatcher<T> dispatcher;

    public static <T extends Entity> AbstractGenerator<T> of(GeneratorConfig config) {
        Limiter limiter = new Limiter(config.getEventRate());
        EntityBuilder<BillingMessageEntity> builder = new EntityParallelBufferedBuilder<>(
                new BillingMessageBuilder(config.getEventsCount()),
                config.getBufferSize(),
                config.getThreadsNum()
        );
        EntityDispatcher<BillingMessageEntity> dispatcher = new BillingMessageDispatcher(config);

        return (AbstractGenerator<T>) new BillingMessageGenerator(limiter, builder, dispatcher);
    }

    public void run(AtomicBoolean isRunning, Meter meter) {
        dispatcher.start();

        while(isRunning.get()) {
            T entity = builder.build();
            Integer permits = entity.getDispatchPermits();

            limiter.aquire(permits);
            dispatcher.dispatch(entity);
            meter.mark(Long.valueOf(permits));
        }

        dispatcher.stop();
    }
}
