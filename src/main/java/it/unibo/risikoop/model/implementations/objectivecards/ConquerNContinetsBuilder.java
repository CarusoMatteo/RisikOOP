package it.unibo.risikoop.model.implementations.objectivecards;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import it.unibo.risikoop.model.implementations.PlayerGameContext;
import it.unibo.risikoop.model.implementations.specification.ConquerContinentsSpec;
import it.unibo.risikoop.model.interfaces.Continent;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Specification;

public class ConquerNContinetsBuilder extends AbstractObjectiveCardBuilder {

    private static final int MIN_TERRITORIES = 10;
    private final Set<Continent> continents;

    public ConquerNContinetsBuilder(GameManager gameManager, Player owner) {
        super(gameManager, owner);
        this.continents = createBalanceObjective();
    }

    @Override
    protected String buildDescription() {
        String description = continents.stream()
                .map(Continent::getName)
                .collect(Collectors.joining(
                        ", ",
                        "Conquer all this continent: ",
                        "."));

        return description;
    }

    @Override
    protected Specification<PlayerGameContext> buildSpecification() {
        return new ConquerContinentsSpec(continents);
    }

    private Set<Continent> createBalanceObjective() {
        int territories = 0;
        Set<Continent> selectedContinents = Set.of();
        while (territories < MIN_TERRITORIES) {
            Optional<Continent> continent = getRandomContinent(selectedContinents);
            if (continent.isPresent()) {
                selectedContinents.add(continent.get());
                territories += continent.get().getTerritories().size();
            }
        }

        return selectedContinents;
    }

    private Optional<Continent> getRandomContinent(Set<Continent> selctedContinents) {
        return gameManager.getContinents().stream()
                .filter(e -> !selctedContinents.contains(e))
                .findAny();
    }

}
