package it.unibo.risikoop.controller.Implementations;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import it.unibo.risikoop.controller.Interfaces.Controller;
import it.unibo.risikoop.controller.utilities.EventType;
import it.unibo.risikoop.model.Implementations.Color;
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
            case EventType.START_GAME_EVENT:
                viewList.forEach(RisikoView::start);
                break;
            case EventType.ADD_PLAYER_EVENT:
                data.ifPresent(obj -> {
                    if (obj instanceof List l &&
                            l.get(0) instanceof String s &&
                            l.get(1) instanceof Integer r &&
                            l.get(2) instanceof Integer g &&
                            l.get(3) instanceof Integer b) {
                        if (!gameManager.addPlayer(s, new Color(r, g, b))) {
                            viewList.forEach(RisikoView::show_player_add_failed);
                        }
                    }
                });
                break;
            default:
                throw new AssertionError();
        }
    }

}
