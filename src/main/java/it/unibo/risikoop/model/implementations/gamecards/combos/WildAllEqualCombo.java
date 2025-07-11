package it.unibo.risikoop.model.implementations.gamecards.combos;

import java.util.Objects;
import java.util.Set;

import it.unibo.risikoop.model.interfaces.PlayerHand;
import it.unibo.risikoop.model.interfaces.cards.ComboCheckStrategy;
import it.unibo.risikoop.model.interfaces.cards.GameCard;
import it.unibo.risikoop.model.interfaces.cards.UnitType;

/**
 * Combo that checks for a Wild card and two cards of different UnitTypes.
 */
public final class WildAllEqualCombo implements ComboCheckStrategy {

    private static final int WILD_ALL_EQUAL_UNIT_REWARD = 12;

    @Override
    public boolean comboIsValid(final Set<GameCard> cards) {
        if (cards == null || cards.size() != 3) {
            throw new IllegalArgumentException("The hand must contain 3 cards.");
        }
        // One WILD and two cards of different UnitTypes.
        return countWildUnitTypes(cards) == 1 && countNonWildUnitTypes(cards) == 2;
    }

    @Override
    public boolean comboIsPossibile(final PlayerHand hand) {
        if (Objects.isNull(hand) || Objects.isNull(hand.getCards())) {
            throw new IllegalArgumentException("The hand must not be null.");
        }

        // One WILD and two cards of different UnitTypes.
        return countWildUnitTypes(hand.getCards()) >= 1 && countNonWildUnitTypes(hand.getCards()) >= 2;
    }

    @Override
    public int getUnitRewardAmount() {
        return WILD_ALL_EQUAL_UNIT_REWARD;
    }

    private long countWildUnitTypes(final Set<GameCard> cards) {
        return cards.stream()
                .map(GameCard::getType)
                .filter(t -> t.equals(UnitType.WILD))
                .distinct()
                .count();
    }

    private long countNonWildUnitTypes(final Set<GameCard> cards) {
        return cards.stream()
                .map(GameCard::getType)
                .filter(t -> !t.equals(UnitType.WILD))
                .distinct()
                .count();
    }
}
