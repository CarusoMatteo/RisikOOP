package it.unibo.risikoop.controller.implementations;

import it.unibo.risikoop.controller.interfaces.GamePhaseController;
import it.unibo.risikoop.model.interfaces.Territory;

import java.util.EnumMap;
import java.util.Map;

import it.unibo.risikoop.model.implementations.gamephase.AttackPhase;
import it.unibo.risikoop.model.implementations.gamephase.ComboPhaseImpl;
import it.unibo.risikoop.model.implementations.gamephase.InitialReinforcementPhase;
import it.unibo.risikoop.model.implementations.gamephase.MovementPhase;
import it.unibo.risikoop.model.implementations.gamephase.ReinforcementPhase;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.GamePhase;
import it.unibo.risikoop.model.interfaces.TurnManager;

/**
 * Orchestrates il ciclo di fasi:
 * * prima volta: INITIAL_REINFORCEMENT → REINFORCEMENT → COMBO → ATTACK →
 * MOVEMENT
 * * poi, per ogni turno: REINFORCEMENT → COMBO → ATTACK → MOVEMENT
 */
public class GamePhaseControllerImpl implements GamePhaseController {

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
    private PhaseKey current;
    private boolean initialDone;

    public GamePhaseControllerImpl(final TurnManager tm, final GameManager gm) {
        this.turnManager = tm;
        this.phases = new EnumMap<>(PhaseKey.class);

        phases.put(PhaseKey.INITIAL_REINFORCEMENT, new InitialReinforcementPhase(tm, gm));
        phases.put(PhaseKey.COMBO, new ComboPhaseImpl());
        phases.put(PhaseKey.REINFORCEMENT, new ReinforcementPhase(tm));
        phases.put(PhaseKey.ATTACK, new AttackPhase(tm));
        phases.put(PhaseKey.MOVEMENT, new MovementPhase(tm));

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
    }

    @Override
    public void setUnitsToUse(final int units) {
        phase().setUnitsToUse(units);
    }

    @Override
    public void performAction() {
        phase().performAction();
    }

    private void advancePhase() {
        final PhaseKey prev = current;
        PhaseKey next = current.next();

        // Se abbiamo finito INITIAL_REINFORCEMENT, segniamo che non va più rieseguita
        if (prev == PhaseKey.INITIAL_REINFORCEMENT) {
            initialDone = true;
        }

        // Se INITIAL è già andata e il prossimo sarebbe INITIAL, skip a REINFORCEMENT
        if (initialDone && next == PhaseKey.INITIAL_REINFORCEMENT) {
            next = PhaseKey.REINFORCEMENT;
        }

        current = next;
        phase().initializationPhase();

        // Ogni volta che passiamo da MOVEMENT → REINFORCEMENT, giro di turno
        if (prev == PhaseKey.MOVEMENT && current == PhaseKey.COMBO) {
            turnManager.nextPlayer();
        }
    }

    @Override
    public void nextPhase() {
        if (phase().isComplete()) {
            advancePhase();
        }
    }
}
