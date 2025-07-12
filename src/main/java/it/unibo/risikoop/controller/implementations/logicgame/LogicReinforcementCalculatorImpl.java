package it.unibo.risikoop.controller.implementations.logicgame;

import java.util.Set;

import it.unibo.risikoop.controller.interfaces.logicgame.LogicReinforcementCalculator;
import it.unibo.risikoop.model.interfaces.Continent;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.TurnManager;

/**
 * Computes the number of initial reinforcement units allocated to a player
 * at the start of the game, based on the total territories and player count.
 * <p>
 * Uses a configurable average units-per-territory factor to determine
 * the initial distribution.
 * </p>
 */
public final class LogicReinforcementCalculatorImpl implements LogicReinforcementCalculator {

    private final GameManager gameMenager;
    private final TurnManager turnManager;

    /**
     * Constructs a new initial units calculator using the given game manager.
     *
     * @param gameManager the {@link GameManager} providing current game state
     */
    public LogicReinforcementCalculatorImpl(final GameManager gameMenager, final TurnManager turnManager) {
        this.gameMenager = gameMenager;
        this.turnManager = turnManager;
    }

    @Override
    public int calcPlayerUnits() {
        return calcTerritoryUnits() + calcContinetUnits();
    }

    private int calcTerritoryUnits() {
        final Player p = turnManager.getCurrentPlayer();
        final int units = (int) (p.getTerritories().size() / 3);
        return units < 0 ? 1 : units;
    }

    private int calcContinetUnits() {
        final Set<Continent> continents = gameMenager.getContinents();
        final Player p = turnManager.getCurrentPlayer();

        return continents.stream()
                .filter(c -> p.getTerritories().contains(c.getTerritories()))
                .mapToInt(Continent::getUnitReward).sum();
    }
}
