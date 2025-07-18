package it.unibo.risikoop.controller.interfaces;

import java.util.Optional;

import it.unibo.risikoop.model.interfaces.AttackResult;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.TurnManager;
import it.unibo.risikoop.model.interfaces.gamephase.GamePhase;
import it.unibo.risikoop.model.interfaces.gamephase.InternalState;

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

        public PhaseKey next() {
            final int idx = (this.ordinal() + 1) % values().length;
            return values()[idx];
        }
    }

    /**
     * Selects a territory to perform the current phase's action on.
     *
     * @param territory the {@link Territory} to select
     * @return if the selected territory is suitable for whatever phase you are in
     */
    boolean selectTerritory(Territory territory);

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

    /**
     * Returns the current game phase.
     *
     * @return the current {@link GamePhase}
     */
    GamePhase getCurrentPhase();

    /**
     * Shows the results of the attack.
     *
     * @return an {@link AttackResult} containing the dice rolls for both attacker
     *         and defender
     */
    Optional<AttackResult> showAttackResults();

    /**
     * Enables fast attack mode, allowing the player to perform multiple attacks
     * in quick succession without waiting for confirmation after each attack.
     */
    void enableFastAttack();

    /**
     * return the internal state of the current state.
     * 
     * @return an optional that describe the internal state
     */
    Optional<InternalState> getInternalState();

    /**
     * return the Phase that we are current in.
     * 
     * @return the {@link PhaseKey}
     */
    PhaseKey getPhaseKey();

    /**
     * make the view update when showing the territory's owner.
     */
    void uodateViewTerritoryOwner();
}
