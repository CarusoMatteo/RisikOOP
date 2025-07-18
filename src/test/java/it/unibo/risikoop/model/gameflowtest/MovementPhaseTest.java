package it.unibo.risikoop.model.gameflowtest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import it.unibo.risikoop.controller.interfaces.GamePhaseController.PhaseKey;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;

/**
 * Tests for the Movement phase of the game flow.
 */
@DisplayName("Movement Phase")
class MovementPhaseTest extends AbstractGamePhaseTest {

    @Override
    protected PhaseKey startPhase() {
        return PhaseKey.MOVEMENT;
    }

    @Test
    @DisplayName("1) Cannot select enemy territory as source")
    void cannotSelectEnemyAsSource() {
        Player current = tm.getCurrentPlayer();
        Territory enemy = gm.getTerritory("T3").get(); // Bob's territory
        // Attempt to select
        assertFalse(gpc.selectTerritory(enemy), "Should not allow selecting enemy as source");
        // State remains Selecting the moving from territory
        assertEquals("Selecting the moving from territory", gpc.getInnerStatePhaseDescription());
        assertEquals(current, tm.getCurrentPlayer());
    }

    @Test
    @DisplayName("2) Selecting own territory advances to destination selection")
    void selectingOwnTerritoryAdvancesToDestination() {
        Territory src = gm.getTerritory("T1").get(); // Alice's territory
        // Give some units so movement is possible
        src.addUnits(3);

        assertTrue(gpc.selectTerritory(src), "Should allow selecting own territory as source");
        gpc.performAction();
        assertEquals("Selecting the moving to territory", gpc.getInnerStatePhaseDescription());
    }

    @Test
    @DisplayName("3) Cannot select invalid destination")
    void cannotSelectInvalidDestination() {
        Territory src = gm.getTerritory("T1").get();
        src.addUnits(3);
        gpc.selectTerritory(src);
        gpc.performAction(); // now in SELECT_DESTINATION

        Territory invalid = gm.getTerritory("T3").get(); // enemy, even though neighbor
        assertFalse(gpc.selectTerritory(invalid), "Should not allow selecting enemy as destination");
        // State remains
        assertEquals("Selecting the moving to territory", gpc.getInnerStatePhaseDescription());
    }

    @Test
    @DisplayName("4) Selecting valid destination advances to units selection")
    void selectingValidDestinationAdvancesToUnits() {
        Territory src = gm.getTerritory("T1").get();
        Territory dest = gm.getTerritory("T2").get(); // also Alice's and neighbor
        src.addUnits(3);

        gpc.selectTerritory(src);
        gpc.performAction();
        assertTrue(gpc.selectTerritory(dest), "Should allow selecting own neighbor as destination");
        gpc.performAction();
        assertEquals("Selecting units quantity", gpc.getInnerStatePhaseDescription());
    }

    @Test
    @DisplayName("5) Units selection enforces positive and <= sourceUnits-1")
    void unitsSelectionEnforced() {
        Territory src = gm.getTerritory("T1").get();
        Territory dest = gm.getTerritory("T2").get();
        src.addUnits(3);

        // reach units selection
        gpc.selectTerritory(src);
        gpc.performAction();
        gpc.selectTerritory(dest);
        gpc.performAction();
        assertEquals("Selecting units quantity", gpc.getInnerStatePhaseDescription());

        // invalid: zero
        gpc.setUnitsToUse(0);
        gpc.performAction();
        assertEquals("Selecting units quantity", gpc.getInnerStatePhaseDescription());

        // invalid: >= srcUnits
        gpc.setUnitsToUse(src.getUnits());
        gpc.performAction();
        assertEquals("Selecting units quantity", gpc.getInnerStatePhaseDescription());

        // valid
        gpc.setUnitsToUse(2);
        gpc.performAction();
        assertEquals("Executing the movement", gpc.getInnerStatePhaseDescription());
    }

    @Test
    @DisplayName("6) Executing movement transfers units and allows loop or end phase")
    void executingMovementTransfersUnitsAndAllowsLoop() {
        Territory src = gm.getTerritory("T1").get();
        Territory dest = gm.getTerritory("T2").get();
        src.addUnits(3);

        // reach execute
        gpc.selectTerritory(src); gpc.performAction();
        gpc.selectTerritory(dest); gpc.performAction();
        gpc.setUnitsToUse(2); gpc.performAction();
        assertEquals("Executing the movement", gpc.getInnerStatePhaseDescription());

        int srcBefore = src.getUnits();
        int destBefore = dest.getUnits();

        // performAction should execute movement
        gpc.performAction();
        assertEquals(srcBefore - 2, src.getUnits());
        assertEquals(destBefore + 2, dest.getUnits());

        // After movement, still in Executing the movement (allow more moves)
        assertEquals("Executing the movement", gpc.getInnerStatePhaseDescription());

        // Next overall phase should be advanced via nextPhase()
        gpc.nextPhase();
        assertEquals(COMBO, gpc.getStateDescription());
    }
}
