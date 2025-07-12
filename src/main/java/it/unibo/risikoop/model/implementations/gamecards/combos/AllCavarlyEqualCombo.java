package it.unibo.risikoop.model.implementations.gamecards.combos;

import it.unibo.risikoop.model.interfaces.cards.UnitType;

/**
 * Combo that checks for a three of a kind of Cavarly game cards.
 */
public final class AllCavarlyEqualCombo extends AllEqualCombo {

    private static final int CAVALRY_UNIT_REWARD = 8;

    @Override
    public int getUnitRewardAmount() {
        return CAVALRY_UNIT_REWARD;
    }

    @Override
    protected UnitType getUnitType() {
        return UnitType.CAVALRY;
    }
}
