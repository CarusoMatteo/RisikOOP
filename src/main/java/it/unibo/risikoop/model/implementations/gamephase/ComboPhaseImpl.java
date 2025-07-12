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
public class ComboPhaseImpl implements GamePhase {

    @Override
    public boolean isComplete() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void performAction() {
        // TODO Auto-generated method stub

    }

    @Override
    public void selectTerritory(Territory t) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setUnitsToUse(int units) {
        // TODO Auto-generated method stub

    }

    @Override
    public void initializationPhase() {
        // TODO Auto-generated method stub
        
    }
}
