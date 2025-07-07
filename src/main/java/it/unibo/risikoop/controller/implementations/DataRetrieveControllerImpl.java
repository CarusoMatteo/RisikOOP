package it.unibo.risikoop.controller.implementations;

import java.util.List;

import org.graphstream.graph.Graph;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.risikoop.controller.interfaces.DataRetrieveController;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;

public class DataRetrieveControllerImpl implements DataRetrieveController {
    private final GameManager gameManager;

    @SuppressFBWarnings("EI_EXPOSE_REP2")

    public DataRetrieveControllerImpl(final GameManager gameManager) {
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
