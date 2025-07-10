package it.unibo.risikoop.model.implementations.specification;

import java.util.Objects;

import it.unibo.risikoop.model.implementations.PlayerGameContext;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Specification;

public final class KillPlayerOrConquer24TerritoriesSpec implements Specification<PlayerGameContext> {
    private static final int DEFAULT_REQUIRED_TERRITORIES = 24;
    private final Player target;
    private final int requiredTerritories;

    public KillPlayerOrConquer24TerritoriesSpec(final Player target) {
        this.target = Objects.requireNonNull(target, "target cannot be null");
        this.requiredTerritories = DEFAULT_REQUIRED_TERRITORIES;
    }

    @Override
    public boolean isSatisfiedBy(final PlayerGameContext candidate) {
        final Specification<PlayerGameContext> killPlayer = new KillPlayerSpec(target);
        final Specification<PlayerGameContext> conquerTerritories = new ConquerTerritoriesSpec(requiredTerritories);

        return killPlayer.or(conquerTerritories).isSatisfiedBy(candidate);
    }

}
