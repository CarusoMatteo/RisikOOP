package it.unibo.risikoop.controller.interfaces;

import it.unibo.risikoop.model.interfaces.GamePhase;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.TurnManager;

/**
 * Controller interface for managing the different phases of the game.
 * <p>
 * A GamePhaseController handles user interactions and executes
 * the logic for a specific phase (e.g., attack, fortification, reinforcement),
 * including territory selection, performing actions, and transitioning
 * to the next phase.
 * </p>
 */
public interface GamePhaseController {

    /**
     * Selects a territory to perform the current phase's action on.
     *
     * @param territory the {@link Territory} to select
     * @throws IllegalArgumentException when territory selected is unusable
     */
    void selectTerritory(Territory territory);

    /**
     * Executes the action defined by the current game phase.
     * <p>
     * The exact operation depends on the phase—for example,
     * carrying out an attack, validating a fortification move,
     * or distributing reinforcements.
     * </p>
     */
    void performAction();

    /**
     * Advances the controller to the next game phase.
     * <p>
     * Transitions from the current phase to the subsequent one
     * in the game's sequence (e.g., from attack phase to fortification phase).
     * </p>
     */
    void nextPhase();

    /**
     * Specifies the number of units to use for the upcoming action
     * in the current phase.
     *
     * @param units the number of units to use
     */
    void setUnitsToUse(int units);

    /**
     * Gives the actual game phase description.
     * 
     * @return a string holding the description
     */
    String getStateDescription();

    /**
     * Gives the actual inner game phase description.
     * 
     * @return a string holding the description
     */
    String getInnerStatePhaseDescription();

    /**
     * return th turn manager for the faces.
     * 
     * @return the TurnManager object
     */
    TurnManager getTurnManager();

    /**
     * go to the next player in the list.
     */
    void nextPlayer();

    GamePhase getCurrentPhase();

    void updateDstTerritory(String name);
}
