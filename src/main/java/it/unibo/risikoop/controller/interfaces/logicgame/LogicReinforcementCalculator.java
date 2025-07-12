package it.unibo.risikoop.controller.interfaces.logicgame;

/**
 * Defines the contract for computing the number of reinforcement units
 * a player is entitled to at the start of the reinforcement phase.
 */
public interface LogicReinforcementCalculator {

    /**
     * Calculates how many units the current player should receive
     * during the reinforcement phase, based on game rules
     * (e.g., number of territories owned, continent bonuses, card exchanges).
     *
     * @return the number of reinforcement units awarded to the player
     */
    int calcPlayerUnits();
}
