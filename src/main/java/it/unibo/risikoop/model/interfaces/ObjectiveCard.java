package it.unibo.risikoop.model.interfaces;

/**
 * Represents an objective card in the game.
 * Objective cards define win conditions that players must achieve to win the game.
 */
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
