package it.unibo.risikoop.model.interfaces;

public interface ObjectiveCard {

    /**
     * Checks if the player has met the win condition of the objective card.
     *
     * @return true if the player has met the win condition, false otherwise
     */
    boolean isAchieved();

    /**
     * Returns the description of the objective card.
     *
     * @return the description of the objective card
     */
    String getDescription();
}
