package it.unibo.risikoop.model.implementations.gamecards.combos;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import it.unibo.risikoop.model.interfaces.cards.ComboCheckStrategy;
import it.unibo.risikoop.model.interfaces.cards.ComboChecker;
import it.unibo.risikoop.model.interfaces.cards.GameCard;

/**
 * This class allows to check if a combo is usable and validate it to get how
 * many rewards units its gives.
 */
public final class ComboCheckerImpl implements ComboChecker {

    private final List<ComboCheckStrategy> strategies = List.of(
            new WildAllEqualCombo(),
            new AllDifferentCombo(),
            new AllKnightEqualCombo(),
            new AllJackEqualCombo(),
            new AllCannonEqualCombo());

    @Override
    public Optional<Integer> useCombo(final Set<GameCard> cards) {
        if (cards == null || cards.size() != 3) {
            throw new IllegalArgumentException("The hand must contain 3 cards.");
        }

        return strategies.stream()
                .filter(strategy -> strategy.comboIsValid(cards))
                .map(ComboCheckStrategy::getUnitRewardAmount).findFirst();
    }
}
