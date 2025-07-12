package it.unibo.risikoop.model.interfaces.cards;

import java.util.Set;

import it.unibo.risikoop.model.interfaces.PlayerHand;

/**
 * Interface to check if combos are valid.
 */
public interface ComboCheckStrategy {

    /**
     * Checks if a specific Set of 3 cards forms a valid combo.
     * 
     * @param cards the Set of cards to check.
     * @return if the Set of cards forms a valid combo.
     * @throws IllegalArgumentException if the Set does not contain exactly 3 cards.
     */
    boolean comboIsValid(Set<GameCard> cards);

    /**
     * Returns if a player has a valid combo in their hand.
     * 
     * @param hand the player's hand.
     * @return if the player has a valid combo.
     */
    boolean comboIsPossibile(PlayerHand hand);

    /**
     * Returns the amount of units rewarded for this combo.
     * 
     * @return the amount of units rewarded for this combo.
     */
    int getUnitRewardAmount();
}
