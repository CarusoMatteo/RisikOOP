package it.unibo.risikoop.model.implementations.gamecards.combos;

import java.util.Set;

import it.unibo.risikoop.model.interfaces.PlayerHand;
import it.unibo.risikoop.model.interfaces.cards.ComboCheckStrategy;
import it.unibo.risikoop.model.interfaces.cards.GameCard;

public class AllDifferentCombo implements ComboCheckStrategy {

    @Override
    public Boolean comboIsValid(Set<GameCard> cards) {
        if (cards == null || cards.size() != 3) {
            throw new IllegalArgumentException("The hand must contain 3 cards.");
        }

        // Check if all cards in the set have different UnitTypes.
        return cards.stream()
                .map(c -> c.getType())
                .distinct()
                .count() == 3;
    }

    @Override
    public Boolean comboIsPossibile(PlayerHand hand) {
        if (hand == null) {
            throw new IllegalArgumentException("The hand must not be null.");
        }

        // Check if the hand contains at least 3 (to account for WILD)
        // different UnitTypes.
        return hand.getCards()
                .stream()
                .map(c -> c.getType())
                .distinct()
                .count() >= 3;
    }

}
