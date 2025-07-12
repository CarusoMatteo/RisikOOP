package it.unibo.risikoop.model.implementations.gamephase;

import java.util.Set;
import java.util.stream.Collectors;

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
    enum PhaseState {
        SELECT_SOURCE,
        SELECT_DESTINATION,
        SELECT_UNITS,
        MOVE_UNITS
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
     * @param turnManager the TurnManager that manages the turns in the game
     */
    public MovementPhase(final TurnManager turnManager) {
        this.turnManager = turnManager;
        this.source = null;
        this.destination = null;
        this.unitsToMove = 0;
        this.moved = false;
        this.state = PhaseState.SELECT_SOURCE;
    }

    @Override
    public void selectTerritory(final Territory t) {
        final Player p = turnManager.getCurrentPlayer();
        final Set<Territory> owned = p.getTerritories().stream().collect(Collectors.toSet());

        if (state == PhaseState.SELECT_SOURCE) {
            if (owned.contains(t) && t.getUnits() >= 2) {
                this.source = t;
                unitsToMove = 0;
            }
        } else if (state == PhaseState.SELECT_DESTINATION) {
            if (source.getNeightbours().contains(t)) {
                this.destination = t;
            }
        }
    }

    @Override
    public void performAction() {
        if (state == PhaseState.SELECT_SOURCE && source != null) {
            state = PhaseState.SELECT_DESTINATION;
        } else if (state == PhaseState.SELECT_DESTINATION && destination != null) {
            state = PhaseState.MOVE_UNITS;
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
        if (source != null && units <= source.getUnits() - 1) {
            this.unitsToMove = units;
        }
    }

    @Override
    public void initializationPhase() {
        // TODO Auto-generated method stub
    }
}
