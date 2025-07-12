package it.unibo.risikoop.model.interfaces.cards;

import java.util.Optional;

import it.unibo.risikoop.model.interfaces.Player;

/**
 * Base interface for game cards in Risiko.
 */
public interface GameCard {
    /**
     * @return the type of this card (JACK, CAVALRY, ARTILLERY or WILD)
     */
    UnitType getType();

    /**
     * @return the owner of this card
     */
    Optional<Player> getOwner();

    /**
     * Checks if this card is a territory card.
     *
     * @return true if this card is a territory card, false otherwise
     */
    boolean isTerritoryCard();

    /**
     * Sets the owner of this card.
     *
     * @param player the player who will own this card
     * @return true if the owner was successfully set, false if the card already has
     *         an owner
     */
    boolean updateOwner(Player player);
}
