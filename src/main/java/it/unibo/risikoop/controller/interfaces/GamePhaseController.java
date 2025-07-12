package it.unibo.risikoop.controller.interfaces;

import it.unibo.risikoop.model.interfaces.Territory;

public interface GamePhaseController {

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

    void nextPhase();

    /**
     * Set the number of units to use in the current action.
     * 
     * @param units the number of units to use
     */
    void setUnitsToUse(int units);
}
