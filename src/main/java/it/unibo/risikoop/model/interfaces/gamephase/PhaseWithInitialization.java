package it.unibo.risikoop.model.interfaces.gamephase;

/**
 * Interface for game phases that require an initialization step.
 * This interface is used to define phases that need to perform some setup
 * before the main actions of the phase can take place.
 */
public interface PhaseWithInitialization {

    /**
     * Initializes the phase by setting all the relevant data.
     * This method should be called before any actions are performed in the phase.
     */
    void initializationPhase();
} 
