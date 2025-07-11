package it.unibo.risikoop.model.implementations.gamecards.combos;

import it.unibo.risikoop.model.interfaces.cards.UnitType;

public class CavarlyAllEqualCombo extends AllEqualCombo {

    private static final int CAVALRY_UNIT_REWARD = 8;

    @Override
    protected UnitType getUnitType() {
        return UnitType.CAVALRY;
    }

    @Override
    protected int getUnitRewardAmount() {
        return CAVALRY_UNIT_REWARD;
    }
}
