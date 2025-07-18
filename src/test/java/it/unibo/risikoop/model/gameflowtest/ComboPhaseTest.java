package it.unibo.risikoop.model.gameflowtest;

import static org.junit.jupiter.api.Assertions.*;

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
        for (Player player : gm.getPlayers()) {
            // calculate units to place: initialLogic - territories owned
            int unitsToPlace = initialLogic.calcPlayerUnits() -
                    player.getTerritories().size();
            Territory t = player.getTerritories().get(0);
            for (int i = 0; i < unitsToPlace; i++) {
                gpc.selectTerritory(t);
            }
            // after placing all, performAction moves to next player's placement
            gpc.performAction();
        }
        // After both players, controller should be in Combo phase
        assertEquals(COMBO, gpc.getStateDescription(),
                "Should be in combo phase after initial reinforcement");
    }

    @Test
    @DisplayName("1) No actions except nextPhase")
    void noActionsAllowedExceptNextPhase() {
        // Arrange: reach Combo phase
        reachComboPhase();
        Player current = tm.getCurrentPlayer();

        // Act & Assert: selectTerritory does nothing
        Territory any = gm.getTerritory("T1").get();
        gpc.selectTerritory(any);
        assertEquals(COMBO, gpc.getStateDescription(),
                "State should remain Combo after selectTerritory");
        assertEquals(current, tm.getCurrentPlayer(),
                "Current player should not change after selectTerritory");

        // Act & Assert: performAction does nothing
        gpc.performAction();
        assertEquals(COMBO, gpc.getStateDescription(),
                "State should remain Combo after performAction");
        assertEquals(current, tm.getCurrentPlayer(),
                "Current player should not change after performAction");
    }

    @Test
    @DisplayName("2) nextPhase advances to Reinforcement")
    void nextPhaseAdvancesToReinforcement() {
        // Arrange: reach Combo phase
        reachComboPhase();
        Player current = tm.getCurrentPlayer();

        // Act: advance to next phase
        gpc.nextPhase();

        // Assert: state is Reinforcement, same player
        assertEquals(REINFORCEMENT, gpc.getStateDescription(),
                "State should become Reinforcement after nextPhase");
        assertEquals(current, tm.getCurrentPlayer(),
                "Current player should remain the same after nextPhase");
    }

    @Override
    protected PhaseKey startPhase() {
        return PhaseKey.COMBO;
    }
}
