package it.unibo.risikoop.controller.implementations;

import java.util.Set;

import it.unibo.risikoop.controller.interfaces.LogicReinforcementCalculator;
import it.unibo.risikoop.model.interfaces.Continent;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.TurnManager;

public class LogicReinforcementCalculatorImpl implements LogicReinforcementCalculator {

    private final GameManager gameMenager;
    private final TurnManager turnManager;

    public LogicReinforcementCalculatorImpl(GameManager gameMenager, TurnManager turnManager) {
        this.gameMenager = gameMenager;
        this.turnManager = turnManager;
    }

    @Override
    public int calcPlayerUnits() {
        return calcTerritoryUnits() + calcContinetUnits();
    }

    private int calcTerritoryUnits() {
        Player p = turnManager.getCurrentPlayer();
        int units = (int) (p.getTerritories().size() / 3);
        return units < 0 ? 1 : units;
    }

    private int calcContinetUnits() {
        Set<Continent> continents = gameMenager.getContinents();
        Player p = turnManager.getCurrentPlayer();

        return continents.stream()
                .filter(c -> p.getTerritories().contains(c.getTerritories()))
                .mapToInt(Continent::getUnitReward).sum();
    }
}
