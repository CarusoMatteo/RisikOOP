package it.unibo.risikoop.model.implementations.gamephase;

import java.util.Set;
import java.util.stream.Collectors;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.risikoop.controller.interfaces.GamePhaseController;
import it.unibo.risikoop.model.interfaces.GamePhase;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.TurnManager;

/**
 * Represents the movement phase of the Risiko game.
 * In this phase, players can move units from one territory to another.
 * The player selects a source territory, a destination territory, and the
 * number of units to move.
 */
public final class MovementPhase implements GamePhase {
    public enum PhaseState {
        SELECT_SOURCE("Select the source territory"),
        SELECT_DESTINATION("Select the destination territory"),
        SELECT_UNITS("Choose number of units to move"),
        MOVE_UNITS("Execute the move");

        private final String description;

        PhaseState(String description) {
            this.description = description;
        }

        /** Restituisce la descrizione associata a questo stato */
        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    private final TurnManager turnManager;
    private Territory source;
    private Territory destination;
    private int unitsToMove;
    private boolean moved;
    private PhaseState state;

    /**
     * Constructs a MovementPhase with the specified TurnManager.
     * Initializes the phase state to SELECT_SOURCE.
     *
     * @param gpc the game phase manager
     */
    public MovementPhase(final GamePhaseController gpc) {
        this.turnManager = gpc.getTurnManager();
        this.source = null;
        this.destination = null;
        this.unitsToMove = 0;
        this.moved = true;
        this.state = PhaseState.SELECT_SOURCE;
    }

    @Override
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "We intentionally store the Territory reference; game logic needs mutable state.")
    public void selectTerritory(final Territory t) {
        final Player p = turnManager.getCurrentPlayer();
        final Set<Territory> owned = p.getTerritories().stream().collect(Collectors.toSet());

        if (state == PhaseState.SELECT_SOURCE) {
            if (owned.contains(t) && t.getUnits() >= 2) {
                this.source = t;
                unitsToMove = 0;
                moved = false;
            }
        } else if (state == PhaseState.SELECT_DESTINATION) {
            if (source.getNeightbours().contains(t)
                    && !t.equals(source)
                    && t.getOwner().equals(turnManager.getCurrentPlayer())) {
                this.destination = t;
            }
        }
    }

    @Override
    public void performAction() {
        if (state == PhaseState.SELECT_SOURCE && source != null) {
            state = PhaseState.SELECT_DESTINATION;
        } else if (state == PhaseState.SELECT_DESTINATION && destination != null) {
            state = PhaseState.SELECT_UNITS;
        } else if (state == PhaseState.SELECT_UNITS && unitsToMove > 0) {
            state = PhaseState.MOVE_UNITS;
        } else if (state == PhaseState.MOVE_UNITS) {
            source.removeUnits(unitsToMove);
            destination.addUnits(unitsToMove);
            moved = true;
        }
    }

    @Override
    public boolean isComplete() {
        // questa fase termina non appena l'utente esegue lo spostamento (moved=true)
        return moved;
    }

    @Override
    public void setUnitsToUse(final int units) {
        if (state == PhaseState.SELECT_UNITS
                && units <= source.getUnits() - 1
                && units > 0) {
            this.unitsToMove = units;
        }
    }

    @Override
    public void initializationPhase() {
        // TODO Auto-generated method stub
    }

    @Override
    public String getInnerState() {
        return state.getDescription();
    }
}
