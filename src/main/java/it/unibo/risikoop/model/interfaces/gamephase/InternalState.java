package it.unibo.risikoop.model.interfaces.gamephase;

/**
 * an enum for the internal states of any transaction phase.
 */
public enum InternalState {
    /**
     * it describe when you have to select the transaction source territory.
     */
    SELECT_SRC,
    /**
     * it describe when you have to select the transaction destinantion territory.
     */
    SELECT_DST,
    /**
     * it describe when you have to select the transaction' units involved.
     */
    SELECT_UNITS_QUANTITY,
    /**
     * it describe when you have to execute the transaction.
     */
    EXECUTE
}
