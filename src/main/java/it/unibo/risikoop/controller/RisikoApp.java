package it.unibo.risikoop.controller;

import it.unibo.risikoop.controller.implementations.ControllerImpl;
import it.unibo.risikoop.controller.interfaces.Controller;

/**
 * entry point class.
 */
public class RisikoApp {
    private final Controller controller = new ControllerImpl();

    /**
     * Starts the model and attaches the views.
     * 
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
