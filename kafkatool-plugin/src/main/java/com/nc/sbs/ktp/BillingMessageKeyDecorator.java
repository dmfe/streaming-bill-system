package com.nc.sbs.ktp;

import com.google.protobuf.Parser;
import com.kafkatool.external.ICustomMessageDecorator2;
import com.nc.sbs.model.BillingMessageKey;

public class BillingMessageKeyDecorator extends AbstractProtobufMessageDecorator<BillingMessageKey>
        implements ICustomMessageDecorator2 {

    @Override
    public Parser<BillingMessageKey> getParser() {
        return BillingMessageKey.parser();
    }

    @Override
    public String getDisplayName() {
        return "BillingMessageKey";
    }
}
