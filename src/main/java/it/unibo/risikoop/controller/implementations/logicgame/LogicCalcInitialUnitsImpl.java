package it.unibo.risikoop.controller.implementations.logicgame;

import it.unibo.risikoop.controller.interfaces.logicgame.LogicReinforcementCalculator;
import it.unibo.risikoop.model.interfaces.GameManager;

/**
 * Computes the number of initial reinforcement units allocated to a player
 * at the start of the game, based on the total territories and player count.
 * <p>
 * Uses a configurable average units-per-territory factor to determine
 * the initial distribution.
 * </p>
 */
public final class LogicCalcInitialUnitsImpl implements LogicReinforcementCalculator {

    private static final double AVERAGE_UNITS_PER_TERRITORY = 2.5;
    private final GameManager gameManager;

    /**
     * Constructs a new initial units calculator using the given game manager.
     *
     * @param gameManager the {@link GameManager} providing current game state
     */
    public LogicCalcInitialUnitsImpl(final GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public int calcPlayerUnits() {
        final int territories = gameManager.getTerritories().size();
        final int players = gameManager.getPlayers().size();
        final double avg = (double) territories / players;
        return (int) Math.floor(AVERAGE_UNITS_PER_TERRITORY * avg);
    }

}
