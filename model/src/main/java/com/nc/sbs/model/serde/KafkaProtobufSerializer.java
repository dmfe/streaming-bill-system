package com.nc.sbs.model.serde;

import com.google.protobuf.MessageLite;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class KafkaProtobufSerializer<T extends MessageLite> implements Serializer<T> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public byte[] serialize(String topic, T data) {
        return data.toByteArray();
    }

    @Override
    public void close() {}
}
