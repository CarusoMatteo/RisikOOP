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
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.TurnManager;

/**
 * Orchestrates il ciclo di fasi:
 *   * prima volta: INITIAL_REINFORCEMENT → REINFORCEMENT → COMBO → ATTACK → MOVEMENT
 *   * poi, per ogni turno: REINFORCEMENT → COMBO → ATTACK → MOVEMENT
 */
public class GamePhaseControllerImpl implements GamePhaseController {

    private enum PhaseKey {
        INITIAL_REINFORCEMENT,
        REINFORCEMENT,
        COMBO,
        ATTACK,
        MOVEMENT;

        PhaseKey next() {
            int idx = (this.ordinal() + 1) % values().length;
            return values()[idx];
        }
    }

    private final TurnManager turnManager;
    private final Map<PhaseKey, GamePhase> phases;
    private PhaseKey current;
    private boolean initialDone = false;

    public GamePhaseControllerImpl(TurnManager tm, GameManager gm) {
        this.turnManager = tm;
        this.phases = new EnumMap<>(PhaseKey.class);

        phases.put(PhaseKey.INITIAL_REINFORCEMENT, new InitialReinforcementPhase(tm, gm));
        phases.put(PhaseKey.COMBO,                 new ComboPhaseImpl());
        phases.put(PhaseKey.REINFORCEMENT,         new ReinforcementPhase(tm));
        phases.put(PhaseKey.ATTACK,                new AttackPhase(tm));
        phases.put(PhaseKey.MOVEMENT,              new MovementPhase(tm));

        this.current = PhaseKey.INITIAL_REINFORCEMENT;
        phases.get(current).initializationPhase();
    }

    private GamePhase phase() {
        return phases.get(current);
    }

    @Override
    public void selectTerritory(Territory t) {
        phase().selectTerritory(t);
    }

    @Override
    public void setUnitsToUse(int units) {
        phase().setUnitsToUse(units);
    }

    @Override
    public void performAction() {
        phase().performAction();
        if (phase().isComplete()) {
            advancePhase();
        }
    }

    private void advancePhase() {
        PhaseKey prev = current;
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

    // private void phaseBeginner(){

    //     if(current == PhaseKey.INITIAL_REINFORCEMENT){
            
    //     }

    // }

    // /**
    //  * @return il nome della fase attiva, utile per la UI
    //  */
    // public String getCurrentPhaseName() {
    //     return current.name();
    // }


    // // Set unit  
    // private void InitialReinforcementBegin(){
    //     phases.get(current).performAction();
    // }

    // private void reinforcementBegin(){
        
    // }


    // private void comboBegin(){

        
    // }

    // private void attackBegin(){
        
    // }

    // private void movementBegin(){
        
    // }

}

