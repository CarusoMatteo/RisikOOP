package it.unibo.risikoop.model.interfaces.gamephase;

import java.util.Optional;

import it.unibo.risikoop.model.interfaces.AttackResult;

/**
 * Interface for game phases that can show the results of an attack.
 * This interface is used to retrieve the results of an attack performed during
 * the game phase.
 */
public interface PhaseWithAttack {
    /**
     * Show the results of the attack.
     * 
     * @return the attack result
     */
    Optional<AttackResult> showAttackResults();

    /**
     * Enables fast attack mode, allowing the player to perform multiple attacks
     * in quick succession without waiting for confirmation after each attack.
     */
    void enableFastAttack();
}
