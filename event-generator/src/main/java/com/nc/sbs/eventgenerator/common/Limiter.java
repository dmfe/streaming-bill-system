package com.nc.sbs.eventgenerator.common;

import com.google.common.util.concurrent.RateLimiter;

public class Limiter {

    private RateLimiter limiter;

    public Limiter(Double messageRate) {
        limiter = RateLimiter.create(messageRate);
    }

    public void aquire(Integer count) {
        limiter.acquire(count);
    }
}
