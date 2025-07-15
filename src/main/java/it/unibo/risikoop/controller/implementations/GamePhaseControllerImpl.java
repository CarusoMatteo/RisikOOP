package it.unibo.risikoop.controller.implementations;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import it.unibo.risikoop.controller.interfaces.GamePhaseController;
import it.unibo.risikoop.model.implementations.gamephase.AttackPhase;
import it.unibo.risikoop.model.implementations.gamephase.ComboPhaseImpl;
import it.unibo.risikoop.model.implementations.gamephase.InitialReinforcementPhase;
import it.unibo.risikoop.model.implementations.gamephase.MovementPhase;
import it.unibo.risikoop.model.implementations.gamephase.ReinforcementPhase;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.GamePhase;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.TurnManager;
import it.unibo.risikoop.view.interfaces.RisikoView;

/**
 * Coordinates the progression and transition of game phases for each player.
 * <p>
 * At the start of the game, phases follow the sequence:
 * INITIAL_REINFORCEMENT → REINFORCEMENT → COMBO → ATTACK → MOVEMENT.
 * For subsequent turns, the INITIAL_REINFORCEMENT phase is skipped,
 * and phases cycle through REINFORCEMENT → COMBO → ATTACK → MOVEMENT.
 * </p>
 */
public final class GamePhaseControllerImpl implements GamePhaseController {

    private enum PhaseKey {
        INITIAL_REINFORCEMENT,
        REINFORCEMENT,
        COMBO,
        ATTACK,
        MOVEMENT;

        PhaseKey next() {
            final int idx = (this.ordinal() + 1) % values().length;
            return values()[idx];
        }
    }

    private final TurnManager turnManager;
    private final Map<PhaseKey, GamePhase> phases;
    private final List<RisikoView> viewList;
    private PhaseKey current;
    private boolean initialDone;

    /**
     * Creates a new GamePhaseControllerImpl that will manage game phases
     * using the provided turn manager and game manager.
     * <p>
     * Initializes all phase implementations, sets the current phase
     * to INITIAL_REINFORCEMENT, and calls initializationPhase() on it.
     * </p>
     * 
     * @param viewList the list of every view
     * @param tm       the TurnManager that determines player turn order
     * @param gm       the GameManager providing game state and context
     */
    public GamePhaseControllerImpl(final List<RisikoView> viewList, final TurnManager tm, final GameManager gm) {
        this.turnManager = tm;
        this.phases = new EnumMap<>(PhaseKey.class);
        this.viewList = viewList;
        phases.put(PhaseKey.INITIAL_REINFORCEMENT, new InitialReinforcementPhase(this, tm, gm));
        phases.put(PhaseKey.COMBO, new ComboPhaseImpl());
        phases.put(PhaseKey.REINFORCEMENT, new ReinforcementPhase(gm, tm));
        phases.put(PhaseKey.ATTACK, new AttackPhase(this, tm));
        phases.put(PhaseKey.MOVEMENT, new MovementPhase(this, tm));

        this.current = PhaseKey.INITIAL_REINFORCEMENT;
        phases.get(current).initializationPhase();

        this.initialDone = false;
    }

    private GamePhase phase() {
        return phases.get(current);
    }

    @Override
    public void selectTerritory(final Territory t) {
        phase().selectTerritory(t);
        viewList.forEach(i -> i.getMapScene().ifPresent(m -> m.changeTerritoryUnits(t.getName(), t.getUnits())));
    }

    @Override
    public void setUnitsToUse(final int units) {
        phase().setUnitsToUse(units);
    }

    @Override
    public void performAction() {
        phase().performAction();
        viewList.stream().map(v -> v.getMapScene())
                .forEach(o -> o.ifPresent(m -> m.updateCurrentPlayer(
                        turnManager.getCurrentPlayer().getName(),
                        turnManager.getCurrentPlayer().getColor(),
                        turnManager.getCurrentPlayer().getObjectiveCard(),
                        turnManager.getCurrentPlayer().getGameCards())));
    }

    private void advancePhase() {
        final PhaseKey prev = current;
        PhaseKey next = current.next();

        // Mark initial reinforcement as completed after its first execution
        if (prev == PhaseKey.INITIAL_REINFORCEMENT) {
            initialDone = true;
        }

        // Skip INITIAL_REINFORCEMENT in subsequent turns
        if (initialDone && next == PhaseKey.INITIAL_REINFORCEMENT) {
            next = PhaseKey.REINFORCEMENT;
        }

        current = next;
        phase().initializationPhase();

        // After MOVEMENT transitions to COMBO, advance to next player's turn
        viewList.forEach(i -> i.getMapScene()
                .ifPresent(m -> m.enableAction(current == PhaseKey.ATTACK || current == PhaseKey.MOVEMENT)));
        if (prev == PhaseKey.MOVEMENT && current == PhaseKey.COMBO) {
            turnManager.nextPlayer();

        }
    }

    @Override
    public void nextPhase() {
        if (phase().isComplete()) {
            advancePhase();
            viewList.stream().map(v -> v.getMapScene())
                    .forEach(o -> o.ifPresent(m -> m.updateCurrentPlayer(
                            turnManager.getCurrentPlayer().getName(),
                            turnManager.getCurrentPlayer().getColor(),
                            turnManager.getCurrentPlayer().getObjectiveCard(),
                            turnManager.getCurrentPlayer().getGameCards())));
        }

        System.out.println(current.name());
        System.out.println(turnManager.getCurrentPlayer().getName());

    }

    @Override
    public void updateSrcTerritory(String srcTerritoryName) {
        viewList.forEach(i -> i.getMapScene().ifPresent(m -> m.updateSrcTerritory(srcTerritoryName)));

    }

    @Override
    public void updateDstTerritory(String dstTerritoryName) {
        viewList.forEach(i -> i.getMapScene().ifPresent(m -> m.updateDstTerritory(dstTerritoryName)));
    }

    @Override
    public void updatePhaseRelatedText(String srcTerritoryKindString, String dstterritoryKindString,
            String changeStateButonString) {
        viewList.forEach(i -> i.getMapScene().ifPresent(
                m -> m.updatePhaseRelatedText(srcTerritoryKindString, dstterritoryKindString, changeStateButonString)));
    }
}
