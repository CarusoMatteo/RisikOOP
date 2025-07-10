package it.unibo.risikoop.model.implementations.specification;

import java.util.Objects;

import it.unibo.risikoop.model.implementations.PlayerGameContext;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Specification;

public class KillPlayerOrConquer24TerritoriesSpec implements Specification<PlayerGameContext> {
    private static final int DEFAULT_REQUIRED_TERRITORIES = 24;
    private final Player target;
    private final int requiredTerritories;

    public KillPlayerOrConquer24TerritoriesSpec(Player target) {
        this.target = Objects.requireNonNull(target, "target cannot be null");
        this.requiredTerritories = DEFAULT_REQUIRED_TERRITORIES;
    }

    @Override
    public boolean isSatisfiedBy(PlayerGameContext candidate) {
        Specification<PlayerGameContext> killPlayer = new KillPlayerSpec(target);
        Specification<PlayerGameContext> conquerTerritories = new ConquerTerritoriesSpec(requiredTerritories);

        return killPlayer.or(conquerTerritories).isSatisfiedBy(candidate);
    }

}
