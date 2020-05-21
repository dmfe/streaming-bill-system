package com.nc.sbs.eventgenerator.builders;

import com.nc.sbs.eventgenerator.entities.Entity;

public interface EntityBuilder<T extends Entity> {

    T build();
    EntityBuilder<T> copy();
}
