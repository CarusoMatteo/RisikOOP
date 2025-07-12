package it.unibo.risikoop.model.implementations.gamecards.combos;

import it.unibo.risikoop.model.interfaces.cards.UnitType;

/**
 * Combo that checks for a three of a kind of Cannon game cards.
 */
public final class AllCannonEqualCombo extends AllEqualCombo {

    private static final int CANNON_UNIT_REWARD = 4;

    @Override
    public int getUnitRewardAmount() {
        return CANNON_UNIT_REWARD;
    }

    @Override
    protected UnitType getUnitType() {
        return UnitType.CANNON;
    }
}
