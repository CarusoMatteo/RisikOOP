package it.unibo.risikoop.controller.Implementations;

import java.util.Optional;

import it.unibo.risikoop.controller.Interfaces.Controller;
import it.unibo.risikoop.controller.utilities.EventType;

public class ControllerImpl implements Controller {
    @Override
    public void eventHandle(EventType TYPE, Optional<?> data) {
        switch (TYPE) {
            case START_GAME -> {
            }
            default -> {
            }

        }
    }

}
