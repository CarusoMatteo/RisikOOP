package it.unibo.risikoop.model.interfaces;

import java.util.List;

import it.unibo.risikoop.model.Implementations.Color;

public interface Player {
    /**
     * 
     * @return
     */
    public Integer getTotalUnits();

    /**
     * 
     * @param card
     */
    public void addTerritoryCard(TerritoryCard card);

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
     * @return player's color
     */
    public Color getColor();

    /**
     * @return the player's territories
     */
    List<Territory> getTerritories();

}