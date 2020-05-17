package com.nc.sbs.ktp;

import com.google.protobuf.Parser;
import com.kafkatool.external.ICustomMessageDecorator2;
import com.nc.sbs.model.BillingMessage;

public class BillingMessageDecorator extends AbstractProtobufMessageDecorator<BillingMessage>
        implements ICustomMessageDecorator2 {

    @Override
    public Parser<BillingMessage> getParser() {
        return BillingMessage.parser();
    }

    @Override
    public String getDisplayName() {
        return "BillingMessage";
    }
}
