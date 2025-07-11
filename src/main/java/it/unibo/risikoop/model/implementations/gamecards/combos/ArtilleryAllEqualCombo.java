package it.unibo.risikoop.model.implementations.gamecards.combos;

import it.unibo.risikoop.model.interfaces.cards.UnitType;

public class ArtilleryAllEqualCombo extends AllEqualCombo {

    private static final int ARTILLERY_UNIT_REWARD = 8;

    @Override
    protected UnitType getUnitType() {
        return UnitType.ARTILLERY;
    }

    @Override
    protected int getUnitRewardAmount() {
        return ARTILLERY_UNIT_REWARD;
    }
}
