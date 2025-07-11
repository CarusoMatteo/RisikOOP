package it.unibo.risikoop.model.interfaces;

/**
 * Interface for managing the turn order in a game.
 * It provides methods to get the current player, move to the next player.
 */

public interface TurnManager {
    /**
     * Returns the current player whose turn it is.
     *
     * @return the current Player
     */
    Player getCurrentPlayer();

    /**
     * Moves to the next player in the turn order,
     * skipping any eliminated players.
     *
     * @return the Player whose turn is next
     */
    Player nextPlayer();
}