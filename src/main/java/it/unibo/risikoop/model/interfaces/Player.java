package it.unibo.risikoop.model.interfaces;

import java.util.List;
import java.util.Optional;

import it.unibo.risikoop.model.implementations.Color;

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

    /**
     * Returns the player who eliminated this player.
     *
     * @return the Player who eliminated this player
     */
    Optional<Player> getKiller();

}