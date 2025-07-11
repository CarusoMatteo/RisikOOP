package it.unibo.risikoop.model.implementations;

import java.util.List;

import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.TurnManager;

/**
 * Implementation of the TurnManager interface.
 * This class manages the turn order of players in a game,
 * allowing to get the current player and move to the next player,
 * skipping any eliminated players.
 */
public class TurnManagerImpl implements TurnManager {

    private final List<Player> players;
    private int currentPlayerIndex;

    /**
     * Constructs a TurnManagerImpl with the specified list of players.
     * Initializes the current player index to 0.
     *
     * @param players the list of players in the game
     */
    public TurnManagerImpl(List<Player> players) {
        this.players = players;
        currentPlayerIndex = 0;
    }

    @Override
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    @Override
    public Player nextPlayer() {
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } while (players.get(currentPlayerIndex).isEliminated());

        return players.get(currentPlayerIndex);
    }
}
