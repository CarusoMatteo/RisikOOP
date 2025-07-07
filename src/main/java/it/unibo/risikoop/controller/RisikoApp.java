package it.unibo.risikoop.controller;

import it.unibo.risikoop.controller.implementations.ControllerImpl;
import it.unibo.risikoop.controller.interfaces.Controller;

public class RisikoApp {
    Controller controller = new ControllerImpl();

    /**
     * Starts the model and attaches the views.
     * 
     * @param views the views to attach
     */
    public RisikoApp() {
        controller.start();
    }

    /**
     * @param args ignored
     */
    public static void main(final String... args) {
        new RisikoApp();
    }
}
