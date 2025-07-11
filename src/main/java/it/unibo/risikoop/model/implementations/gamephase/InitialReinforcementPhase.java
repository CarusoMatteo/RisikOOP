package it.unibo.risikoop.model.implementations.gamephase;

import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.GamePhase;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.TurnManager;

public class InitialReinforcementPhase implements GamePhase {

    private static final double AVERAGE_UNITS_PER_TERRITORY = 2.5;
    private final TurnManager turnManager;
    private final int initialUnits;
    private final GameManager gameManager;

    public InitialReinforcementPhase(TurnManager turnManager, GameManager gameManager) {
        this.turnManager = turnManager;
        this.gameManager = gameManager;
        initialUnits = calculateInitialUnits();
    }

    @Override
    public boolean isComplete() {
        return turnManager.isNewRound();
    }

    @Override
    public void performAction() {
        Player p = turnManager.getCurrentPlayer();
        if (p.getUnitsToPlace() <= 0) {
            turnManager.nextPlayer();
            turnManager.getCurrentPlayer().addUnitsToPlace(initialUnits);
        } 
    }

    @Override
    public void selectTerritory(Territory t) {
        Player p = turnManager.getCurrentPlayer();

        if(!p.getTerritories().contains(t)) {
            throw new IllegalArgumentException("Player does not own the selected territory.");
        }

        if(p.getUnitsToPlace() > 0){
            t.addUnits(1);
            p.removeUnitsToPlace(1);
        }
    }

    private int calculateInitialUnits(){
        int territories = gameManager.getTerritories().size();
        int players = gameManager.getPlayers().size();
        int initialUnits = (int) (AVERAGE_UNITS_PER_TERRITORY * (territories / players));
        return initialUnits;
    } 
}
