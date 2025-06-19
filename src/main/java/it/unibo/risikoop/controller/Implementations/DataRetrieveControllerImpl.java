package it.unibo.risikoop.controller.Implementations;

import java.util.List;

import org.graphstream.graph.Graph;

import it.unibo.risikoop.controller.Interfaces.DataRetrieveController;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;

public class DataRetrieveControllerImpl implements DataRetrieveController {
    private final GameManager gameManager;

    public DataRetrieveControllerImpl(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public List<Player> getPlayerList() {
        return gameManager.getPlayers();
    }

    @Override
    public Graph getActualMap() {
        return gameManager.getActualWorldMap();
    }

}
