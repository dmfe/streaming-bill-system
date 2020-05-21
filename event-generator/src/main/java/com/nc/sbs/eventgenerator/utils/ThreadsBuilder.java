package com.nc.sbs.eventgenerator.utils;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class ThreadsBuilder {

    private ThreadsBuilder() {}

    public static List<Thread> threads(Boolean isDaemon, Integer count, Consumer<Integer> job) {
        return IntStream.range(0, count)
                .mapToObj(num -> {
                    Thread thread = new Thread(() -> job.accept(num), "entity-generator-thread-" + num);
                    thread.setDaemon(isDaemon);

                    return thread;
                })
                .collect(Collectors.toList());
    }
}
