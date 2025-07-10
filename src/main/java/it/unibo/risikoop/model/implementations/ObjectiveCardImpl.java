package it.unibo.risikoop.model.implementations;

import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.ObjectiveCard;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Specification;

/**
 * Implementation of the ObjectiveCard interface.
 * This class represents an objective card with a description, an owner, and a win condition.
 */
public final class ObjectiveCardImpl implements ObjectiveCard {

    private final String description;
    private final Player owner;
    private final GameManager gameManager;
    private final Specification<PlayerGameContext> winCond;

    /**
     * Constructs an ObjectiveCardImpl with the specified description, owner, game manager, and win condition.
     *
     * @param description the description of the objective card
     * @param owner       the player who owns the objective card
     * @param gameManager the game manager managing the game state
     * @param winCond     the specification defining the win condition for this objective card
     */
    public ObjectiveCardImpl(final String description, final Player owner, final GameManager gameManager,
            final Specification<PlayerGameContext> winCond) {
        this.description = description;
        this.owner = owner;
        this.gameManager = gameManager;
        this.winCond = winCond;
    }

    @Override
    public boolean isAchieved() {
        return winCond.isSatisfiedBy(new PlayerGameContext(owner, gameManager));
    }


    @Override
    public String getDescription() {
        return description;
    }

}
