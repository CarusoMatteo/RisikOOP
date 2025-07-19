package it.unibo.risikoop.model.implementations.gamephase;

import java.util.Optional;
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
import it.unibo.risikoop.model.interfaces.gamephase.PhaseWithInitialization;
import it.unibo.risikoop.model.interfaces.gamephase.PhaseWithTransaction;
import it.unibo.risikoop.model.interfaces.gamephase.PhaseWithUnits;

/**
 * Represents the movement phase of the Risiko game.
 * In this phase, players can move units from one territory to another.
 * The player selects a source territory, a destination territory, and the
 * number of units to move.
 */
public final class MovementPhase
        implements GamePhase, PhaseDescribable, PhaseWithUnits, PhaseWithActionToPerforme, PhaseWithInitialization,
        PhaseWithTransaction {

    private final TurnManager turnManager;
    private final GamePhaseController gpc;
    private Optional<Territory> source;
    private Optional<Territory> destination;
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
        this.source = Optional.empty();
        this.destination = Optional.empty();
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
                this.source = Optional.ofNullable(t);
                return true;
            }
        } else if (internalState == InternalState.SELECT_DST) {
            if (source.map(Territory::getNeightbours).orElse(Set.of()).contains(t)
                    && !t.equals(source.orElseGet(null))
                    && t.getOwner().equals(turnManager.getCurrentPlayer())) {

                this.destination = Optional.ofNullable(t);
                return true;
            }
        }
        return false;
    }

    @Override
    public void performAction() {
        if (internalState == InternalState.SELECT_SRC && source.isPresent()) {
            nextState();
        } else if (internalState == InternalState.SELECT_DST && destination.isPresent()) {
            nextState();
        } else if (internalState == InternalState.SELECT_UNITS_QUANTITY && unitsToMove > 0) {
            nextState();
        } else if (internalState == InternalState.EXECUTE) {
            source.ifPresent(src -> src.removeUnits(unitsToMove));
            destination.ifPresent(dst -> dst.addUnits(unitsToMove));
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
                && units <= source.map(Territory::getUnits).orElse(0) - 1
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
                return "Selecting the moving to territory";
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

    @Override
    public void initializationPhase() {
        internalState = InternalState.SELECT_SRC;
        source = Optional.empty();
        destination = Optional.empty();
        unitsToMove = 0;
        moved = false;
    }

}
