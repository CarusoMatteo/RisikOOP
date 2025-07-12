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
import it.unibo.risikoop.model.interfaces.TurnManager;

/**
 * controller for retrieveing for the vbiew all kind of necessary data.
 */
public final class DataRetrieveControllerImpl implements DataRetrieveController {
    private final GameManager gm;
    private final TurnManager tm;

    /**
     * constructor .
     * 
     * @param tm the turn manager
     * @param gm the game manager
     */
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public DataRetrieveControllerImpl(final TurnManager tm, final GameManager gm) {
        this.gm = gm;
        this.tm = tm;
    }

    @Override
    public List<Player> getPlayerList() {
        return gm.getPlayers();
    }

    @Override
    public Graph getActualMap() {
        return gm.getActualWorldMap();
    }

    @Override
    public Set<Territory> getTerritories() {
        return Collections.unmodifiableSet(gm.getTerritories());
    }

    @Override
    public Optional<Integer> getTerritoryUnitsFromName(final String name) {
        return gm.getTerritory(name).map(Territory::getUnits);
    }

    @Override
    public String getCurrentPlayerName() {
        return tm.getCurrentPlayer().getName();
    }

    @Override
    public Color getCurrentPlayerColor() {
        return tm.getCurrentPlayer().getColor();
    }
}
