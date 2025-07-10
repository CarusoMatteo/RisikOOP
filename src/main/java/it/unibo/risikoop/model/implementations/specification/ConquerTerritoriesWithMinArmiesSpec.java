package it.unibo.risikoop.model.implementations.specification;

import it.unibo.risikoop.model.implementations.PlayerGameContext;
import it.unibo.risikoop.model.interfaces.Specification;

public final class ConquerTerritoriesWithMinArmiesSpec implements Specification<PlayerGameContext> {
    private final int minArmies;
    private final int minTerritories;

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
