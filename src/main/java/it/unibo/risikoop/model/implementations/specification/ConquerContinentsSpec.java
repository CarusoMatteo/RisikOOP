package it.unibo.risikoop.model.implementations.specification;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import it.unibo.risikoop.model.implementations.PlayerGameContext;
import it.unibo.risikoop.model.interfaces.Continent;
import it.unibo.risikoop.model.interfaces.Specification;

/**
 * Specification that checks if a player has conquered specified continents
 * by ensuring that all territories within those continents are owned by the player.
 */
public final class ConquerContinentsSpec implements Specification<PlayerGameContext> {
    private final Set<Continent> continents;

    /**
     * Constructs a ConquerContinentsSpec with the specified set of continents.
     * The continents set must not be null or empty.
     *
     * @param continents the set of continents to check
     * @throws IllegalArgumentException if the continents set is null or empty
     */
    public ConquerContinentsSpec(final Set<Continent> continents) {
        this.continents = Objects.requireNonNull(continents, "continents cannot be null");
        if (continents.isEmpty()) {
            throw new IllegalArgumentException("Continents set cannot be empty.");
        }
    }

    @Override
    public boolean isSatisfiedBy(final PlayerGameContext ctx) {
        // nice
        return ctx.player().getTerritories().containsAll(
                continents.stream()
                        .flatMap(continent -> continent.getTerritories().stream())
                        .collect(Collectors.toSet()));
    }
}
