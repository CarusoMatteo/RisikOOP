package it.unibo.risikoop.model.interfaces.gamephase;

/**
 * phase used when you want to perform a transaction.
 */
public interface PhaseWithTransaction {
    /**
     * go to the next subState.
     */
    void nextState();

    /**
     * get the sub state you are currently in.
     * 
     * @return the {@link InternalState} you are currently at
     */
    InternalState getInternalState();
}
