package de.Andre.FluidSimulation;

import de.Andre.FluidSimulation.Controller.ConfigController;

import java.util.logging.Logger;

public class Main {
    private final ConfigController controller;
    private final Logger logger;

    public Main() {
        this.logger = Logger.getLogger(this.getClass().getName());
        this.controller = new ConfigController(this);

    }

    public static void main(String[] arg){
        new Main();
    }

    public Logger getLogger() {
        return logger;
    }
}
