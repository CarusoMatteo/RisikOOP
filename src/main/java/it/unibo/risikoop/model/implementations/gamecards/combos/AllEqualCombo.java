package it.unibo.risikoop.model.implementations.gamecards.combos;

import java.util.Objects;
import java.util.Set;

import it.unibo.risikoop.model.interfaces.PlayerHand;
import it.unibo.risikoop.model.interfaces.cards.ComboCheckStrategy;
import it.unibo.risikoop.model.interfaces.cards.GameCard;
import it.unibo.risikoop.model.interfaces.cards.UnitType;

public abstract class AllEqualCombo implements ComboCheckStrategy {

    @Override
    public Boolean comboIsValid(final Set<GameCard> hand) {
        if (Objects.isNull(hand) || hand.size() != 3) {
            throw new IllegalArgumentException("The hand must contain 3 cards.");
        }

        // Check if the hand contains only <getUnitType> cards.

        return hand.stream()
                .filter(card -> card.getType() == getUnitType())
                .count() == 3;
    }

    @Override
    public Boolean comboIsPossibile(final PlayerHand hand) {
        if (Objects.isNull(hand)) {
            throw new IllegalArgumentException("The hand must not be null.");
        }

        // Check if the hand contains at least 3 <UnitType> cards.

        return hand.getCards()
                .stream()
                .filter(card -> card.getType() == getUnitType())
                .count() >= 3;
    }

    protected abstract UnitType getUnitType();

    protected abstract int getUnitRewardAmount();
}
