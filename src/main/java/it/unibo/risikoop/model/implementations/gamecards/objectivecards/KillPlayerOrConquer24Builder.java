package it.unibo.risikoop.model.implementations.gamecards.objectivecards;

import it.unibo.risikoop.model.implementations.PlayerGameContext;
import it.unibo.risikoop.model.implementations.specification.KillPlayerOrConquer24TerritoriesSpec;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Specification;

/**
 * Builder for an objective card that requires either killing a specific player
 * or conquering at least 24 territories.
 * The target player is randomly selected from the list of players in the game.
 */
public final class KillPlayerOrConquer24Builder extends AbstractObjectiveCardBuilder {

    private final Player target;

    /**
     * Constructs a KillPlayerOrConquer24Builder with the specified GameManager
     * and owner.
     * The target player is randomly selected from the list of players in the game.
     *
     * @param gameManager the GameManager that manages the game state
     * @param owner       the player who owns the objective card
     */
    public KillPlayerOrConquer24Builder(final GameManager gameManager, final Player owner) {
        super(gameManager, owner);
        this.target = super.getGameManager().getPlayers()
                .get(super.getRandom().nextInt(super.getGameManager().getPlayers().size()));
    }

    @Override
    protected String buildDescription() {
        return "Kill player "
                + target.getName()
                + "from the board or conquer at least 24 territories.";
    }

    @Override
    protected Specification<PlayerGameContext> buildSpecification() {
        return new KillPlayerOrConquer24TerritoriesSpec(target);
    }
}
