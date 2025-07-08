package it.unibo.risikoop.model.implementations.objectivecards;

import it.unibo.risikoop.model.implementations.PlayerGameContext;
import it.unibo.risikoop.model.implementations.specification.KillPlayerOrConquer24TerritoriesSpec;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Specification;

public class KillOrConquer24Builder extends AbstractObjectiveCardBuilder {

    private final Player target;

    public KillOrConquer24Builder(GameManager gameManager, Player owner) {
        super(gameManager, owner);
        this.target = gameManager.getPlayers()
                .get(super.random.nextInt(gameManager.getPlayers().size()));
    }

    @Override
    protected String buildDescription() {
        String description = "Kill player " +
                target.getName() +
                "from the board or conquer at least 24 territories.";
        return description;
    }

    @Override
    protected Specification<PlayerGameContext> buildSpecification() {
        return new KillPlayerOrConquer24TerritoriesSpec(target);
    }

}
