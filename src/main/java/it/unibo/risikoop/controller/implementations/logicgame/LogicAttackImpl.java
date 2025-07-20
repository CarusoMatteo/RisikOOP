package it.unibo.risikoop.controller.implementations.logicgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.risikoop.controller.interfaces.logicgame.LogicAttack;
import it.unibo.risikoop.model.implementations.AttackResultImpl;
import it.unibo.risikoop.model.interfaces.AttackResult;
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
public final class LogicAttackImpl implements LogicAttack {

    private static final int DICE_FACE = 6;
    private static final int MAX_DICE_USE = 3;
    private final Random rand = new Random();
    private Territory src;
    private Territory dst;
    private Optional<List<Integer>> attackerDice;
    private Optional<List<Integer>> defenderDice;
    private boolean isFastAttackEnabled;
    private Optional<AttackResult> attackResult;

    /**
     * Constructs a new {@code LogicAttackImpl} instance with no preconfigured
     * territories or unit counts. Attack and defense state will be initialized
     * at the start of each
     * {@link #attack(Player, Player, Territory, Territory, int)} call.
     */
    public LogicAttackImpl() {
        this.src = null;
        this.dst = null;
        attackerDice = Optional.empty();
        defenderDice = Optional.empty();
        attackResult = Optional.empty();
        isFastAttackEnabled = false;
    }

    @Override
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "We intentionally store the Territory reference; game logic needs mutable state.")
    public boolean attack(final Player attacker, final Player defender, final Territory src, final Territory dst,
            final int units) {

        this.src = src;
        this.dst = dst;

        int attackerUnits = units;
        final int defenderUnits = dst.getUnits();

        do {
            // Simula il lancio dei dadi
            final List<Integer> attackerDice = this.attackerDice.isEmpty()
                    ? rollDice(Math.min(MAX_DICE_USE, attackerUnits))
                    : this.attackerDice.get();

            final List<Integer> defenderDice = this.defenderDice.isEmpty()
                    ? rollDice(Math.min(MAX_DICE_USE, defenderUnits))
                    : this.defenderDice.get();

            final int attackerLosses = compareDiceRolls(attackerDice, defenderDice);

            attackResult = Optional.of(new AttackResultImpl(attackerDice, defenderDice));

            // Se il difensore perde tutte le unità, l'attaccante conquista il territorio
            if (dst.getUnits() == 0) {
                dst.setOwner(attacker);
                dst.addUnits(attackerUnits - attackerLosses);
                src.removeUnits(attackerUnits - attackerLosses);
                attacker.addTerritory(dst);
                defender.removeTerritory(dst);
                checkKillPlayer(defender, attacker);
                return attackEnd(true);
            }

            // se l'attancante finisce tutte le truppe con cui attacare l'attacco e concluso
            // e ha vinto il difensore
            attackerUnits = attackerUnits - attackerLosses;
            if (attackerUnits == 0) {
                return attackEnd(false);
            }
        } while (isFastAttackEnabled);

        return attackEnd(false);
    }

    @Override
    public void enableFastAttack() {
        this.isFastAttackEnabled = true;
    }

    @Override
    public Optional<AttackResult> showAttackResults() {
        Optional<AttackResult> returned = Optional.empty();
        if (attackResult.isPresent()) {
            returned = Optional.of(attackResult.get());
            attackResult = Optional.empty();
        }
        return returned;
    }

    public void setAttackerDice(List<Integer> dice) {
        this.attackerDice = Optional.ofNullable(dice);
    }

    public void setDefencerDice(List<Integer> dice) {
        this.defenderDice = Optional.ofNullable(dice);
    }

    private List<Integer> rollDice(final int n) {
        final List<Integer> dice = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            dice.add(rand.nextInt(DICE_FACE) + 1);
        }
        dice.sort(Collections.reverseOrder());
        return dice;
    }

    private int compareDiceRolls(final List<Integer> attackerDice, final List<Integer> defenderDice) {
        final int battles = Math.min(attackerDice.size(), defenderDice.size());
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

    private boolean attackEnd(boolean attackRes) {
        isFastAttackEnabled = false;
        this.attackerDice = Optional.empty();
        this.defenderDice = Optional.empty();
        return attackRes;
    }

    private void checkKillPlayer(Player p, Player killer) {
        if (p.getTerritories().isEmpty()) {
            p.setKiller(killer);
        }
    }
}
