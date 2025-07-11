package it.unibo.risikoop.model.implementations.gamecards.combos;

import it.unibo.risikoop.model.interfaces.cards.UnitType;

/**
 * Combo that checks for a three of a kind of Artillery game cards.
 */
public final class AllArtilleryEqualCombo extends AllEqualCombo {

    private static final int ARTILLERY_UNIT_REWARD = 8;

    @Override
    public int getUnitRewardAmount() {
        return ARTILLERY_UNIT_REWARD;
    }

    @Override
    protected UnitType getUnitType() {
        return UnitType.ARTILLERY;
    }
}
