package it.unibo.risikoop.model.implementations.gamecards.combos;

import it.unibo.risikoop.model.interfaces.cards.UnitType;

public class InfantryAllEqualCombo extends AllEqualCombo {

    private static final int INFANTRY_UNIT_REWARD = 6;

    @Override
    protected UnitType getUnitType() {
        return UnitType.INFANTRY;
    }

    @Override
    protected int getUnitRewardAmount() {
        return INFANTRY_UNIT_REWARD;
    }
}
