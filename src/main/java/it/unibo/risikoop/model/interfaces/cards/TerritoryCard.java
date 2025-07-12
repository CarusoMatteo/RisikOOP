package it.unibo.risikoop.model.interfaces.cards;

import it.unibo.risikoop.model.interfaces.Territory;

/**
 * Represents a card that corresponds to a specific territory in the game.
 * <p>
 * Each TerritoryCard is associated with a specific Territory object.
 */
public interface TerritoryCard extends GameCard {
    /**
     * @return the territory represented by this card
     */
    Territory getAssociatedTerritory();
}
