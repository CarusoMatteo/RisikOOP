package it.unibo.risikoop.controller.implementations.logicgame;

import java.util.Optional;

import it.unibo.risikoop.controller.interfaces.logicgame.LogicAttack;
import it.unibo.risikoop.model.interfaces.AttackResult;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;

/**
 * 
 */
public final class AttackTest implements LogicAttack {
    /**
     * enum for the states of the player.
     */
    public enum PlayerType {
        /**
         * enum for the attacker states of the player.
         */
        ATTACKER,
        /**
         * enum for the defender state of the player.
         */
        DEFENDER
    }

    private PlayerType playerType;

    @Override
    public boolean attack(final Player attacker, final Player defender, final Territory src, final Territory dst,
            final int units) {

        if (!checkConditionAttack(src, units)) {
            return false;
        }

        if (playerType == PlayerType.ATTACKER) {
            attackerWin(attacker, defender, src, dst, units);
            return true;
        } else {
            defenderWin(src, units);
            return false;
        }
    }

    @Override
    public void enableFastAttack() {
    }

    @Override
    public Optional<AttackResult> showAttackResults() {
        return Optional.empty();
    }

    /**
     * 
     * @param type
     */
    public void selectWin(final PlayerType type) {
        playerType = type;
    }

    private boolean checkConditionAttack(final Territory src, final int units) {
        return units > 1 && units < src.getUnits();
    }

    private void defenderWin(final Territory src, final int units) {
        src.removeUnits(units);
    }

    private void attackerWin(final Player attacker, final Player defender, final Territory src, final Territory dst,
            final int units) {

        // rimuvo il territorio dal difensore e lo do all'attancante
        dst.removeUnits(dst.getUnits());
        dst.addUnits(units);
        dst.setOwner(attacker);

        defender.removeTerritory(dst);
        attacker.addTerritory(dst);

        // tolgo le truppe mosse dall'attacante
        src.removeUnits(units);
    }

}
