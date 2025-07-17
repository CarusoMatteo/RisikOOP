package it.unibo.risikoop.model.interfaces;

import java.util.List;

public interface AttackResult {

    /**
     * Returns the number of attacker losses.
     */
    List<Integer> getAttackerDiceRolls();
    
    /**
     * Returns the number of defender losses.
     */
    List<Integer> getDefenderDiceRolls();
} 