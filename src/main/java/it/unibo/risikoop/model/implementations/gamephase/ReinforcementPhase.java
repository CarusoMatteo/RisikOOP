package it.unibo.risikoop.model.implementations.gamephase;

import it.unibo.risikoop.controller.implementations.logicgame.LogicReinforcementCalculatorImpl;
import it.unibo.risikoop.controller.interfaces.GamePhaseController;
import it.unibo.risikoop.controller.interfaces.logicgame.LogicReinforcementCalculator;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.TurnManager;
import it.unibo.risikoop.model.interfaces.gamephase.GamePhase;
import it.unibo.risikoop.model.interfaces.gamephase.PhaseDescribable;
import it.unibo.risikoop.model.interfaces.gamephase.PhaseWithInitialization;

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
public final class ReinforcementPhase implements GamePhase, PhaseDescribable, PhaseWithInitialization {

    private final TurnManager turnManager;
    private final LogicReinforcementCalculator logic;
    private final GamePhaseController gpc;

    /**
     * Constructs a new ReinforcementPhase for the given turn manager.
     * 
     * @param gm  the game manager
     * @param gpc the game phase manager
     */
    public ReinforcementPhase(final GameManager gm, final GamePhaseController gpc) {
        this.gpc = gpc;
        this.turnManager = gpc.getTurnManager();
        logic = new LogicReinforcementCalculatorImpl(gm, turnManager);
    }

    @Override
    public boolean selectTerritory(final Territory t) {
        final Player current = turnManager.getCurrentPlayer();
        if (current.getUnitsToPlace() > 0 && current.getTerritories().contains(t)) {
            t.addUnits(1);
            current.removeUnitsToPlace(1);
            return true;
        }
        return false;
    }

    @Override
    public boolean isComplete() {
        // Phase complete only when all reinforcement units are placed
        final Player current = turnManager.getCurrentPlayer();
        return current.getUnitsToPlace() == 0;
    }

    @Override
    public void initializationPhase() {
        turnManager.getCurrentPlayer()
                .addUnitsToPlace(logic.calcPlayerUnits());
    }

    @Override
    public String getInnerStatePhaseDescription() {
        return "Player " + gpc.getTurnManager().getCurrentPlayer().getName() + " place units: "
                + gpc.getTurnManager().getCurrentPlayer().getUnitsToPlace();
    }
}
