package com.nc.sbs.eventgenerator.builders;

import com.nc.sbs.eventgenerator.entities.BillingMessageEntity;
import com.nc.sbs.eventgenerator.providers.RandomizedValuesProvider;
import com.nc.sbs.eventgenerator.providers.SystemSettingsProvider;
import com.nc.sbs.model.BillingMessage;
import com.nc.sbs.model.BillingMessageHeader;
import com.nc.sbs.model.BillingMessageKey;
import com.nc.sbs.model.BillingPayload;
import com.nc.sbs.model.MessageType;
import com.nc.sbs.model.RatedEvent;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class BillingMessageBuilder implements EntityBuilder<BillingMessageEntity> {

    private static final Integer VERSION = 1;
    private static final Integer PROCESS_ID = 0;
    private static final BillingMessageHeader MSG_HEADER = BillingMessageHeader.newBuilder()
            .setType(MessageType.RATED_EVENTS)
            .setVersion(VERSION)
            .setHost(SystemSettingsProvider.getSystemSettings().getHost())
            .setProcessId(Integer.valueOf(SystemSettingsProvider.getSystemSettings().getPid()))
            .setProcessId(PROCESS_ID)
            .build();

    private final Integer eventsNum;

    @Override
    public BillingMessageEntity build() {

        BillingMessageKey key = BillingMessageKey.newBuilder()
                .setId(UUID.randomUUID().toString())
                .build();

        BillingMessage message = BillingMessage.newBuilder()
                .setHeader(MSG_HEADER)
                .setPayload(BillingPayload.newBuilder()
                        .addAllEvents(ratedEvents()).build())
                .build();

        return new BillingMessageEntity(key, message, eventsNum);
    }

    @Override
    public EntityBuilder<BillingMessageEntity> copy() {
        return new BillingMessageBuilder(eventsNum);
    }

    private List<RatedEvent> ratedEvents() {

        return IntStream.range(0, eventsNum)
                .mapToObj(num -> RatedEvent.newBuilder()
                        .setAccountId(RandomizedValuesProvider.accountId())
                        .setSource(RandomizedValuesProvider.source())
                        .setEventDT(Instant.now().toEpochMilli())
                        .setEventCost(RandomizedValuesProvider.cost())
                        .setEventTypeId(1)
                        .setPlanId(1)
                        .build())
                .collect(Collectors.toList());
    }
}
