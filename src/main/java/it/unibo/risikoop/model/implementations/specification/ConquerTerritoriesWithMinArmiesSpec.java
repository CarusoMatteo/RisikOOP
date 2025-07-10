package it.unibo.risikoop.model.implementations.specification;

import it.unibo.risikoop.model.implementations.PlayerGameContext;
import it.unibo.risikoop.model.interfaces.Specification;

/**
 * Specification that checks if a player has conquered a specified number of territories
 * with a minimum number of armies in each territory.
 * The player must own at least the required number of territories with the specified minimum armies.
 */
public final class ConquerTerritoriesWithMinArmiesSpec implements Specification<PlayerGameContext> {
    private final int minArmies;
    private final int minTerritories;

    /**
     * Constructs a ConquerTerritoriesWithMinArmiesSpec with the specified minimum number of armies
     * and minimum number of territories.
     * Both parameters must be greater than zero.
     *
     * @param minArmies      the minimum number of armies required in each territory
     * @param minTerritories the minimum number of territories the player must conquer
     * @throws IllegalArgumentException if minArmies or minTerritories is less than or equal to zero
     */
    public ConquerTerritoriesWithMinArmiesSpec(final int minArmies, final int minTerritories) {
        if (minArmies <= 0 || minTerritories <= 0) {
            throw new IllegalArgumentException("Minimum armies or Territories must be greater than zero.");
        }

        this.minArmies = minArmies;
        this.minTerritories = minTerritories;
    }

    @Override
    public boolean isSatisfiedBy(final PlayerGameContext ctx) {
        return ctx.player().getTerritories().stream()
                .filter(territory -> territory.getUnits() >= minArmies)
                .count() >= minTerritories;
    }

}
