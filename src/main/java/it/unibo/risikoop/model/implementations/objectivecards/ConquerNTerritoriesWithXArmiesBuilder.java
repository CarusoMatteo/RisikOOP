package it.unibo.risikoop.model.implementations.objectivecards;

import it.unibo.risikoop.model.implementations.PlayerGameContext;
import it.unibo.risikoop.model.implementations.specification.ConquerTerritoriesWithMinArmiesSpec;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Specification;

/**
 * Builder for an objective card that requires conquering at least a specified number
 * of territories with a minimum number of armies.
 */
public final class ConquerNTerritoriesWithXArmiesBuilder
        extends AbstractObjectiveCardBuilder {

    private final int territories;
    private final int armies;

    /**
     * Constructs a ConquerNTerritoriesWithXArmiesBuilder with the specified GameManager,
     * owner, number of territories, and minimum number of armies.
     *
     * @param gameManager the GameManager that manages the game state
     * @param owner       the player who owns the objective card
     * @param territories the minimum number of territories to conquer
     * @param armies      the minimum number of armies required to conquer each territory
     */
    public ConquerNTerritoriesWithXArmiesBuilder(final GameManager gameManager, final Player owner,
            final int territories, final int armies) {
        super(gameManager, owner);
        this.territories = territories;
        this.armies = armies;
    }

    @Override
    protected String buildDescription() {
        return "Conquer at least "
                + territories
                + " territories with at least "
                + armies
                + " armies.";
    }

    @Override
    protected Specification<PlayerGameContext> buildSpecification() {
        return new ConquerTerritoriesWithMinArmiesSpec(territories, armies);
    }

}
