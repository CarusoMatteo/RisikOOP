package it.unibo.risikoop.model.implementations.gamecards.combos;

import it.unibo.risikoop.model.interfaces.cards.UnitType;

/**
 * Combo that checks for a three of a kind of Jack game cards.
 */
public final class AllJackEqualCombo extends AllEqualCombo {

    private static final int JACK_UNIT_REWARD = 6;

    @Override
    public int getUnitRewardAmount() {
        return JACK_UNIT_REWARD;
    }

    @Override
    protected UnitType getUnitType() {
        return UnitType.JACK;
    }
}
