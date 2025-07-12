package it.unibo.risikoop.controller.implementations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.graphstream.graph.Graph;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.risikoop.controller.interfaces.DataRetrieveController;
import it.unibo.risikoop.model.implementations.Color;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;

/**
 * controller for retrieveing for the vbiew all kind of necessary data.
 */
public final class DataRetrieveControllerImpl implements DataRetrieveController {
    private final GameManager gameManager;

    /**
     * constructor .
     * 
     * @param gameManager
     */
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

    @Override
    public Set<Territory> getTerritories() {
        return Collections.unmodifiableSet(gameManager.getTerritories());
    }

    @Override
    public Optional<Integer> getTerritoryUnitsFromName(final String name) {
        return gameManager.getTerritory(name).map(Territory::getUnits);
    }

    @Override
    public String getCurrentPlayerName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCurrentPlayerName'");
    }

    @Override
    public Color getCurrentPlayerColor() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCurrentPlayerColor'");
    }
}
