package it.unibo.risikoop.model.implementations.specification;

import it.unibo.risikoop.model.implementations.PlayerGameContext;
import it.unibo.risikoop.model.interfaces.Specification;

public final class ConquerTerritoriesSpec implements Specification<PlayerGameContext> {
    private final int requiredTerritories;

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
