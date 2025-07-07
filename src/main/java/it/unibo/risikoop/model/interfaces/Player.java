package it.unibo.risikoop.model.interfaces;

import java.util.List;

import it.unibo.risikoop.model.implementations.Color;

/**
 * 
 */
public interface Player {
    /**
     * 
     * @return the total units
     */
    Integer getTotalUnits();

    /**
     * 
     * @param card
     */
    void addTerritoryCard(TerritoryCard card);

    /**
     * 
     * @return the all the possessed territory cards
     */
    List<TerritoryCard> getTerritoryCards();

    /**
     * 
     * @return player's name
     */
    String getName();

    /**
     * @return player's color
     */
    Color getColor();

    /**
     * @return the player's territories
     */
    List<Territory> getTerritories();

}
