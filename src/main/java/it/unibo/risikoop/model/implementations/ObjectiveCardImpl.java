package it.unibo.risikoop.model.implementations;

import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.ObjectiveCard;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Specification;

public final class ObjectiveCardImpl implements ObjectiveCard {

    private final String description;
    private final Player owner;
    private final GameManager gameManager;
    private final Specification<PlayerGameContext> winCond;

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

    public String getDescription() {
        return description;
    }

}
