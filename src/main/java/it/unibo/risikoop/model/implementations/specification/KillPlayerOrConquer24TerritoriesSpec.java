package it.unibo.risikoop.model.implementations.specification;

import java.util.Objects;

import it.unibo.risikoop.model.implementations.PlayerGameContext;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Specification;

/**
 * Specification that checks if a player has either killed a specified target player
 * or conquered at least 24 territories.
 * This specification combines two conditions using logical OR.
 */
public final class KillPlayerOrConquer24TerritoriesSpec implements Specification<PlayerGameContext> {
    private static final int DEFAULT_REQUIRED_TERRITORIES = 24;
    private final Player target;
    private final int requiredTerritories;

    /**
     * Constructs a KillPlayerOrConquer24TerritoriesSpec with the specified target player.
     * The required number of territories is set to the default value of 24.
     *
     * @param target the player that must
     */
    public KillPlayerOrConquer24TerritoriesSpec(final Player target) {
        this.target = Objects.requireNonNull(target, "target cannot be null");
        this.requiredTerritories = DEFAULT_REQUIRED_TERRITORIES;
    }

    @Override
    public boolean isSatisfiedBy(final PlayerGameContext candidate) {
        final Specification<PlayerGameContext> killPlayer = new KillPlayerSpec(target);
        // final Specification<PlayerGameContext> conquerTerritories = new ConquerTerritoriesSpec(requiredTerritories);
        final Specification<PlayerGameContext> conquerTerritories = new ConquerTerritoriesWithMinArmiesSpec(requiredTerritories, 1);


        return killPlayer.or(conquerTerritories).isSatisfiedBy(candidate);
    }

}
