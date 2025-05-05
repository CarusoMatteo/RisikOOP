package it.unibo.risikoop.controller.Implementations;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import it.unibo.risikoop.controller.Interfaces.Controller;
import it.unibo.risikoop.controller.utilities.EventType;
import it.unibo.risikoop.model.Implementations.GameManagerImpl;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.view.Implementations.SwingView;
import it.unibo.risikoop.view.Interfaces.RisikoView;

public class ControllerImpl implements Controller {
    private final GameManager gameManager = new GameManagerImpl();
    private final List<RisikoView> viewList = new LinkedList<>();

    public ControllerImpl() {
        viewList.add(new SwingView(this));
    }

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
