package it.unibo.risikoop.controller.interfaces;

import java.util.Set;

import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.cards.GameCard;

/**
 * Controller interface for managing card game operations in RisikOOP.
 * This interface defines methods for drawing cards, finding combos, and using
 * combos.
 */
public interface CardGameController {

    /**
     * Draws a card from the deck.
     *
     * @return the drawn GameCard
     */
    GameCard drawCard();

    /**
     * Returns if the set contains a valid combo.
     * 
     * @param cards the set of cards to check for a valid combo.
     * @return true if the set contains a valid combo, otherwise false.
     * @throws IllegalArgumentException if the set does not contain exactly 3 cards.
     */
    boolean isComboValid(Set<GameCard> cards);

    /**
     * Uses a combo for the specified player.
     *
     * @param player the player who is using the combo.
     * @param cards  the combo to be used.
     * @throws IllegalArgumentException if the player or their hand is null, or if
     *                                  the cards set does not contain exactly 3
     *                                  cards.
     * @throws IllegalStateException    if the combo is not valid.
     *                                  This must not happen because an invalid
     *                                  combo shouldn't be usable since the "Use
     *                                  combo" button should be disabled.
     */
    void useCombo(Player player, Set<GameCard> cards);

}
