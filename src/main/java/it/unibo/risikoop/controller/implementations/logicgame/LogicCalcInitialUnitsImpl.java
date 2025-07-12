package it.unibo.risikoop.controller.implementations.logicgame;

import it.unibo.risikoop.controller.interfaces.logicgame.LogicReinforcementCalculator;
import it.unibo.risikoop.model.interfaces.GameManager;

public class LogicCalcInitialUnitsImpl implements LogicReinforcementCalculator{

    private static final double AVERAGE_UNITS_PER_TERRITORY = 2.5;
    private GameManager gameManager;

    public LogicCalcInitialUnitsImpl(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public int calcPlayerUnits() {
        int territories = gameManager.getTerritories().size();
        int players = gameManager.getPlayers().size();
        double avg = (double) territories / players;
        return (int) Math.floor(AVERAGE_UNITS_PER_TERRITORY * avg);
    }
    
}
