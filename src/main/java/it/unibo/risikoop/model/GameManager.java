package it.unibo.risikoop.model;

import java.util.List;

public interface GameManager {
    /**
     * Add a new player if not already present
     * 
     * @param name new player's name
     * @return true if the player has been added succesfully
     */
    public boolean addPlayer(String name);

    /**
     * Spartisce equamente i territori tra i player e dà ad ognuno una carta
     * obiettivo
     */
    public void finishingInizialization();

    /**
     * Add new units to a territory
     * 
     * @param territory
     * @param units
     */
    public void addUnits(Territory territory, int units);

    public List<Territory> getPlayerTerritory();

}
