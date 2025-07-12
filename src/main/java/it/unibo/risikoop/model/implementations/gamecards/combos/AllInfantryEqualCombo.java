package it.unibo.risikoop.model.implementations.gamecards.combos;

import it.unibo.risikoop.model.interfaces.cards.UnitType;

/**
 * Combo that checks for a three of a kind of Infantry game cards.
 */
public final class AllInfantryEqualCombo extends AllEqualCombo {

    private static final int INFANTRY_UNIT_REWARD = 6;

    @Override
    public int getUnitRewardAmount() {
        return INFANTRY_UNIT_REWARD;
    }

    @Override
    protected UnitType getUnitType() {
        return UnitType.INFANTRY;
    }
}
