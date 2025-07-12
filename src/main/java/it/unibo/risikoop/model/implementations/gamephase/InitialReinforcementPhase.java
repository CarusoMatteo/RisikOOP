package it.unibo.risikoop.model.implementations.gamephase;

import it.unibo.risikoop.controller.implementations.logicgame.LogicCalcInitialUnitsImpl;
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
public class InitialReinforcementPhase implements GamePhase {

    private final TurnManager turnManager;
    private final int initialUnits;
    private final GameManager gameManager;
    private LogicReinforcementCalculator logic;

    /**
     * Constructs an InitialReinforcementPhase with the specified TurnManager and
     * GameManager.
     * The initial number of units is calculated based on the number of territories
     * and players.
     *
     * @param turnManager the TurnManager that manages the turns in the game
     * @param gameManager the GameManager that manages the game state
     */
    public InitialReinforcementPhase(TurnManager turnManager, GameManager gameManager) {
        this.turnManager = turnManager;
        this.gameManager = gameManager;
        this.logic = new LogicCalcInitialUnitsImpl(gameManager);
        initialUnits = logic.calcPlayerUnits();
    }

    @Override
    public boolean isComplete() {
        return turnManager.isNewRound();
    }

    @Override
    public void performAction() {
        Player p = turnManager.getCurrentPlayer();

        if (p.getUnitsToPlace() <= 0 && !turnManager.isNewRound()) {
            turnManager.nextPlayer();
            turnManager.getCurrentPlayer().addUnitsToPlace(initialUnits);
        }
    }

    @Override
    public void selectTerritory(Territory t) {
        Player p = turnManager.getCurrentPlayer();

        if (!p.getTerritories().contains(t)) {
            throw new IllegalArgumentException("Player does not own the selected territory.");
        }

        if (p.getUnitsToPlace() > 0) {
            t.addUnits(1);
            p.removeUnitsToPlace(1);
        }
    }

    @Override
    public void setUnitsToUse(int units) {
        // TODO Auto-generated method stub
    }

    @Override
    public void initializationPhase() {
        turnManager.getCurrentPlayer().addUnitsToPlace(initialUnits);
    }

    @Override
    public void setUnitsToUse(int units) {
        // TODO Auto-generated method stub
    }
}
