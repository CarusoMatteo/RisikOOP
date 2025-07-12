package it.unibo.risikoop.controller.interfaces;

import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;

/**
 * Defines the core attack logic for resolving combat between two territories.
 * Implementations should encapsulate the dice rolls, unit losses, and territory
 * conquest rules of the game.
 */
public interface LogicAttack {

    /**
     * Executes an attack from one territory to another, resolving combat
     * according to game rules.
     *
     * @param attacker the player initiating the attack
     * @param defender the player whose territory is being attacked
     * @param src      the source territory from which units are attacking; must be owned by {@code attacker}
     * @param dst      the destination territory being attacked; must be owned by {@code defender} and adjacent to {@code src}
     * @return {@code true} if the attack results in the attacker conquering the {@code dst} territory;
     *         {@code false} if the attack fails and the defender retains control
     * @throws IllegalArgumentException if any precondition is violated (e.g., insufficient units in {@code src},
     *         non‐adjacent territories, or mismatched ownership)
     */
    boolean attack(Player attacker, Player defender, Territory src, Territory dst, int units);
    
} 

