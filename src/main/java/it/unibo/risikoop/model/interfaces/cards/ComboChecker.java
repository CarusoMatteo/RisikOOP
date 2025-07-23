package it.unibo.risikoop.model.interfaces.cards;

import java.util.Optional;
import java.util.Set;

/**
 * This class allows to check if a combo is usable and to use it.
 */
public interface ComboChecker {

    /**
     * Uses any combo with the given set of cards.
     * 
     * @param cards the set of cards to use for the combo.
     * @return the amount of units rewarded for the combo,
     *         or an empty Optional if no combo can be used with those cards.
     *         This does not include the reward if the player owns the territory
     *         represented by a card used in the combo.
     */
    Optional<Integer> useCombo(Set<GameCard> cards);

}
