package it.unibo.risikoop.model.gameflowtest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import it.unibo.risikoop.controller.interfaces.GamePhaseController.PhaseKey;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;

/**
 * Tests for the Combo (manage combos) phase of the game flow.
 * In this phase, no actions are allowed except advancing to the next phase.
 */
@DisplayName("Combo Phase")
class ComboPhaseTest extends AbstractGamePhaseTest {
    /**
     * Helper to reach the combo phase by completing initial reinforcement for
     * both
     * players.
     */
    private void reachComboPhase() {
        // For each player: place all initial units then performAction to advance
        for (final Player player : getGm().getPlayers()) {
            // calculate units to place: initialLogic - territories owned
            final int unitsToPlace = getInitialLogic().calcPlayerUnits()
                    - player.getTerritories().size();
            final Territory t = player.getTerritories().iterator().next();
            for (int i = 0; i < unitsToPlace; i++) {
                getGpc().selectTerritory(t);
            }
            // after placing all, performAction moves to next player's placement
            getGpc().performAction();
        }
        // After both players, controller should be in Combo phase
        assertEquals(COMBO, getGpc().getStateDescription(),
                "Should be in combo phase after initial reinforcement");
    }

    @Test
    @DisplayName("1) No actions except nextPhase")
    void noActionsAllowedExceptNextPhase() {
        // Arrange: reach Combo phase
        reachComboPhase();
        final Player current = getTm().getCurrentPlayer();

        // Act & Assert: selectTerritory does nothing
        final Territory any = getGm().getTerritory("T1").get();
        getGpc().selectTerritory(any);
        assertEquals(COMBO, getGpc().getStateDescription(),
                "State should remain Combo after selectTerritory");
        assertEquals(current, getTm().getCurrentPlayer(),
                "Current player should not change after selectTerritory");

        // Act & Assert: performAction does nothing
        getGpc().performAction();
        assertEquals(COMBO, getGpc().getStateDescription(),
                "State should remain Combo after performAction");
        assertEquals(current, getTm().getCurrentPlayer(),
                "Current player should not change after performAction");
    }

    @Test
    @DisplayName("2) nextPhase advances to Reinforcement")
    void nextPhaseAdvancesToReinforcement() {
        // Arrange: reach Combo phase
        reachComboPhase();
        final Player current = getTm().getCurrentPlayer();

        // Act: advance to next phase
        getGpc().nextPhase();

        // Assert: state is Reinforcement, same player
        assertEquals(REINFORCEMENT, getGpc().getStateDescription(),
                "State should become Reinforcement after nextPhase");
        assertEquals(current, getTm().getCurrentPlayer(),
                "Current player should remain the same after nextPhase");
    }

    @Override
    protected PhaseKey startPhase() {
        return PhaseKey.COMBO;
    }
}
