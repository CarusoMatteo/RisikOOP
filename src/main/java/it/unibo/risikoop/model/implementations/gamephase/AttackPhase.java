
package it.unibo.risikoop.model.implementations.gamephase;

import it.unibo.risikoop.controller.implementations.logicgame.LogicAttackImpl;
import it.unibo.risikoop.controller.interfaces.logicgame.LogicAttack;
import it.unibo.risikoop.model.interfaces.GamePhase;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.TurnManager;

/**
 * Represents the attack phase in the game, managing the sequence of actions
 * required to perform an attack.
 * The phase progresses through selecting the attacking territory, the defending
 * territory, the number of units to use,
 * and finally executing the attack. The class ensures that only valid
 * territories and unit counts are selected,
 * and coordinates the attack logic in conjunction with the current turn
 * manager.
 */
public class AttackPhase implements GamePhase {

    private enum PhaseState {
        SELECT_ATTACKER,
        SELECT_DEFENDER,
        SELECT_UNITS,
        EXECUTE_ATTACK
    }

    private final TurnManager turnManager;
    private final LogicAttack logic;
    private PhaseState state;
    private Player attacker;
    private Player defender;
    private Territory attackerSrc;
    private Territory defenderDst;
    private int unitsToUse;
    private boolean isEnd;

    public AttackPhase(TurnManager turnManager) {
        this.turnManager = turnManager;
        this.logic = new LogicAttackImpl();
        this.state = PhaseState.SELECT_ATTACKER;
        this.attacker = turnManager.getCurrentPlayer();
        this.attackerSrc = null;
        this.defenderDst = null;
        this.unitsToUse = 0;
        this.isEnd = true;
    }

    @Override
    public boolean isComplete() {
        return isEnd;
    }

    @Override
    public void performAction() {
        if (state == PhaseState.SELECT_ATTACKER && attackerSrc != null) {
            isEnd = false;
            state = PhaseState.SELECT_DEFENDER;
        } else if (state == PhaseState.SELECT_DEFENDER && defenderDst != null) {
            state = PhaseState.SELECT_UNITS;
        } else if (state == PhaseState.SELECT_UNITS && unitsToUse > 0) {
            state = PhaseState.EXECUTE_ATTACK;
        } else if (state == PhaseState.EXECUTE_ATTACK) {
            logic.attack(attacker, defender, attackerSrc, defenderDst, unitsToUse);
            isEnd = true; // Mark that an attack has been executed
            state = PhaseState.SELECT_ATTACKER; // Reset to allow for another attack
        }
    }

    @Override
    public void selectTerritory(Territory t) {
        if (state == PhaseState.SELECT_ATTACKER && isValidAttacker(t)) {
            this.attackerSrc = t;
            unitsToUse = 0;
        } else if (state == PhaseState.SELECT_DEFENDER && isValidDefender(t)) {
            this.defender = t.getOwner();
            this.defenderDst = t;
        }
    }

    @Override
    public void setUnitsToUse(int units) {
        if (units > 0 && units <= attackerSrc.getUnits() - 1) {
            unitsToUse = units;
        }
    }

    @Override
    public void initializationPhase() {
    }

    private boolean isValidAttacker(Territory t) {
        boolean hasEnemyNeighbor = t.getNeightbours().stream()
                .anyMatch(neighbour -> !neighbour.getOwner().equals(turnManager.getCurrentPlayer()));
        boolean hasEnoughUnits = t.getUnits() >= 2;
        return hasEnemyNeighbor && hasEnoughUnits;
    }

    private boolean isValidDefender(Territory t) {
        return !t.getOwner().equals(turnManager.getCurrentPlayer()) &&
                attackerSrc.getNeightbours().contains(t);
    }
}
