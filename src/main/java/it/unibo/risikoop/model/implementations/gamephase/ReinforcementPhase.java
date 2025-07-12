package it.unibo.risikoop.model.implementations.gamephase;

import it.unibo.risikoop.controller.interfaces.logicgame.LogicReinforcementCalculator;
import it.unibo.risikoop.model.interfaces.GamePhase;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.TurnManager;

/**
 * ReinforcementPhase allows the current player to place all their
 * available reinforcement units onto their territories.
 * <p>
 * - selectTerritory(...) places one unit on the chosen territory (if valid)<br>
 * - performAction() is a no‐op<br>
 * - isComplete() returns true only after all units have been placed<br>
 * - setUnitsToUse(...) is ignored
 * </p>
 */
public final class ReinforcementPhase implements GamePhase {

    private final TurnManager turnManager;
    private LogicReinforcementCalculator logic;
    private boolean isFirtsReq = true;

    /**
     * Constructs a new ReinforcementPhase for the given turn manager.
     *
     * @param turnManager the TurnManager tracking current player and turns
     */
    public ReinforcementPhase(final TurnManager turnManager) {
        this.turnManager = turnManager;
    }

    @Override
    public void selectTerritory(final Territory t) {
        final Player current = turnManager.getCurrentPlayer();
        if (current.getUnitsToPlace() > 0 && current.getTerritories().contains(t)) {
            t.addUnits(1);
            current.removeUnitsToPlace(1);
        }
    }

    @Override
    public void performAction() {
        if (isFirtsReq) {
            isFirtsReq = false;
            final Player current = turnManager.getCurrentPlayer();
            current.addUnitsToPlace(logic.calcPlayerUnits());
        }
    }

    @Override
    public boolean isComplete() {
        // Phase complete only when all reinforcement units are placed
        final Player current = turnManager.getCurrentPlayer();
        return current.getUnitsToPlace() == 0;
    }

    @Override
    public void setUnitsToUse(final int units) {
        // Not used in this phase
    }

    @Override
    public void initializationPhase() {
        // TODO Auto-generated method stub
    }
}
