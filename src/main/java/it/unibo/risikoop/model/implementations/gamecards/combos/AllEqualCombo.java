package it.unibo.risikoop.model.implementations.gamecards.combos;

import java.util.Objects;
import java.util.Set;

import it.unibo.risikoop.model.interfaces.PlayerHand;
import it.unibo.risikoop.model.interfaces.cards.ComboCheckStrategy;
import it.unibo.risikoop.model.interfaces.cards.GameCard;
import it.unibo.risikoop.model.interfaces.cards.UnitType;

/**
 * Combo that checks for a three of a kind game cards.
 * This class is extended for each specific UnitType.
 */
public abstract class AllEqualCombo implements ComboCheckStrategy {

    /**
     * This is a Templete Method for the Template Method pattern.
     * 
     * @inheritDoc
     */
    @Override
    public final Boolean comboIsValid(final Set<GameCard> cards) {
        if (Objects.isNull(cards) || cards.size() != 3) {
            throw new IllegalArgumentException("The hand must contain 3 cards.");
        }

        // Check if the hand contains only <getUnitType> cards.

        return countEqualUnitTypes(cards) == 3;
    }

    /**
     * This is a Templete Method for the Template Method pattern.
     * 
     * @inheritDoc
     */
    @Override
    public final Boolean comboIsPossibile(final PlayerHand hand) {
        if (Objects.isNull(hand) || Objects.isNull(hand.getCards())) {
            throw new IllegalArgumentException("The hand must not be null.");
        }

        // Check if the hand contains at least 3 <UnitType> cards.
        return countEqualUnitTypes(hand.getCards()) >= 3;
    }

    @Override
    public abstract int getUnitRewardAmount();

    /**
     * Returns the UnitType that this combo checks for.
     * This is the primitive operation for the Template Method pattern.
     * 
     * @return the UnitType to check for.
     */
    protected abstract UnitType getUnitType();

    private long countEqualUnitTypes(final Set<GameCard> cards) {
        return cards.stream()
                .filter(card -> card.getType() == getUnitType())
                .count();
    }
}
