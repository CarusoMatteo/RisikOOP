package it.unibo.risikoop.controller;

import it.unibo.risikoop.controller.Implementations.ControllerImpl;
import it.unibo.risikoop.controller.Interfaces.Controller;
import it.unibo.risikoop.controller.utilities.EventType;

public class RisikoApp {
    Controller controller = new ControllerImpl();

    /**
     * Starts the model and attaches the views.
     * 
     * @param views the views to attach
     */
    public RisikoApp() {
        controller.eventHandle(EventType.START_GAME_EVENT, null);
    }

    /**
     * @param args ignored
     */
    public static void main(final String... args) {
        var instance = new RisikoApp();
    }
}
