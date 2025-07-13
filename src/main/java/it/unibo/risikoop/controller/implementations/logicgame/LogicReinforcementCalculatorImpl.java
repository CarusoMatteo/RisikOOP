package it.unibo.risikoop.controller.implementations.logicgame;

import java.util.Set;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
     * Constructs a new reinforcement unit calculator.
     *
     * @param gameMenager the {@link GameManager} holding game data (continents,
     *                    territories)
     * @param turnManager the {@link TurnManager} used to identify the current
     *                    player
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Intentionally storing the external GameManager for ongoing calculations")
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
        final int units = p.getTerritories().size() / 3;
        return units <= 0 ? 1 : units;
    }

    private int calcContinetUnits() {
        final Set<Continent> continents = gameMenager.getContinents();
        final Player p = turnManager.getCurrentPlayer();

        return continents.stream()
                .filter(c -> p.getTerritories().contains(c.getTerritories()))
                .mapToInt(Continent::getUnitReward).sum();
    }
}
