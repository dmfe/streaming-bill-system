package com.nc.sbs.ktp;

import com.google.protobuf.MessageLite;
import com.google.protobuf.Parser;

import java.util.Map;

public abstract class AbstractProtobufMessageDecorator<M extends MessageLite> {

    public abstract String getDisplayName();

    public abstract Parser<M> getParser();

    public final String decorate(String zookeeperHost,
                                 String brokerHost,
                                 String topic,
                                 long partitionId,
                                 long offset,
                                 byte[] msg,
                                 Map<String, byte[]> headers,
                                 Map<String, String> reserved) {
        if (msg == null) {
            return null;
        }

        try {
            return getDisplayName() + ":\n" + getParser().parseFrom(msg).toString();
        } catch (Exception e) {
            return "Parse error";
        }
    }
}
