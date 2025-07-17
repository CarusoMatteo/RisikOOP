package it.unibo.risikoop.model.implementations.gamephase;

import java.util.Set;
import java.util.stream.Collectors;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.risikoop.controller.interfaces.GamePhaseController;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.TurnManager;
import it.unibo.risikoop.model.interfaces.gamephase.GamePhase;
import it.unibo.risikoop.model.interfaces.gamephase.InternalState;
import it.unibo.risikoop.model.interfaces.gamephase.PhaseDescribable;
import it.unibo.risikoop.model.interfaces.gamephase.PhaseWithActionToPerforme;
import it.unibo.risikoop.model.interfaces.gamephase.PhaseWithTransaction;
import it.unibo.risikoop.model.interfaces.gamephase.PhaseWithUnits;

/**
 * Represents the movement phase of the Risiko game.
 * In this phase, players can move units from one territory to another.
 * The player selects a source territory, a destination territory, and the
 * number of units to move.
 */
public final class MovementPhase
        implements GamePhase, PhaseDescribable, PhaseWithUnits, PhaseWithActionToPerforme, PhaseWithTransaction {

    private final TurnManager turnManager;
    private final GamePhaseController gpc;
    private Territory source;
    private Territory destination;
    private int unitsToMove;
    private boolean moved;
    private InternalState internalState;

    /**
     * Constructs a MovementPhase with the specified TurnManager.
     * Initializes the phase state to SELECT_SOURCE.
     *
     * @param gpc the game phase manager
     */
    public MovementPhase(final GamePhaseController gpc) {
        this.gpc = gpc;
        this.turnManager = gpc.getTurnManager();
        this.source = null;
        this.destination = null;
        this.unitsToMove = 0;
        this.moved = true;
        internalState = InternalState.SELECT_SRC;
    }

    @Override
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "We intentionally store the Territory reference; game logic needs mutable state.")
    public boolean selectTerritory(final Territory t) {
        final Player p = turnManager.getCurrentPlayer();
        final Set<Territory> owned = p.getTerritories().stream().collect(Collectors.toSet());

        if (internalState == InternalState.SELECT_SRC) {
            if (owned.contains(t) && t.getUnits() >= 2) {
                this.source = t;
                unitsToMove = 0;
                moved = false;
                return true;
            }
        } else if (internalState == InternalState.SELECT_DST) {
            if (source.getNeightbours().contains(t)
                    && !t.equals(source)
                    && t.getOwner().equals(turnManager.getCurrentPlayer())) {
                this.destination = t;
                return true;
            }
        }
        return false;
    }

    @Override
    public void performAction() {
        if (internalState == InternalState.SELECT_SRC && source != null) {
            nextState();
        } else if (internalState == InternalState.SELECT_DST && destination != null) {
            nextState();
        } else if (internalState == InternalState.SELECT_UNITS_QUANTITY && unitsToMove > 0) {
            nextState();
        } else if (internalState == InternalState.EXECUTE) {
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
        if (internalState == InternalState.SELECT_UNITS_QUANTITY
                && units <= source.getUnits() - 1
                && units > 0) {
            this.unitsToMove = units;
        }
    }

    @Override
    public String getInnerStatePhaseDescription() {
        switch (internalState) {

            case SELECT_SRC -> {
                return "Selecting the moving from territory";
            }
            case SELECT_DST -> {
                return "Selecting the moving to territory ";
            }
            case SELECT_UNITS_QUANTITY -> {
                return "Selecting units quantity";
            }
            case EXECUTE -> {
                return "Executing the movement";
            }
            default -> throw new AssertionError();
        }
    }

    @Override
    public void nextState() {
        switch (internalState) {
            case SELECT_DST -> internalState = InternalState.SELECT_UNITS_QUANTITY;
            case SELECT_SRC -> internalState = InternalState.SELECT_DST;
            case SELECT_UNITS_QUANTITY -> internalState = InternalState.EXECUTE;
            case EXECUTE -> internalState = InternalState.SELECT_SRC;
            default -> throw new IllegalArgumentException("Unexpected value: " + internalState);
        }
    }

    @Override
    public InternalState getInternalState() {
        return internalState;
    }
}
