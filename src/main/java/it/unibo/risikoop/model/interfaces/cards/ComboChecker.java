package it.unibo.risikoop.model.interfaces.cards;

import java.util.Optional;
import java.util.Set;

import it.unibo.risikoop.model.interfaces.PlayerHand;

/**
 * This class allows to check if a combo is usable and to use it.
 */
public interface ComboChecker {

    /**
     * Checks if any combo is possible with the given player's hand.
     * 
     * @param hand the player's hand.
     * @return if any combo is possible.
     */
    boolean anyComboIsPossible(PlayerHand hand);

    /**
     * Uses any combo with the given set of cards.
     * 
     * @param cards the set of cards to use for the combo.
     * @return the amount of units rewarded for the combo,
     *         or an empty Optional if no combo can be used with those cards.
     */
    Optional<Integer> useCombo(Set<GameCard> cards);

}
