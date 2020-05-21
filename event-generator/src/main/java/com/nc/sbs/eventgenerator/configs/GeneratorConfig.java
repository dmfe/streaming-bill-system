package com.nc.sbs.eventgenerator.configs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeneratorConfig {
    private String kafkaHost;
    private Double eventRate;
    private Integer eventsCount;
    private Integer bufferSize;
    private Integer threadsNum;
}
