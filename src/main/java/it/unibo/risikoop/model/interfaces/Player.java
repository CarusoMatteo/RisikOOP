package it.unibo.risikoop.model.interfaces;

import java.util.List;
import java.util.Optional;

import it.unibo.risikoop.model.implementations.Color;

/**
 * 
 */
public interface Player {
    /**
     * add a new territory to the player.
     * 
     * @param territories
     * @return if it was possible to add the territory
     * 
     */
    boolean addTerritories(List<Territory> territories);

    default boolean addTerritory(Territory territory) {
        return addTerritories(List.of(territory));
    }

    /**
     * remove a territory from the player.
     * 
     * @param territory
     * @return if it was possible to remove the territory
     * 
     */
    boolean removeTerritory(Territory territory);

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
