package it.unibo.risikoop.controller.Interfaces;

import java.util.Optional;

import it.unibo.risikoop.controller.utilities.EventType;
import it.unibo.risikoop.controller.utilities.RetrieveType;

public interface Controller {
    /**
     * 
     */
    public void eventHandle(EventType TYPE, Optional<?> data);

    public Optional<?> retrieveFromModel(RetrieveType Type);
}
