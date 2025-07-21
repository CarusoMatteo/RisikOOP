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
public final class TurnManagerImpl implements TurnManager {

    private final List<Player> players;
    private int currentPlayerIndex;
    private boolean isLast;

    /**
     * Constructs a TurnManagerImpl with the specified list of players.
     * Initializes the current player index to 0.
     *
     * @param players the list of players in the game
     */
    public TurnManagerImpl(final List<Player> players) {

        if (players == null || players.isEmpty()) {
            throw new IllegalArgumentException("Player list must not be null or empty");
        }

        this.isLast = false;
        this.players = List.copyOf(players);
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

        // nice
        isLast = players.stream().filter(p -> !p.isEliminated()).toList().getLast()
                .equals(players.get(currentPlayerIndex));

        return players.get(currentPlayerIndex);
    }

    @Override
    public Boolean isLastPlayer() {
        return isLast;
    }
}
