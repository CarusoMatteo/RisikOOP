package it.unibo.risikoop.controller.implementations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.graphstream.graph.Graph;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.risikoop.controller.interfaces.DataRetrieveController;
import it.unibo.risikoop.model.implementations.Color;
import it.unibo.risikoop.model.interfaces.Continent;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.ObjectiveCard;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.TurnManager;
import it.unibo.risikoop.model.interfaces.cards.GameCard;

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

    @Override
    public ObjectiveCard getCurrentPlayerObjectiveCard() {
        return tm.getCurrentPlayer().getObjectiveCard();
    }

    @Override
    public List<GameCard> getCurrentPlayerGameCards() {
        return tm.getCurrentPlayer().getGameCards();
    }

    @Override
    public Player getCurrentPlayer() {
        return tm.getCurrentPlayer();
    }

    @Override
    public Optional<Territory> getTerritoryFromName(final String name) {
        return gm.getTerritory(name);
    }

    @Override
    public Set<Continent> getContinents() {
        return gm.getContinents();
    }

    @Override
    public boolean isOwned(final String territoryName, final String playerName) {
        final var territoryOptional = gm.getTerritory(territoryName);
        final var playerOptional = gm.getPlayers()
                .stream()
                .filter(p -> p.getName().equals(playerName))
                .findFirst();
        return playerOptional.isPresent()
                && territoryOptional.isPresent()
                && playerOptional.get().getTerritories().contains(territoryOptional.get());
    }
}
