package it.unibo.risikoop.model.implementations;

import java.util.List;

import it.unibo.risikoop.model.interfaces.AttackResult;

/**
 * Implementation of the AttackResult interface, representing the result of an
 * attack in the game.
 * It contains the dice rolls for both the attacker and defender.
 */
public class AttackResultImpl implements AttackResult {

    private final List<Integer> attackerDiceRolls;
    private final List<Integer> defenderDiceRolls;

    public AttackResultImpl(List<Integer> attackerDiceRolls, List<Integer> defenderDiceRolls) {
        this.attackerDiceRolls = attackerDiceRolls;
        this.defenderDiceRolls = defenderDiceRolls;
    }

    @Override
    public List<Integer> getAttackerDiceRolls() {
        return attackerDiceRolls;
    }

    @Override
    public List<Integer> getDefenderDiceRolls() {
        return defenderDiceRolls;
    }

    @Override
    public String toString() {
        return "attackerDiceRolls=" + diceRollsToString(attackerDiceRolls) +
                "\ndefenderDiceRolls=" + diceRollsToString(defenderDiceRolls);
    }

    private String diceRollsToString(List<Integer> diceRolls) {
        return diceRolls.stream()
                .map(String::valueOf)
                .collect(java.util.stream.Collectors.joining(","));
    }
}
