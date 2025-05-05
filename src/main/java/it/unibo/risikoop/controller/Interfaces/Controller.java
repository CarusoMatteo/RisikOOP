package it.unibo.risikoop.controller.Interfaces;

import java.util.Optional;

import it.unibo.risikoop.controller.utilities.EventType;

public interface Controller {
    /**
     * 
     */
    public void eventHandle(EventType TYPE, Optional<?> data);
}
