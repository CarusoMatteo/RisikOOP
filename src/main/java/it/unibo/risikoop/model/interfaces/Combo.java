package it.unibo.risikoop.model.interfaces;

import java.util.List;

import it.unibo.risikoop.model.interfaces.cards.UnitType;

/**
 * Interface representing a combo in the game.
 * A combo consists of a list of unit types and an award for completing the
 * combo.
 */
public interface Combo {

    /**
     * Returns the list of unit types required for this combo.
     *
     * @return a list of UnitType representing the required units for the combo
     */
    List<UnitType> getUnitTypes();

    /**
     * Returns the award for completing this combo.
     * The award is typically a number of units that can be added to the player's
     * army.
     *
     * @return an integer representing the unit award for completing the combo
     */
    int getUnitAward();
}