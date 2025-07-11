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
    private boolean newRound = false;

    /**
     * Constructs a TurnManagerImpl with the specified list of players.
     * Initializes the current player index to 0.
     *
     * @param players the list of players in the game
     */
    public TurnManagerImpl(List<Player> players) {

        if (players == null || players.isEmpty()) {
            throw new IllegalArgumentException("Player list must not be null or empty");
        }

        this.players = players;
        currentPlayerIndex = 0;
    }

    @Override
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    @Override
    public Player nextPlayer() {
        int prevIndex = currentPlayerIndex;
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } while (players.get(currentPlayerIndex).isEliminated());

        newRound = prevIndex > currentPlayerIndex;

        return players.get(currentPlayerIndex);
    }

    @Override
    public Boolean isNewRound() {
        return newRound;
    }
}
