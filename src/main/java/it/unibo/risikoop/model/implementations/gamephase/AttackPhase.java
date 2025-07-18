
package it.unibo.risikoop.model.implementations.gamephase;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.risikoop.controller.implementations.CardGameControllerImpl;
import it.unibo.risikoop.controller.implementations.logicgame.LogicAttackImpl;
import it.unibo.risikoop.controller.interfaces.GamePhaseController;
import it.unibo.risikoop.controller.interfaces.logicgame.LogicAttack;
import it.unibo.risikoop.model.interfaces.AttackResult;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.TurnManager;
import it.unibo.risikoop.model.interfaces.gamephase.GamePhase;
import it.unibo.risikoop.model.interfaces.gamephase.InternalState;
import it.unibo.risikoop.model.interfaces.gamephase.PhaseDescribable;
import it.unibo.risikoop.model.interfaces.gamephase.PhaseWithActionToPerforme;
import it.unibo.risikoop.model.interfaces.gamephase.PhaseWithAttack;
import it.unibo.risikoop.model.interfaces.gamephase.PhaseWithTransaction;
import it.unibo.risikoop.model.interfaces.gamephase.PhaseWithUnits;

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
public final class AttackPhase
        implements GamePhase, PhaseDescribable, PhaseWithUnits, PhaseWithActionToPerforme, PhaseWithAttack,
        PhaseWithTransaction {

    private final TurnManager turnManager;
    private final LogicAttack logic;
    private Player attacker;
    private Player defender;
    private Optional<Territory> attackerSrc;
    private Optional<Territory> defenderDst;
    private int unitsToUse;
    private boolean isEnd;
    private final GamePhaseController gamePhaseController;
    private InternalState internalState;
    private final GameManager gameManager;

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
     * @param gm  the {@link GameManager}
     * @param gpc the {@link GamePhaseController}
     */

    public AttackPhase(final GamePhaseController gpc, final GameManager gm) {
        this.gamePhaseController = gpc;
        this.gameManager = gm;
        this.turnManager = gamePhaseController.getTurnManager();
        this.logic = new LogicAttackImpl();
        this.attacker = turnManager.getCurrentPlayer();
        this.attackerSrc = Optional.empty();
        this.defenderDst = Optional.empty();
        this.unitsToUse = 0;
        this.isEnd = true;
        internalState = InternalState.SELECT_SRC;
    }

    @Override
    public boolean isComplete() {
        return isEnd;
    }

    @Override
    public void performAction() {
        if (internalState == InternalState.SELECT_SRC && attackerSrc.isPresent()) {
            isEnd = false;
            nextState();
        } else if (internalState == InternalState.SELECT_DST && defenderDst.isPresent()) {
            nextState();
        } else if (internalState == InternalState.SELECT_UNITS_QUANTITY && unitsToUse > 0) {
            nextState();
        } else if (internalState == InternalState.EXECUTE) {
            if (logic.attack(attacker, defender, attackerSrc.get(), defenderDst.get(), unitsToUse)) {
                attacker.addGameCard(new CardGameControllerImpl(gameManager).drawCard());
                this.gamePhaseController.uodateViewTerritoryOwner();
            }
            isEnd = true; // Mark that an attack has been executed
            nextState();
        }
    }

    @Override
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "We intentionally store the Territory reference; game logic needs mutable state.")
    public boolean selectTerritory(final Territory t) {
        if (internalState == InternalState.SELECT_SRC && isValidAttacker(t)) {
            this.attackerSrc = Optional.ofNullable(t);
            this.attacker = t.getOwner();
            unitsToUse = 0;
            return true;
        } else if (internalState == InternalState.SELECT_DST && isValidDefender(t)) {
            this.defender = t.getOwner();
            this.defenderDst = Optional.ofNullable(t);
            return true;
        }
        return false;
    }

    @Override
    public void setUnitsToUse(final int units) {
        if (internalState == InternalState.SELECT_UNITS_QUANTITY
                && units <= attackerSrc.map(t -> t.getUnits()).orElse(0) - 1
                && units > 0) {
            unitsToUse = units;
        }
    }

    @Override
    public String getInnerStatePhaseDescription() {
        switch (internalState) {

            case SELECT_SRC -> {
                return "Selecting attacker";
            }
            case SELECT_DST -> {
                return "Selecting defender";
            }
            case SELECT_UNITS_QUANTITY -> {
                return "Selecting units quantity";
            }
            case EXECUTE -> {
                return "Executing the attack";
            }
            default -> throw new AssertionError();
        }
    }

    // Orredno serev per testare gli attacchi
    public void setAttackerDice(List<Integer> dice) {
        LogicAttackImpl l = (LogicAttackImpl) logic;
        l.setAttackerDice(dice);
    }

    // Orrendo serve per testare gli attacchi
    public void setDefencerDice(List<Integer> dice) {
        LogicAttackImpl l = (LogicAttackImpl) logic;
        l.setDefencerDice(dice);
    }

    public LogicAttack getAttackLogic() {
        return logic;
    }

    private boolean isValidAttacker(final Territory t) {
        final boolean hasEnemyNeighbor = t.getNeightbours().stream()
                .anyMatch(neighbour -> !neighbour.getOwner().equals(turnManager.getCurrentPlayer()));
        final boolean hasEnoughUnits = t.getUnits() >= 2;
        final boolean isMine = t.getOwner().equals(turnManager.getCurrentPlayer());
        return hasEnemyNeighbor && hasEnoughUnits && isMine;
    }

    private boolean isValidDefender(final Territory t) {
        boolean isMy = t.getOwner().equals(turnManager.getCurrentPlayer());
        // boolean isNeightbour = attackerSrc.getNeightbours().contains(t);
        boolean isNeightbour = attackerSrc.map(Territory::getNeightbours)
                .orElse(Set.of()).contains(t);

        return !isMy && isNeightbour;
    }

    @Override
    public Optional<AttackResult> showAttackResults() {
        return logic.showAttackResults();
    }

    @Override
    public void enableFastAttack() {
        logic.enableFastAttack();
    }

    @Override
    public void nextState() {
        switch (internalState) {
            case SELECT_SRC -> internalState = InternalState.SELECT_DST;
            case SELECT_DST -> internalState = InternalState.SELECT_UNITS_QUANTITY;
            case SELECT_UNITS_QUANTITY -> internalState = InternalState.EXECUTE;
            case EXECUTE -> internalState = InternalState.SELECT_SRC;
            default -> throw new IllegalArgumentException("Unexpected value: " + internalState);
        }
    }

    @Override
    public InternalState getInternalState() {
        return internalState;
    }
}
