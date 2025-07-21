package it.unibo.risikoop.model.interfaces.gamephase;

import it.unibo.risikoop.model.interfaces.Territory;

/**
 * Interface representing a game phase in the Risiko game.
 * Each phase allows players to interact with the game state,
 * such as selecting territories, confirming actions, and canceling selections.
 */
public interface GamePhase {

    /**
     * Select a territory to perform an action on.
     * 
     * @param t the Territory to select
     * @return true if you could succesfully select the territory, false otherwise
     */
    boolean selectTerritory(Territory t);

    /**
     * Check if phase is complete.
     * 
     * @return true if the phase is complete, false otherwise
     */
    boolean isComplete();
}
