package it.unibo.risikoop.model.interfaces.gamephase;

public interface PhaseWithTransaction {

    void nextState();

    InternalState getInternalState();
}
