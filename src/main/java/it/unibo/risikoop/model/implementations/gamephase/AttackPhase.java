
package it.unibo.risikoop.model.implementations.gamephase;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.risikoop.controller.implementations.logicgame.LogicAttackImpl;
import it.unibo.risikoop.controller.interfaces.GamePhaseController;
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
public final class AttackPhase implements GamePhase {

    private enum PhaseState {
        SELECT_ATTACKER,
        SELECT_DEFENDER,
        SELECT_UNITS,
        EXECUTE_ATTACK
    }

    private final TurnManager turnManager;
    private final LogicAttack logic;
    private PhaseState state;
    private final Player attacker;
    private Player defender;
    private Territory attackerSrc;
    private Territory defenderDst;
    private int unitsToUse;
    private boolean isEnd;
    private final GamePhaseController GamePhaseController;

    /**
     * Constructs a new AttackPhase associated with the given turn manager.
     *
     * <p>
     * This initializes the internal attack logic, sets the phase state to
     * {@code SELECT_ATTACKER}, captures the current player from the turn manager
     * as the attacker, and resets all other fields (no source or destination
     * territory selected, zero units to use). The phase is marked as complete
     * until an attacker territory is chosen.
     * </p>
     *
     * @param gpc the {@link GamePhaseController}
     */

    public AttackPhase(final GamePhaseController gpc) {
        this.GamePhaseController = gpc;
        this.turnManager = gpc.getTurnManager();
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
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "We intentionally store the Territory reference; game logic needs mutable state.")
    public void selectTerritory(final Territory t) {
        if (state == PhaseState.SELECT_ATTACKER && isValidAttacker(t)) {
            this.attackerSrc = t;
            unitsToUse = 0;
        } else if (state == PhaseState.SELECT_DEFENDER && isValidDefender(t)) {
            this.defender = t.getOwner();
            this.defenderDst = t;
        }
    }

    @Override
    public void setUnitsToUse(final int units) {
        if (units > 0 && units <= attackerSrc.getUnits() - 1) {
            unitsToUse = units;
        }
    }

    @Override
    public void initializationPhase() {
    }

    private boolean isValidAttacker(final Territory t) {
        final boolean hasEnemyNeighbor = t.getNeightbours().stream()
                .anyMatch(neighbour -> !neighbour.getOwner().equals(turnManager.getCurrentPlayer()));
        final boolean hasEnoughUnits = t.getUnits() >= 2;
        return hasEnemyNeighbor && hasEnoughUnits;
    }

    private boolean isValidDefender(final Territory t) {
        return !t.getOwner().equals(turnManager.getCurrentPlayer()) && attackerSrc.getNeightbours().contains(t);
    }
}
