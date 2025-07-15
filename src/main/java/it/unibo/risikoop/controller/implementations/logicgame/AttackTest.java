package it.unibo.risikoop.controller.implementations.logicgame;

import it.unibo.risikoop.controller.interfaces.logicgame.LogicAttack;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;

public final class AttackTest implements LogicAttack {

    public enum PlayerType {
        ATTACKER,
        DEFENDER
    }

    private PlayerType playerType;

    @Override
    public boolean attack(Player attacker, Player defender, Territory src, Territory dst, int units) {

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

    public void selectWin(PlayerType type) {
        playerType = type;
    }

    private boolean checkConditionAttack(Territory src, int units) {
        return units > 1 && units < src.getUnits();
    }

    private void defenderWin(Territory src, int units) {
        src.removeUnits(units);
    }

    private void attackerWin(Player attacker, Player defender, Territory src, Territory dst, int units) {

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
