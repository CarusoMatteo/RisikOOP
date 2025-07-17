package it.unibo.risikoop.model.interfaces;

/**
 * Factory interface for creating ObjectiveCard instances.
 * This interface defines a method to create an ObjectiveCard for a given
 * player.
 */
public interface ObjectiveCardFactory {
    /**
     * Creates an ObjectiveCard.
     *
     * @param owner the player who owns the objective card.
     * @return a new ObjectiveCard instance.
     */
    ObjectiveCard createObjectiveCard(Player owner);
}
