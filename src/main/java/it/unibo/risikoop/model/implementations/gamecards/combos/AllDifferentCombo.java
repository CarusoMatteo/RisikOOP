package it.unibo.risikoop.model.implementations.gamecards.combos;

import java.util.Objects;
import java.util.Set;

import it.unibo.risikoop.model.interfaces.PlayerHand;
import it.unibo.risikoop.model.interfaces.cards.ComboCheckStrategy;
import it.unibo.risikoop.model.interfaces.cards.GameCard;
import it.unibo.risikoop.model.interfaces.cards.UnitType;

/**
 * Combo that checks for a hand containing 3 different UnitTypes.
 * WILD cards do not count in this combo, as per Risiko rules.
 */
public final class AllDifferentCombo implements ComboCheckStrategy {

    private static final int ALL_DIFFERENT_UNIT_REWARD = 10;

    @Override
    public boolean comboIsValid(final Set<GameCard> cards) {
        if (Objects.isNull(cards) || cards.size() != 3) {
            throw new IllegalArgumentException("The hand must contain 3 cards.");
        }

        // Check if all cards in the set have different UnitTypes.
        return countDistinctNonWildUnitTypes(cards) == 3;
    }

    @Override
    public boolean comboIsPossibile(final PlayerHand hand) {
        if (Objects.isNull(hand) || Objects.isNull(hand.getCards())) {
            throw new IllegalArgumentException("The hand must not be null.");
        }

        // Check if the hand contains 3 different UnitTypes.
        // WILD can not be used in this combo.
        return countDistinctNonWildUnitTypes(hand.getCards()) >= 3;
    }

    @Override
    public int getUnitRewardAmount() {
        return ALL_DIFFERENT_UNIT_REWARD;
    }

    private long countDistinctNonWildUnitTypes(final Set<GameCard> cards) {
        return cards.stream()
                .map(GameCard::getType)
                .filter(t -> !t.equals(UnitType.WILD))
                .distinct()
                .count();
    }
}
