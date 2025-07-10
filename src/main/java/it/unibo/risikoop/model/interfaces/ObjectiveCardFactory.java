package it.unibo.risikoop.model.interfaces;

public interface ObjectiveCardFactory {

    /**
     * Creates an ObjectiveCard.
     *
     * @return a new ObjectiveCard instance.
     */
    ObjectiveCard createObjectiveCard(Player owner);
}
