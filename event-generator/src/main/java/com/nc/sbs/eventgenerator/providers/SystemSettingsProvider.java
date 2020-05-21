package com.nc.sbs.eventgenerator.providers;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public final class SystemSettingsProvider {

    @Getter
    private static SystemSettings systemSettings = obtainSystemSettings();

    private SystemSettingsProvider() {}

    private static SystemSettings obtainSystemSettings() {
        RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
        String[] pidAndHost = mxBean.getName().split("@");

        return new SystemSettings(pidAndHost[0], pidAndHost[1]);
    }

    @AllArgsConstructor
    @Getter
    public static class SystemSettings {
        private String pid;
        private String host;
    }
}
