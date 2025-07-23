package it.unibo.risikoop.model.implementations.gamecards.combos;

import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

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
        // One WILD and two cards of equal UnitTypes.
        return countWildUnitTypes(cards) == 1 && getMostFrequentNonWildUnitTypeFrequency(cards) == 2;
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

    /**
     * Returns the frequency of the most frequent non-WILD UnitType in the set.
     * 
     * @param cards
     * @return the frequency of the most frequent non-WILD UnitType.
     */
    private Long getMostFrequentNonWildUnitTypeFrequency(final Set<GameCard> cards) {
        return cards.stream()
                .map(GameCard::getType)
                .filter(t -> !t.equals(UnitType.WILD))
                .collect(Collectors.groupingBy(t -> t, Collectors.counting()))
                .entrySet().stream()
                .max(Entry.comparingByValue())
                .map(Entry::getValue)
                .orElse(0L);
    }
}
