package it.unibo.risikoop.model.interfaces;

import java.util.Set;

import it.unibo.risikoop.model.interfaces.cards.GameCard;

public interface CardDeck {

    /**
     * Draws a card from the deck.
     * 
     * @return the drawn card
     */
    GameCard drawCard();

    /**
     * Adds a set of cards to the deck.
     * 
     * @param card the set of cards to add
     * @return true if the cards were successfully added, false otherwise
     */
    boolean addCards(Set<GameCard> card);

    /**
     * Checks if the deck is empty.
     * 
     * @return true if the deck is empty, false otherwise
     */
    boolean isEmpty();
} 
