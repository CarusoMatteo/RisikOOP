package it.unibo.risikoop.model.interfaces;

import java.util.List;
import java.util.Optional;

import it.unibo.risikoop.model.implementations.Color;
import it.unibo.risikoop.model.interfaces.cards.TerritoryCard;

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

    /**
     * Returns the player who eliminated this player.
     *
     * @return the Player who eliminated this player
     */
    Optional<Player> getKiller();

    /**
     * Sets the player who eliminated this player.
     *
     * @param killer the Player who eliminated this player
     */
    void setKiller(Player killer);

}
