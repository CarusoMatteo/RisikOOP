package it.unibo.risikoop.model.interfaces;

/**
 * Interface representing a game phase in the Risiko game.
 * Each phase allows players to interact with the game state,
 * such as selecting territories, confirming actions, and canceling selections.
 */
public interface GamePhase {

    /**
     * Select a territory to perform an action on.
     * 
     * @param t the Territory to select
     */
    void selectTerritory(Territory t);

    /**
     * Rappresent an acction to be performed in that phase,
     * e.g., selecting attacking, fortifying, etc.
     */
    void performAction();

    /**
     * Check if phase is complete.
     * 
     * @return true if the phase is complete, false otherwise
     */
    boolean isComplete();

    /**
     * Set the number of units to use in the current action.
     * 
     * @param units the number of units to use
     */
    void setUnitsToUse(int units);
}