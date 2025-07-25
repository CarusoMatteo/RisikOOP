package it.unibo.risikoop.model.interfaces.holder;

import java.util.List;

import it.unibo.risikoop.model.implementations.Color;
import it.unibo.risikoop.model.interfaces.Player;

/**
 * An interface for holding a list of players.
 */
public interface PlayersHolder {
    /**
     * Add a new player if not already present.
     * 
     * @param name new player's name
     * @param col  new player's color
     * @return true if the player has been added succesfully
     */
    boolean addPlayer(String name, Color col);

    /**
     * 
     * @return players list.
     */
    List<Player> getPlayers();

    /**
     * remove a new player with a certain name if already present.
     * 
     * @param name player's name
     * @return true if the player has been removed succesfully
     */

    boolean removePlayer(String name);
}
