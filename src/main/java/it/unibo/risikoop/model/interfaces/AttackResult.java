package it.unibo.risikoop.model.interfaces;

import java.util.List;

/**
 * The interfaces for the results of the attackes.
 */
public interface AttackResult {

    /**
     * get the attacker dice rolls.
     * 
     * @return the number of attacker losses.
     */
    List<Integer> getAttackerDiceRolls();

    /**
     * get the defender dice rolls.
     * 
     * @return the number of defender losses.
     */
    List<Integer> getDefenderDiceRolls();
}
