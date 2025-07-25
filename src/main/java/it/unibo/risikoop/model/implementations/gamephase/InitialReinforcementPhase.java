package it.unibo.risikoop.model.implementations.gamephase;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.risikoop.controller.implementations.logicgame.LogicCalcInitialUnitsImpl;
import it.unibo.risikoop.controller.interfaces.GamePhaseController;
import it.unibo.risikoop.controller.interfaces.logicgame.LogicReinforcementCalculator;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.gamephase.GamePhase;
import it.unibo.risikoop.model.interfaces.gamephase.PhaseDescribable;
import it.unibo.risikoop.model.interfaces.gamephase.PhaseWithActionToPerforme;
import it.unibo.risikoop.model.interfaces.gamephase.PhaseWithInitialization;

/**
 * Represents the initial reinforcement phase of the Risiko game.
 * In this phase, players receive a calculated number of units to place
 * on their owned territories at the start of the game.
 */
public final class InitialReinforcementPhase
        implements GamePhase, PhaseDescribable, PhaseWithInitialization, PhaseWithActionToPerforme {

    private final GamePhaseController gpc;
    private final int initialUnits;
    private final LogicReinforcementCalculator logic;

    /**
     * Constructs an InitialReinforcementPhase with the specified TurnManager and
     * GameManager.
     * The initial number of units is calculated based on the number of territories
     * and players.
     *
     * @param gpc         the GamePhaseController
     * @param gameManager the GameManager that manages the game state
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "We intentionally store the Territory reference;"
            + "game logic needs mutable state.")
    public InitialReinforcementPhase(final GamePhaseController gpc, final GameManager gameManager) {
        this.gpc = gpc;
        this.logic = new LogicCalcInitialUnitsImpl(gameManager);
        initialUnits = logic.calcPlayerUnits();
    }

    @Override
    public boolean isComplete() {
        return gpc.getTurnManager().isLastPlayer()
                && gpc.getTurnManager().getCurrentPlayer().getUnitsToPlace() == 0;
    }

    @Override
    public void performAction() {
        final Player p = gpc.getTurnManager().getCurrentPlayer();

        // System.out.println(p.getName());

        if (p.getUnitsToPlace() == 0 && !gpc.getTurnManager().isLastPlayer()) {
            gpc.nextPlayer();
            gpc.getTurnManager().getCurrentPlayer().addUnitsToPlace(initialUnits);
            addOneUnitOnEachPlayerTerritory();
        }
    }

    @Override
    public boolean selectTerritory(final Territory t) {
        final Player p = gpc.getTurnManager().getCurrentPlayer();

        if (p.getUnitsToPlace() > 0 && p.getTerritories().contains(t)) {
            t.addUnits(1);
            p.removeUnitsToPlace(1);
            return true;
        }
        return false;

    }

    @Override
    public void initializationPhase() {
        gpc.getTurnManager().getCurrentPlayer().addUnitsToPlace(initialUnits);
        addOneUnitOnEachPlayerTerritory();
    }

    private void addOneUnitOnEachPlayerTerritory() {
        final var p = gpc.getTurnManager().getCurrentPlayer();
        p.getTerritories().forEach(t -> {
            p.removeUnitsToPlace(1);
            t.addUnits(1);
        });
    }

    @Override
    public String getInnerStatePhaseDescription() {
        return "Player " + gpc.getTurnManager().getCurrentPlayer().getName() + " unit placement: "
                + gpc.getTurnManager().getCurrentPlayer().getUnitsToPlace();
    }
}
