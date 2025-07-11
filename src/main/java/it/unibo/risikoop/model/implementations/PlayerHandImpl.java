package it.unibo.risikoop.model.implementations;

import java.util.Collection;
import java.util.Set;

import it.unibo.risikoop.model.interfaces.PlayerHand;
import it.unibo.risikoop.model.interfaces.cards.GameCard;

/**
 * Implementation of the PlayerHand interface, representing a player's hand of
 * cards.
 * This class provides methods to manage the cards in a player's hand, including
 * adding, removing, and checking for cards.
 */
public final class PlayerHandImpl implements PlayerHand {

    private final Set<GameCard> cards;

    /**
     * Default constructor initializing an empty player hand.
     */
    public PlayerHandImpl() {
        this.cards = Set.of();
    }

    @Override
    public boolean addCard(final GameCard card) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }
        return cards.add(card);

    }

    @Override
    public boolean addCards(final Collection<GameCard> cards) {
        if (cards == null || cards.isEmpty()) {
            throw new IllegalArgumentException("Cards collection cannot be null or empty");
        }

        return cards.addAll(cards);
    }

    @Override
    public boolean clear() {
        cards.clear();
        return true;
    }

    @Override
    public boolean contains(final GameCard card) {
        return cards.contains(card);
    }

    @Override
    public Set<GameCard> getCards() {
        return Set.copyOf(cards);
    }

    @Override
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    @Override
    public boolean removeCard(final GameCard card) {
        return cards.remove(card);
    }

    @Override
    public boolean removeCards(final Collection<GameCard> cards) {
        return this.cards.removeAll(cards);
    }

    @Override
    public int size() {
        return cards.size();
    }
}
