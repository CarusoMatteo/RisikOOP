package it.unibo.risikoop.model.implementations.gamephase;

import it.unibo.risikoop.model.interfaces.GamePhase;
import it.unibo.risikoop.model.interfaces.Territory;

/**
 * Implementation of the {@link GamePhase} interface representing the Combo
 * Phase in the game.
 * <p>
 * The ComboPhaseImpl class defines the behavior and logic for the Combo Phase,
 * which is a specific phase in the game where players may perform combined
 * actions.
 * This class provides methods to check if the phase is complete
 * </p>
 *
 * @see GamePhase
 * @see Territory
 */
public final class ComboPhaseImpl implements GamePhase {

    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    public void performAction() {
    }

    @Override
    public void selectTerritory(final Territory t) {
    }

    @Override
    public void setUnitsToUse(final int units) {
    }

    @Override
    public void initializationPhase() {
    }

    @Override
    public String getInnerState() {
        return "player chose combos";
    }
}
