package it.unibo.risikoop.model;

import java.util.List;

public interface GameManager {
    /**
     * Add a new player if not already present
     * 
     * @param name new player's name
     * @return true if the player has been added succesfully
     */
    public boolean addPlayer(String name, Color col);

    /**
     * remove a new player with a certain name if already present
     * 
     * @param name player's name
     * @return true if the player has been removed succesfully
     */
    public boolean removePlayer(String name);

    /**
     * Equally gives territory to each player and a goal card
     */
    public void finishingInizialization();

    /**
     * Add new units to a territory
     * 
     * @param territory
     * @param units
     */
    public void addUnits(Territory territory, int units);

    /**
     * 
     * @return players list
     */
    public List<Player> getPlayers();
    // TODO: Create active player
    // TODO: Create player change

}
