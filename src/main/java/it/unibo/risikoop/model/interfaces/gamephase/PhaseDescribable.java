package it.unibo.risikoop.model.interfaces.gamephase;

/**
 * Interface for game phases that can provide a description of their current state.
 * This interface is used to retrieve a string representation of the phase's inner state,
 * which can be useful for debugging or displaying information to the user.
 */
public interface PhaseDescribable {
    /**
     * @return a description of the current phase
     */
    String getInnerStatePhaseDescription();
}
