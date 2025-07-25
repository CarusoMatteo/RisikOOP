package it.unibo.risikoop.model.implementations;

import java.util.Objects;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;

/**
 * Represents the context of a player in a game, encapsulating the player and
 * the game manager.
 * This record is used to pass player-specific information along with the game
 * state.
 *
 * @param player      the player associated with this context
 * @param gameManager the game manager managing the game state
 */
// This supression is necessary because i nead to have player
// and gameManager updated in the PlayerGameContext
@SuppressFBWarnings("EI_EXPOSE_REP")
public record PlayerGameContext(
        Player player,
        GameManager gameManager) {

    /**
     * Constructs a PlayerGameContext with the specified player and game manager.
     * where the player and game manager must not be null.
     * 
     * @param player
     * @param gameManager
     */
    // @SuppressFBWarnings("EI_EXPOSE_REP")
    public PlayerGameContext {
        Objects.requireNonNull(player, "player must not be null");
        Objects.requireNonNull(gameManager, "gameManager must not be null");
    }
}
