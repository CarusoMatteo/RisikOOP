package it.unibo.risikoop.controller.implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import it.unibo.risikoop.controller.interfaces.LogicAttack;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;

/**
 * Concrete implementation of the {@link LogicAttack} interface,
 * providing Risiko-style dice attack resolution between two territories.
 * <p>
 * Uses up to three attack dice and up to two defense dice per battle round,
 * sorts the dice in descending order, compares each pair (defender wins ties),
 * applies unit losses, and repeats until the attacker cannot continue or the
 * defender’s territory is conquered.
 * </p>
 */
public class LogicAttackImpl implements LogicAttack {

    private final static int DICE_FACE = 6;
    private final static int MAX_DICE_USE = 3;
    private int attackerUnits;
    private int defenderUnits;
    private final Random rand = new Random();
    private Territory src;
    private Territory dst;

    /**
     * Constructs a new {@code LogicAttackImpl} instance with no preconfigured
     * territories or unit counts. Attack and defense state will be initialized
     * at the start of each {@link #attack(Player, Player, Territory, Territory, int)} call.
     */
    public LogicAttackImpl() {
        this.attackerUnits = 0;
        this.defenderUnits = 0;
        this.src = null;
        this.dst = null;
    }

    @Override
    public boolean attack(Player attacker, Player defender, Territory src, Territory dst, int units) {
        if (src.getOwner() != attacker || dst.getOwner() != defender) {
            return false;
        }
        if (units < 1 || units >= src.getUnits()) {
            return false;
        }

        attackerUnits = units;
        defenderUnits = dst.getUnits();
        this.src = src;
        this.dst = dst;

        // Simula il lancio dei dadi
        List<Integer> attackerDice = rollDice(Math.min(MAX_DICE_USE, attackerUnits));
        List<Integer> defenderDice = rollDice(defenderUnits);

        int attackerLosses = compareDiceRolls(attackerDice, defenderDice);

        // Se il difensore perde tutte le unità, l'attaccante conquista il territorio
        if (dst.getUnits() == 0) {
            dst.setOwner(attacker);
            dst.addUnits(attackerUnits - attackerLosses);
            src.removeUnits(attackerUnits - attackerLosses);
            attacker.addTerritory(dst);
            defender.removeTerritory(dst);
            return true;
        }

        return false;
    }

    private List<Integer> rollDice(int n) {
        List<Integer> dice = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            dice.add(rand.nextInt(DICE_FACE) + 1);
        }
        dice.sort(Collections.reverseOrder());
        return dice;
    }

    private int compareDiceRolls(List<Integer> attackerDice, List<Integer> defenderDice) {
        int battles = Math.min(attackerDice.size(), defenderDice.size());
        int attackerLosses = 0;
        int defenderLosses = 0;

        for (int i = 0; i < battles; i++) {
            if (attackerDice.get(i) > defenderDice.get(i)) {
                defenderLosses++;
            } else {
                attackerLosses++;
            }
        }

        // Update terrotory units
        src.removeUnits(attackerLosses);
        dst.removeUnits(defenderLosses);

        return attackerLosses;
    }
}
