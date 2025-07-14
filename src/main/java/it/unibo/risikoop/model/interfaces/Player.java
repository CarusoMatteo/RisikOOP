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
     * Returns the number of units this player can place.
     * 
     * @return the number of units to place.
     */
    int getUnitsToPlace();

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
     * Return this player's hand.
     * 
     * @return the player's hand.
     */
    PlayerHand getHand();

    /**
     * Checks if the player is eliminated.
     */
    boolean isEliminated();

    /**
     * Returns the objective card of this player.
     * 
     * @return the objective card of this player.
     */
    ObjectiveCard getObjectiveCard();

    /**
     * Set objective card of this player.
     */
    void setObjectiveCard(ObjectiveCard objectiveCard);
}
