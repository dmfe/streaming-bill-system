package com.nc.sbs.eventgenerator.dispatchers;

import com.nc.sbs.eventgenerator.entities.Entity;

public interface EntityDispatcher<T extends Entity> {

    void start();
    void stop();
    void dispatch(T entity);
}
