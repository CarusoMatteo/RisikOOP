package it.unibo.risikoop.controller.implementations;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.risikoop.controller.interfaces.GamePhaseController;
import it.unibo.risikoop.model.implementations.gamephase.AttackPhase;
import it.unibo.risikoop.model.implementations.gamephase.ComboPhaseImpl;
import it.unibo.risikoop.model.implementations.gamephase.InitialReinforcementPhase;
import it.unibo.risikoop.model.implementations.gamephase.MovementPhase;
import it.unibo.risikoop.model.implementations.gamephase.ReinforcementPhase;
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
import it.unibo.risikoop.model.interfaces.gamephase.PhaseWithInitialization;
import it.unibo.risikoop.model.interfaces.gamephase.PhaseWithTransaction;
import it.unibo.risikoop.model.interfaces.gamephase.PhaseWithUnits;
import it.unibo.risikoop.view.interfaces.MapScene;
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

    private final TurnManager turnManager;
    private final Map<PhaseKey, GamePhase> phases;
    private final List<RisikoView> viewList;
    private PhaseKey current;
    private boolean initialDone;
    private final GameManager gm;
    private final Runnable onGameOver;

    /**
     * Creates a new GamePhaseControllerImpl that will manage game phases
     * using the provided turn manager and game manager.
     * <p>
     * Initializes all phase implementations, sets the current phase
     * to INITIAL_REINFORCEMENT, and calls initializationPhase() on it.
     * </p>
     * 
     * @param viewList   the list of every view
     * @param tm         the TurnManager that determines player turn order
     * @param gm         the GameManager providing game state and context
     * @param onGameOver the {@link Runnable} descriving the method to call when
     *                   some player wins
     */
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public GamePhaseControllerImpl(final List<RisikoView> viewList, final TurnManager tm, final GameManager gm,
            final Runnable onGameOver) {
        this.turnManager = tm;
        this.gm = gm;
        this.phases = new EnumMap<>(PhaseKey.class);
        this.viewList = Collections.unmodifiableList(viewList);
        this.onGameOver = onGameOver;

        phases.put(PhaseKey.INITIAL_REINFORCEMENT, new InitialReinforcementPhase(this, gm));
        phases.put(PhaseKey.COMBO, new ComboPhaseImpl());
        phases.put(PhaseKey.REINFORCEMENT, new ReinforcementPhase(gm, this));
        phases.put(PhaseKey.ATTACK, new AttackPhase(this, gm));
        phases.put(PhaseKey.MOVEMENT, new MovementPhase(this));

        this.current = PhaseKey.INITIAL_REINFORCEMENT;
        currentPhaseAs(PhaseWithInitialization.class)
                .ifPresent(PhaseWithInitialization::initializationPhase);

        this.initialDone = false;
    }

    /**
     * Only used for testing.
     * 
     * @param viewList
     * @param tm
     * @param gm
     * @param onGameOver
     * @param startPhase
     */
    public GamePhaseControllerImpl(
            final List<RisikoView> viewList,
            final TurnManager tm,
            final GameManager gm,
            final Runnable onGameOver,
            final PhaseKey startPhase) {
        this(viewList, tm, gm, onGameOver);
        // Forza la fase corrente a quella indicata
        this.current = startPhase;
        // Segna come già inizializzata la fase iniziale per evitare doppie
        // inizializzazioni
        this.initialDone = true;
        // Se necessario, inizializza il nuovo stato di fase
        currentPhaseAs(PhaseWithInitialization.class)
                .ifPresent(PhaseWithInitialization::initializationPhase);
    }

    private GamePhase phase() {
        return phases.get(current);
    }

    @Override
    public boolean selectTerritory(final Territory t) {
        final var results = phase().selectTerritory(t);
        viewUpdate();
        checkWin();
        return results;
    }

    @Override
    public void setUnitsToUse(final int units) {
        currentPhaseAs(PhaseWithUnits.class)
                .ifPresent(p -> p.setUnitsToUse(units));

    }

    @Override
    public void performAction() {
        currentPhaseAs(PhaseWithActionToPerforme.class)
                .ifPresent(PhaseWithActionToPerforme::performAction);
        checkWin();
        viewUpdate();
    }

    private void checkWin() {
        if (turnManager.getCurrentPlayer().getObjectiveCard().isAchieved()) {
            // System.out.println(turnManager.getCurrentPlayer().getName() + " has won the
            // game!");
            onGameOver.run();
        }
    }

    @Override
    public void nextPhase() {
        if (phase().isComplete()) {
            advancePhase();
            viewUpdate();
            checkWin();
        }
    }

    @Override
    public String getStateDescription() {
        return String.copyValueOf(current.getLabelDesc().toCharArray());
    }

    @Override
    public TurnManager getTurnManager() {
        return turnManager;
    }

    @Override
    public void nextPlayer() {
        turnManager.nextPlayer();
        viewUpdate();
    }

    @Override
    public String getInnerStatePhaseDescription() {
        return currentPhaseAs(PhaseDescribable.class)
                .map(PhaseDescribable::getInnerStatePhaseDescription).orElse("");
    }

    @Override
    public GamePhase getCurrentPhase() {
        return phase();
    }

    @Override
    public Optional<AttackResult> showAttackResults() {
        return currentPhaseAs(PhaseWithAttack.class)
                .flatMap(PhaseWithAttack::showAttackResults);
    }

    @Override
    public void enableFastAttack() {
        currentPhaseAs(PhaseWithAttack.class)
                .ifPresent(PhaseWithAttack::enableFastAttack);
    }

    private void viewUpdate() {
        final Player p = turnManager.getCurrentPlayer();
        gm.getTerritories().forEach(t -> viewList
                .forEach(i -> i.getMapScene().ifPresent(m -> m.changeTerritoryUnits(t.getName(), t.getUnits()))));
        viewList.stream().map(RisikoView::getMapScene)
                .forEach(o -> o.ifPresent(m -> m.updateCurrentPlayer(
                        p.getName(),
                        p.getColor(),
                        p.getObjectiveCard(),
                        p.getGameCards())));

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
            next = PhaseKey.COMBO;
        }

        current = next;
        currentPhaseAs(PhaseWithInitialization.class)
                .ifPresent(PhaseWithInitialization::initializationPhase);

        // After MOVEMENT transitions to COMBO, advance to next player's turn
        if ((prev == PhaseKey.MOVEMENT && current == PhaseKey.COMBO)
                || prev == PhaseKey.INITIAL_REINFORCEMENT) {
            turnManager.nextPlayer();

        }
    }

    private <T> Optional<T> currentPhaseAs(final Class<T> iface) {
        if (iface.isInstance(phase())) {
            return Optional.of(iface.cast(phase()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<InternalState> getInternalState() {
        return currentPhaseAs(PhaseWithTransaction.class).map(PhaseWithTransaction::getInternalState);
    }

    @Override
    public PhaseKey getPhaseKey() {
        return current;
    }

    @Override
    public void uodateViewTerritoryOwner() {
        viewList
                .stream()
                .map(RisikoView::getMapScene)
                .forEach(m -> m.ifPresent(MapScene::updateTerritoryOwner));
    }
}
