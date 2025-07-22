package it.unibo.risikoop.model.implementations.gamecards.objectivecards;

import java.io.Serial;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import it.unibo.risikoop.model.implementations.PlayerGameContext;
import it.unibo.risikoop.model.implementations.specification.ConquerContinentsSpec;
import it.unibo.risikoop.model.interfaces.Continent;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Specification;

/**
 * Builder for an objective card that requires conquering a specified number of
 * continents.
 * The continents are randomly selected from the available continents in the
 * game.
 */
public final class ConquerNContinetsBuilder extends AbstractObjectiveCardBuilder {

    private static final int MIN_TERRITORIES = 2;
    private final Set<Continent> continents;

    /**
     * Constructs a ConquerNContinetsBuilder with the specified GameManager and
     * owner.
     * The continents are randomly selected to ensure a balanced objective.
     *
     * @param gameManager the GameManager that manages the game state
     * @param owner       the player who owns the objective card
     */
    public ConquerNContinetsBuilder(final GameManager gameManager, final Player owner) {
        super(gameManager, owner);
        this.continents = createBalanceObjective();
    }

    @Override
    protected String buildDescription() {
        // nice
        return continents.stream()
                .map(Continent::getName)
                .collect(Collectors.joining(
                        ", ",
                        "Conquer all this continent: ",
                        "."));
    }

    @Override
    protected Specification<PlayerGameContext> buildSpecification() {
        return new ConquerContinentsSpec(continents);
    }

    private Set<Continent> createBalanceObjective() {
        int territories = 0;
        final Set<Continent> selectedContinents = new HashSet<>();
        while (territories < MIN_TERRITORIES) {
            final Optional<Continent> continent = getRandomContinent(selectedContinents);
            if (continent.isPresent()) {
                selectedContinents.add(continent.get());
                territories += continent.get().getTerritories().size();
            }
        }

        return selectedContinents;
    }

    private Optional<Continent> getRandomContinent(final Set<Continent> selctedContinents) {
        return super.getGameManager().getContinents().stream()
                .filter(e -> !selctedContinents.contains(e))
                .findAny();
    }

}
