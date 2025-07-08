package it.unibo.risikoop.controller;

import it.unibo.risikoop.controller.implementations.ControllerImpl;
import it.unibo.risikoop.controller.interfaces.Controller;

/**
 * entry point class.
 */
public final class RisikoApp {
    private static final Controller CONTROLLER = new ControllerImpl();

    private RisikoApp() {
    }

    /**
     * @param args ignored
     */
    public static void main(final String... args) {
        CONTROLLER.start();
    }
}
