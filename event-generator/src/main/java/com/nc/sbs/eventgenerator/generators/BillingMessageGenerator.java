package com.nc.sbs.eventgenerator.generators;

import com.nc.sbs.eventgenerator.builders.EntityBuilder;
import com.nc.sbs.eventgenerator.common.Limiter;
import com.nc.sbs.eventgenerator.dispatchers.EntityDispatcher;
import com.nc.sbs.eventgenerator.entities.BillingMessageEntity;

class BillingMessageGenerator extends AbstractGenerator<BillingMessageEntity> {

    BillingMessageGenerator(Limiter limiter,
            EntityBuilder<BillingMessageEntity> builder,
            EntityDispatcher<BillingMessageEntity> dispatcher) {

        super(limiter, builder, dispatcher);
    }
}
