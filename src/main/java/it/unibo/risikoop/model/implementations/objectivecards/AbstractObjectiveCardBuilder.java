package it.unibo.risikoop.model.implementations.objectivecards;

import java.util.Objects;
import java.util.Random;

import it.unibo.risikoop.model.implementations.ObjectiveCardImpl;
import it.unibo.risikoop.model.implementations.PlayerGameContext;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.ObjectiveCard;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Specification;

public abstract class AbstractObjectiveCardBuilder {

    protected final GameManager gameManager;
    protected final Random random;
    protected final Player owner;

    protected AbstractObjectiveCardBuilder(final GameManager gameManager, final Player owner) {
        this.gameManager = Objects.requireNonNull(gameManager);
        this.owner = Objects.requireNonNull(owner, "owner can not be null");
        this.random = new Random();
    }

    public ObjectiveCard createCard() {
        Objects.requireNonNull(owner, "owner can not be null");
        final String description = buildDescription();
        final Specification<PlayerGameContext> spec = buildSpecification();
        return new ObjectiveCardImpl(description, owner, gameManager, spec);
    }

    protected abstract String buildDescription();

    protected abstract Specification<PlayerGameContext> buildSpecification();
}
