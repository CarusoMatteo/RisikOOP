package it.unibo.risikoop.model.implementations.gamecards.objectivecards;

import java.util.Objects;
import java.util.Random;

import it.unibo.risikoop.model.implementations.ObjectiveCardImpl;
import it.unibo.risikoop.model.implementations.PlayerGameContext;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.ObjectiveCard;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Specification;

/**
 * Abstract base class for building objective cards.
 * This class provides a template for creating objective cards with a specific
 * description and specification.
 */
public abstract class AbstractObjectiveCardBuilder {
    /** The GameManager that manages the game state. */
    private final GameManager gameManager;
    /** Random generator for subclasses. */
    private final Random random;
    /** The player who owns the objective card. */
    private final Player owner;
    /**
     * Constructs an AbstractObjectiveCardBuilder with the specified GameManager and
     * owner.
     *
     * @param gameManager the GameManager that manages the game state
     * @param owner       the player who owns the objective card
     */
    protected AbstractObjectiveCardBuilder(final GameManager gameManager, final Player owner) {
        this.gameManager = Objects.requireNonNull(gameManager);
        this.owner = Objects.requireNonNull(owner, "owner can not be null");
        this.random = new Random();
    }

    /**
     * Creates an ObjectiveCard using the provided description and specification.
     * This method must be implemented by subclasses to define the specific
     * objective card's description and win condition.
     *
     * @return a new ObjectiveCard instance
     */
    public ObjectiveCard createCard() {
        Objects.requireNonNull(owner, "owner can not be null");
        final String description = buildDescription();
        final Specification<PlayerGameContext> spec = buildSpecification();
        return new ObjectiveCardImpl(description, owner, gameManager, spec);
    }

    /**
     * Returns the GameManager.
     * 
     * @return the GameManager
     */
    protected GameManager getGameManager() {
        return gameManager;
    }

    /**
     * Returns the Random instance.
     * 
     * @return the Random instance
     */
    protected Random getRandom() {
        return random;
    }

    /**
     * Builds the description of the objective card.
     * This method must be implemented by subclasses to provide a specific
     * description for the objective card.
     *
     * @return the description of the objective card
     */
    protected abstract String buildDescription();

    /**
     * Builds the specification for the objective card.
     * This method must be implemented by subclasses to define the win condition
     * for the objective card.
     *
     * @return a Specification that defines the win condition for this objective card
     */
    protected abstract Specification<PlayerGameContext> buildSpecification();
}
