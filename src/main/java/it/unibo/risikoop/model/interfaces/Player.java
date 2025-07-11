package it.unibo.risikoop.model.interfaces;

import java.util.List;
import java.util.Optional;

import it.unibo.risikoop.model.implementations.Color;
import it.unibo.risikoop.model.interfaces.cards.GameCard;
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
    void addGameCard(GameCard card);

    /**
     * 
     * @return the all the possessed territory cards
     */
    List<TerritoryCard> getTerritoryCards();

    /**
     * 
     * @return the all the game cards
     */
    List<GameCard> getGameCards();

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

    /**
     * Adds number of units to the player's total units to place.
     * 
     * @param units the number of units to add
     * @throws IllegalArgumentException if units is negative
     */

    void addUnitsToPlace(int units);

    /**
     * Removes number of units from the player's total units to place.
     * 
     * @param units the number of units to remove
     * @throws IllegalArgumentException if units is negative or greater than the
     *                                  current units to place
     */
    void removeUnitsToPlace(int units);

    /**
     * Gets the number of units the player can place.
     * 
     * @return the number of units to place left
     */
    int getUnitsToPlace();

    /**
     * Checks if the player is eliminated.
     */
    boolean isEliminated();

    /**
     * Adds a territory to the player's territories.
     * @param territory
     * @return true if the territory was added successfully, false otherwise.
     */
    boolean addTerritory(Territory territory);

}
