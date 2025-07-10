package it.unibo.risikoop.model.implementations.specification;

import it.unibo.risikoop.model.implementations.PlayerGameContext;
import it.unibo.risikoop.model.interfaces.Specification;

/**
 * Specification that checks if a player has conquered a specified number of territories.
 * The player must own at least the required number of territories to satisfy this specification.
 */
public final class ConquerTerritoriesSpec implements Specification<PlayerGameContext> {
    private final int requiredTerritories;

    /**
     * Constructs a ConquerTerritoriesSpec with the specified number of required territories.
     * The required territories must be greater than zero.
     *
     * @param requiredTerritories the minimum number of territories the player must conquer
     * @throws IllegalArgumentException if requiredTerritories is less than or equal to zero
     */
    public ConquerTerritoriesSpec(final int requiredTerritories) {

        if (requiredTerritories <= 0) {
            throw new IllegalArgumentException("Required territories must be greater than zero.");
        }

        this.requiredTerritories = requiredTerritories;
    }

    @Override
    public boolean isSatisfiedBy(final PlayerGameContext ctx) {
        return ctx.player().getTerritories().size() >= requiredTerritories;
    }
}
