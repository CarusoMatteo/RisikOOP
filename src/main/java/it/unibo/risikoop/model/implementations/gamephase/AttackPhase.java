package it.unibo.risikoop.model.implementations.gamephase;

import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.GamePhase;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.TurnManager;

public class AttackPhase implements GamePhase{

    private enum PhaseState {
        SELECT_ATTACKER,
        SELECT_DEFENDER,
        SELECT_UNITS,
        EXECUTE_ATTACK
    }

    private final TurnManager turnManager;
    private PhaseState state;
    private Territory attacker;
    private Territory defender;
    private int unitsToUse;
    private boolean isEnd;

    public AttackPhase(TurnManager turnManager) {
        this.turnManager = turnManager;
        this.state = PhaseState.SELECT_ATTACKER;
        this.attacker = null;
        this.defender = null;
        this.unitsToUse = 0; 
        this.isEnd = true;
    }


    @Override
    public boolean isComplete() {
        return isEnd;
    }

    @Override
    public void performAction() {
        
        if(state == PhaseState.SELECT_ATTACKER && attacker != null) {
            isEnd = false; 
            state = PhaseState.SELECT_DEFENDER;
        } else if(state == PhaseState.SELECT_DEFENDER && defender != null) {
            state = PhaseState.SELECT_UNITS;
        } else if(state == PhaseState.SELECT_UNITS && unitsToUse > 0) {
            state = PhaseState.EXECUTE_ATTACK;
        } else if(state == PhaseState.EXECUTE_ATTACK) {
            //todo: Logic to execute the attack
            isEnd = true; // Mark that an attack has been executed
            state = PhaseState.SELECT_ATTACKER; // Reset to allow for another attack
        }
    }

    @Override
    public void selectTerritory(Territory t) {
        if(state == PhaseState.SELECT_ATTACKER && isValidAttacker(t)){
            this.attacker = t;
            unitsToUse = 0; 
        } else if(state == PhaseState.SELECT_DEFENDER && isValidDefender(t) ) {
            this.defender = t;
        }  
    }

    @Override
    public void setUnitsToUse(int units) {
        if(units > 0 && units <= attacker.getUnits() - 1){
            unitsToUse = units;
        }
    }

    private boolean isValidAttacker(Territory t) {
        boolean hasEnemyNeighbor = t.getNeightbours().stream()
            .anyMatch(neighbour -> !neighbour.getOwner().equals(turnManager.getCurrentPlayer()));
        boolean hasEnoughUnits = t.getUnits() >= 2;
        return hasEnemyNeighbor && hasEnoughUnits;
    }

    private boolean isValidDefender(Territory t) {
        return !t.getOwner().equals(turnManager.getCurrentPlayer()) && 
               attacker.getNeightbours().contains(t);
    }
    
}
