package com.nc.sbs.eventgenerator.builders;

import static com.nc.sbs.eventgenerator.utils.ExceptionHandlers.handleInterruptedException;

import com.nc.sbs.eventgenerator.entities.Entity;
import com.nc.sbs.eventgenerator.exceptions.InterruptedRuntimeException;
import com.nc.sbs.eventgenerator.utils.ThreadsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EntityParallelBufferedBuilder<T extends Entity> implements EntityBuilder<T> {

    private final Integer threadsNum;

    private List<Thread> threadPool;
    private List<EntityBuilder<T>> builderPool;
    private BlockingQueue<T> entityQueue;

    private Boolean isRunning = false;

    public EntityParallelBufferedBuilder(EntityBuilder<T> entityBuilder, Integer bufferSize, Integer threadsNum) {
        this.threadsNum = threadsNum;

        this.threadPool = new ArrayList<>();
        this.builderPool = IntStream.range(0, threadsNum)
                .mapToObj(num -> entityBuilder.copy())
                .collect(Collectors.toList());
        this.entityQueue = new LinkedBlockingQueue<>(bufferSize);
    }

    @Override
    public T build() {
        try {
            if (!isRunning) {
                start();
            }

            return entityQueue.take();
        } catch (InterruptedException ex) {
            handleInterruptedException(ex);
        }

        throw new InterruptedRuntimeException("Thread " + Thread.currentThread().getName() + " was interrupted.");
    }

    @Override
    public EntityBuilder<T> copy() {
        throw new UnsupportedOperationException("Copying logic is not supported for parallel buffered builder.");
    }

    private void start() {
        if (!isRunning) {

            threadPool.addAll(ThreadsBuilder.threads(true, threadsNum, num -> {
                try {
                    while (true) {
                        entityQueue.put(builderPool.get(num).build());
                    }
                } catch (InterruptedException ex) {
                    handleInterruptedException(ex);
                }
            }));
            threadPool.forEach(Thread::start);

            while (entityQueue.remainingCapacity() != 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    handleInterruptedException(ex);
                }
            }

            isRunning = true;
        }
    }

    private void stop() {
        if (isRunning) {
            threadPool.forEach(Thread::interrupt);
        }
    }
}
