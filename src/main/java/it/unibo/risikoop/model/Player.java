package it.unibo.risikoop.model;

import java.util.List;

public interface Player {
    /**
     * 
     * @return
     */
    public Integer getTotalUnits();

    /**
     * 
     * @return
     */
    public List<TerritoryCard> getTerritoryCards();

    /**
     * 
     * @return player's name
     */
    public String getName();

    /**
     * @return the player's territories
     */
    List<Territory> getTerritories();

}