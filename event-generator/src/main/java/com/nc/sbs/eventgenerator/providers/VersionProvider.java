package com.nc.sbs.eventgenerator.providers;

import picocli.CommandLine;

public class VersionProvider implements CommandLine.IVersionProvider {

    @Override
    public String[] getVersion() {
        return new String[] { getPackageVersion() };
    }

    private String getPackageVersion() {
        String version = VersionProvider.class.getPackage().getImplementationVersion();
        if (version == null || version.isEmpty()) {
            version = "UNKNOWN";
        }

        return version;
    }
}
