package it.unibo.risikoop.controller.implementations;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import it.unibo.risikoop.model.interfaces.gamephase.PhaseDescribable;
import it.unibo.risikoop.model.interfaces.gamephase.PhaseWithActionToPerforme;
import it.unibo.risikoop.model.interfaces.gamephase.PhaseWithAttack;
import it.unibo.risikoop.model.interfaces.gamephase.PhaseWithInitialization;
import it.unibo.risikoop.model.interfaces.gamephase.PhaseWithUnits;
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

    /**
     * 
     */
    public enum PhaseKey {
        INITIAL_REINFORCEMENT("Fase di rinforzo iniziale"),
        COMBO("Fase di gestione combo"),
        REINFORCEMENT("Fase di rinforzo"),
        ATTACK("Fase di gestione attacchi"),
        MOVEMENT("Fase di gestione spostamenti");

        private final String desc;

        PhaseKey(final String desc) {
            this.desc = desc;
        }

        /**
         * a method that returns the description of the phase.
         * 
         * @return a string holding the description
         */
        @SuppressWarnings("unused")
        public String getLabelDesc() {
            return String.copyValueOf(desc.toCharArray());
        }

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
    private final GameManager gm;

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
        this.gm = gm;
        this.phases = new EnumMap<>(PhaseKey.class);
        this.viewList = viewList;

        phases.put(PhaseKey.INITIAL_REINFORCEMENT, new InitialReinforcementPhase(this, gm));
        phases.put(PhaseKey.COMBO, new ComboPhaseImpl());
        phases.put(PhaseKey.REINFORCEMENT, new ReinforcementPhase(gm, this));
        phases.put(PhaseKey.ATTACK, new AttackPhase(this));
        phases.put(PhaseKey.MOVEMENT, new MovementPhase(this));

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
        viewUpdate();
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
        viewUpdate();
    }

    @Override
    public void nextPhase() {
        if (phase().isComplete()) {
            advancePhase();
            viewUpdate();
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
    public Optional<AttackResult> showAttackResults(){
        return currentPhaseAs(PhaseWithAttack.class)
                .flatMap(PhaseWithAttack::showAttackResults);
    }
    
    @Override
    public void enableFastAttack(){
        currentPhaseAs(PhaseWithAttack.class)
                .ifPresent(PhaseWithAttack::enableFastAttack);
    }

    private void viewUpdate() {
        Player p = turnManager.getCurrentPlayer();
        gm.getTerritories().forEach(t -> viewList
                .forEach(i -> i.getMapScene().ifPresent(m -> m.changeTerritoryUnits(t.getName(), t.getUnits()))));
        viewList.stream().map(v -> v.getMapScene())
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

    private <T> Optional<T> currentPhaseAs(Class<T> iface) {
        if (iface.isInstance(phase())) {
            return Optional.of(iface.cast(phase()));
        }
        return Optional.empty();
    }

}
