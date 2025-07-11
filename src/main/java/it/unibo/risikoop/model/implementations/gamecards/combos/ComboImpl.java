package it.unibo.risikoop.model.implementations.gamecards.combos;

import java.util.List;
import java.util.Objects;

import it.unibo.risikoop.model.interfaces.Combo;
import it.unibo.risikoop.model.interfaces.cards.UnitType;

/**
 * Implementation of the Combo interface representing a combination of unit types
 * and the number of units awarded for that combination.
 */
public class ComboImpl implements Combo {

    private final List<UnitType> unitTypes;
    private final int unitAward;

    /**
     * Constructs a ComboImpl with the specified unit types and unit award.
     *
     * @param unitTypes  the list of unit types in the combo, must not be null or empty
     * @param unitAward  the number of units awarded for this combo, must be greater than 0
     * @throws IllegalArgumentException if unitTypes is null, empty, or if unitAward is less than or equal to 0
     */
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
