package it.unibo.risikoop.model.implementations.gamephase;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.risikoop.controller.implementations.logicgame.LogicCalcInitialUnitsImpl;
import it.unibo.risikoop.controller.interfaces.GamePhaseController;
import it.unibo.risikoop.controller.interfaces.logicgame.LogicReinforcementCalculator;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.GamePhase;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.TurnManager;

/**
 * Represents the initial reinforcement phase of the Risiko game.
 * In this phase, players receive a calculated number of units to place
 * on their owned territories at the start of the game.
 */
public final class InitialReinforcementPhase implements GamePhase {

    private final TurnManager turnManager;
    private final int initialUnits;
    private final LogicReinforcementCalculator logic;
    private GamePhaseController gpc;

    /**
     * Constructs an InitialReinforcementPhase with the specified TurnManager and
     * GameManager.
     * The initial number of units is calculated based on the number of territories
     * and players.
     * 
     * @param gamePhaseControllerImpl
     *
     * @param turnManager             the TurnManager that manages the turns in the
     *                                game
     * @param gameManager             the GameManager that manages the game state
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "We intentionally store the Territory reference; game logic needs mutable state.")
    public InitialReinforcementPhase(GamePhaseController gamePhaseController, final TurnManager turnManager,
            final GameManager gameManager) {
        this.gpc = gamePhaseController;
        this.turnManager = turnManager;
        this.logic = new LogicCalcInitialUnitsImpl(gameManager);
        initialUnits = logic.calcPlayerUnits();
    }

    @Override
    public boolean isComplete() {
        return turnManager.isNewRound();
    }

    @Override
    public void performAction() {
        final Player p = turnManager.getCurrentPlayer();

        if (p.getUnitsToPlace() <= 0 && !turnManager.isNewRound()) {
            turnManager.nextPlayer();
            turnManager.getCurrentPlayer().addUnitsToPlace(initialUnits);
        }
    }

    @Override
    public void selectTerritory(final Territory t) {
        final Player p = turnManager.getCurrentPlayer();

        if (!p.getTerritories().contains(t)) {
            throw new IllegalArgumentException("Player does not own the selected territory.");
        }

        if (p.getUnitsToPlace() > 0) {
            t.addUnits(1);
            p.removeUnitsToPlace(1);
        }
        
    }

    @Override
    public void setUnitsToUse(final int units) {
        // TODO Auto-generated method stub
    }

    @Override
    public void initializationPhase() {
        turnManager.getCurrentPlayer().addUnitsToPlace(initialUnits);
    }
}
