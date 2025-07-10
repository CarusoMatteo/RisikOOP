package it.unibo.risikoop.model.interfaces.cards;

import it.unibo.risikoop.model.interfaces.Player;

/**
 * Base interface for game cards in Risik
 */
public interface GameCard {
    /**
     * @return the type of this card (INFANTRY, CAVALRY, ARTILLERY or WILD)
     */
    UnitType getType();

    /**
     * @return the owner of this card
     */
    Player getOwner();
}