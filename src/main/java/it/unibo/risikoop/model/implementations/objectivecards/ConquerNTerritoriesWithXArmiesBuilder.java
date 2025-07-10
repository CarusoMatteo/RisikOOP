package it.unibo.risikoop.model.implementations.objectivecards;

import it.unibo.risikoop.model.implementations.PlayerGameContext;
import it.unibo.risikoop.model.implementations.specification.ConquerTerritoriesWithMinArmiesSpec;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Specification;

public class ConquerNTerritoriesWithXArmiesBuilder
        extends AbstractObjectiveCardBuilder {

    private final int territories;
    private final int armies;

    public ConquerNTerritoriesWithXArmiesBuilder(final GameManager gameManager, final Player owner,
            final int territories, final int armies) {
        super(gameManager, owner);
        this.territories = territories;
        this.armies = armies;
    }

    @Override
    protected String buildDescription() {
        final String description = "Conquer at least "
                + territories
                + " territories with at least "
                + armies
                + " armies.";
        return description;
    }

    @Override
    protected Specification<PlayerGameContext> buildSpecification() {
        return new ConquerTerritoriesWithMinArmiesSpec(territories, armies);
    }

}
