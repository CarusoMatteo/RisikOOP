package it.unibo.risikoop.model.implementations.gamecards.combos;

import it.unibo.risikoop.model.interfaces.cards.UnitType;

/**
 * Combo that checks for a three of a kind of Knight game cards.
 */
public final class AllKnightEqualCombo extends AllEqualCombo {

    private static final int KNIGHT_UNIT_REWARD = 8;

    @Override
    public int getUnitRewardAmount() {
        return KNIGHT_UNIT_REWARD;
    }

    @Override
    protected UnitType getUnitType() {
        return UnitType.KNIGHT;
    }
}
