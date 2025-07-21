package it.unibo.risikoop.model.interfaces.gamephase;

/**
 * Interface for game phases that can set the number of units to use.
 * This interface is used to manage the number of units available for actions
 * during the game phase.
 */
public interface PhaseWithUnits {
    /**
     * Sets the number of units to use in the current phase.
     *
     * @param units the number of units to use
     */
    void setUnitsToUse(int units);
}
