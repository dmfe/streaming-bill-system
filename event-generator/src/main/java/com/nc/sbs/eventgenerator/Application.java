package com.nc.sbs.eventgenerator;

import picocli.CommandLine;

public class Application {

    public static void main(String[] args) {

        GeneratorWorker generatorWorker = new GeneratorWorker();

        try {
            System.exit(new CommandLine(generatorWorker).execute(args));
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }
}
