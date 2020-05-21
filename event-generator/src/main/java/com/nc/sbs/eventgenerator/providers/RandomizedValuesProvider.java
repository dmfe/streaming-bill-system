package com.nc.sbs.eventgenerator.providers;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public final class RandomizedValuesProvider {

    private static final List<String> accountIds = ImmutableList.of("acc1", "acc2", "acc3");
    private static final List<String> sources = ImmutableList.of("11-11-11", "22-33-44", "46-44-335");
    private static final List<Integer> costs = ImmutableList.of(100000, 200000, 300000);

    private static final Supplier<String> accountGen = getAccountRandomizer();
    private static final Supplier<String> sourceGen = getSourceRandomizer();
    private static final Supplier<Integer> costGen = getCostRandomizer();

    private RandomizedValuesProvider() {}

    public static String accountId() {
        return accountGen.get();
    }

    public static String source() {
        return sourceGen.get();
    }

    public static Integer cost() {
        return costGen.get();
    }

    private static Supplier<String> getAccountRandomizer() {
        return getRandomizer(accountIds);
    }

    private static Supplier<String> getSourceRandomizer() {
        return getRandomizer(sources);
    }

    private static Supplier<Integer> getCostRandomizer() {
        return getRandomizer(costs);
    }

    private static <T> Supplier<T> getRandomizer(List<T> selection) {
        return () -> {
            int size = selection.size();
            int index = new Random().nextInt(size);

            return selection.get(index);
        };
    }
}
