package it.unibo.risikoop.model.implementations.gamecards.combos;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import it.unibo.risikoop.model.interfaces.PlayerHand;
import it.unibo.risikoop.model.interfaces.cards.ComboCheckStrategy;
import it.unibo.risikoop.model.interfaces.cards.ComboChecker;
import it.unibo.risikoop.model.interfaces.cards.GameCard;

/**
 * This class allows to check if a combo is usable and to use it.
 */
public final class ComboCheckerImpl implements ComboChecker {

    private final List<ComboCheckStrategy> strategies = List.of(
            new WildAllEqualCombo(),
            new AllDifferentCombo(),
            new AllCavarlyEqualCombo(),
            new AllInfantryEqualCombo(),
            new AllArtilleryEqualCombo());

    @Override
    public boolean anyComboIsPossible(final PlayerHand hand) {
        return strategies.stream()
                .anyMatch(strategy -> strategy.comboIsPossibile(hand));
    }

    @Override
    public Optional<Integer> useCombo(final Set<GameCard> cards) {
        return strategies.stream()
                .filter(strategy -> strategy.comboIsValid(cards))
                .map(ComboCheckStrategy::getUnitRewardAmount).findFirst();
    }
}
