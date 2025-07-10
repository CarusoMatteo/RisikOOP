package it.unibo.risikoop.model.implementations.specification;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import it.unibo.risikoop.model.implementations.PlayerGameContext;
import it.unibo.risikoop.model.interfaces.Continent;
import it.unibo.risikoop.model.interfaces.Specification;

public final class ConquerContinentsSpec implements Specification<PlayerGameContext> {
    private final Set<Continent> continents;

    public ConquerContinentsSpec(final Set<Continent> continents) {
        this.continents = Objects.requireNonNull(continents, "continents cannot be null");
        if (continents.isEmpty()) {
            throw new IllegalArgumentException("Continents set cannot be empty.");
        }
    }

    @Override
    public boolean isSatisfiedBy(final PlayerGameContext ctx) {
        return ctx.player().getTerritories().containsAll(
                continents.stream()
                        .flatMap(continent -> continent.getTerritories().stream())
                        .collect(Collectors.toSet()));
    }
}
