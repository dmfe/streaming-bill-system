package com.nc.sbs.eventgenerator.entities;

import com.nc.sbs.model.BillingMessage;
import com.nc.sbs.model.BillingMessageKey;
import lombok.Data;

@Data
public class BillingMessageEntity implements Entity {

    private final BillingMessageKey key;
    private final BillingMessage message;
    private final Integer permits;

    @Override
    public Integer getDispatchPermits() {
        return permits;
    }
}
