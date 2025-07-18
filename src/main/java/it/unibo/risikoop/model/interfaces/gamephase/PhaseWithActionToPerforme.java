package it.unibo.risikoop.model.interfaces.gamephase;

/**
 * Interface for game phases that require an action to be performed.
 * This interface is used to define the action that needs to be executed
 * during the game phase, such as selecting an attack or fortifying a territory.
 */
public interface PhaseWithActionToPerforme {

    /**
     * Rappresent an acction to be performed in that phase,
     * e.g., selecting attacking, fortifying, etc.
     */
    void performAction();
}
