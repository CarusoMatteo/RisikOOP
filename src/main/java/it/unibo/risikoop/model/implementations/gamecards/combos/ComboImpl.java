package it.unibo.risikoop.model.implementations.gamecards.combos;

import java.util.List;
import java.util.Objects;

import it.unibo.risikoop.model.interfaces.Combo;
import it.unibo.risikoop.model.interfaces.cards.UnitType;

public class ComboImpl implements Combo {

    private final List<UnitType> unitTypes;
    private final int unitAward;

    public ComboImpl(List<UnitType> unitTypes, int unitAward) {
        this.unitTypes = Objects.requireNonNull(unitTypes, "unitTypes must not be null");
        if (unitTypes.isEmpty()) {
            throw new IllegalArgumentException("unitTypes must not be empty");
        }
        if (unitAward <= 0) {
            throw new IllegalArgumentException("unitAward must be greater than 0");
        }
        this.unitAward = unitAward;
    }

    @Override
    public List<UnitType> getUnitTypes() {
        return List.copyOf(unitTypes);
    }

    @Override
    public int getUnitAward() {
        return unitAward;
    }

}
