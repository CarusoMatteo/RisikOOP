package it.unibo.risikoop.model.interfaces;

import java.util.Collection;
import java.util.Set;


import it.unibo.risikoop.model.interfaces.cards.GameCard;

public interface PlayerHand {

    /**
     * Returns an unmodifiable list of all cards currently in the hand.
     *
     * @return list of cards in hand
     */
    Set<GameCard> getCards();

    /**
     * Adds a single card to the hand.
     *
     * @param card the card to add
     */
    boolean addCard(GameCard card);

    /**
     * Adds multiple cards to the hand in one operation.
     *
     * @param cards the collection of cards to add
     */
    boolean addCards(Collection<GameCard> cards);

    /**
     * Removes a single card from the hand.
     *
     * @param card the card to remove
     * @return true if the card was present and removed, false otherwise
     */
    boolean removeCard(GameCard card);

    /**
     * Removes all specified cards from the hand.
     *
     * @param cards the collection of cards to remove
     * @return true if at least one card was removed, false if none were present
     */
    boolean removeCards(Collection<GameCard> cards);

    /**
     * Checks whether the hand contains the given card.
     *
     * @param card the card to check for
     * @return true if the card is in hand, false otherwise
     */
    boolean contains(GameCard card);

    /**
     * Returns the number of cards currently in hand.
     *
     * @return count of cards in hand
     */
    int size();

    /**
     * Returns true if the hand has no cards.
     *
     * @return true if hand is empty, false otherwise
     */
    boolean isEmpty();

    /**
     * Removes all cards from the hand.
     */
    boolean clear();

}