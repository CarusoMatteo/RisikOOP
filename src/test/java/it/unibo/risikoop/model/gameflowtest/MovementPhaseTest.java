package it.unibo.risikoop.model.gameflowtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    private static final String SELECTING_UNITS_QUANTITY = "Selecting units quantity";

    @Override
    protected PhaseKey startPhase() {
        return PhaseKey.MOVEMENT;
    }

    @Test
    @DisplayName("1) Cannot select enemy territory as source")
    void cannotSelectEnemyAsSource() {
        final Player current = getTm().getCurrentPlayer();
        final Territory enemy = getGm().getTerritory("T3").get(); // Bob's territory
        // Attempt to select
        assertFalse(getGpc().selectTerritory(enemy), "Should not allow selecting enemy as source");
        // State remains Selecting the moving from territory
        assertEquals("Selecting the moving from territory", getGpc().getInnerStatePhaseDescription());
        assertEquals(current, getTm().getCurrentPlayer());
    }

    @Test
    @DisplayName("2) Selecting own territory advances to destination selection")
    void selectingOwnTerritoryAdvancesToDestination() {
        final Territory src = getGm().getTerritory("T1").get(); // Alice's territory
        // Give some units so movement is possible
        src.addUnits(3);

        assertTrue(getGpc().selectTerritory(src), "Should allow selecting own territory as source");
        getGpc().performAction();
        assertEquals("Selecting the moving to territory", getGpc().getInnerStatePhaseDescription());
    }

    @Test
    @DisplayName("3) Cannot select invalid destination")
    void cannotSelectInvalidDestination() {
        final Territory src = getGm().getTerritory("T1").get();
        src.addUnits(3);
        getGpc().selectTerritory(src);
        getGpc().performAction(); // now in SELECT_DESTINATION

        final Territory invalid = getGm().getTerritory("T3").get(); // enemy, even though neighbor
        assertFalse(getGpc().selectTerritory(invalid), "Should not allow selecting enemy as destination");
        // State remains
        assertEquals("Selecting the moving to territory", getGpc().getInnerStatePhaseDescription());
    }

    @Test
    @DisplayName("4) Selecting valid destination advances to units selection")
    void selectingValidDestinationAdvancesToUnits() {
        final Territory src = getGm().getTerritory("T1").get();
        final Territory dest = getGm().getTerritory("T2").get(); // also Alice's and neighbor
        src.addUnits(3);

        getGpc().selectTerritory(src);
        getGpc().performAction();
        assertTrue(getGpc().selectTerritory(dest), "Should allow selecting own neighbor as destination");
        getGpc().performAction();
        assertEquals(SELECTING_UNITS_QUANTITY, getGpc().getInnerStatePhaseDescription());
    }

    @Test
    @DisplayName("5) Units selection enforces positive and <= sourceUnits-1")
    void unitsSelectionEnforced() {
        final Territory src = getGm().getTerritory("T1").get();
        final Territory dest = getGm().getTerritory("T2").get();
        src.addUnits(3);

        // reach units selection
        getGpc().selectTerritory(src);
        getGpc().performAction();
        getGpc().selectTerritory(dest);
        getGpc().performAction();
        assertEquals(SELECTING_UNITS_QUANTITY, getGpc().getInnerStatePhaseDescription());

        // invalid: zero
        getGpc().setUnitsToUse(0);
        getGpc().performAction();
        assertEquals(SELECTING_UNITS_QUANTITY, getGpc().getInnerStatePhaseDescription());

        // invalid: >= srcUnits
        getGpc().setUnitsToUse(src.getUnits());
        getGpc().performAction();
        assertEquals(SELECTING_UNITS_QUANTITY, getGpc().getInnerStatePhaseDescription());

        // valid
        getGpc().setUnitsToUse(2);
        getGpc().performAction();
        assertEquals("Executing the movement", getGpc().getInnerStatePhaseDescription());
    }

    @Test
    @DisplayName("6) Executing movement transfers units and allows loop or end phase")
    void executingMovementTransfersUnitsAndAllowsLoop() {
        final Territory src = getGm().getTerritory("T1").get();
        final Territory dest = getGm().getTerritory("T2").get();
        src.addUnits(3);

        // reach execute
        getGpc().selectTerritory(src);
        getGpc().performAction();
        getGpc().selectTerritory(dest);
        getGpc().performAction();
        getGpc().setUnitsToUse(2);
        getGpc().performAction();
        assertEquals("Executing the movement", getGpc().getInnerStatePhaseDescription());

        final int srcBefore = src.getUnits();
        final int destBefore = dest.getUnits();

        // performAction should execute movement
        getGpc().performAction();
        assertEquals(srcBefore - 2, src.getUnits());
        assertEquals(destBefore + 2, dest.getUnits());

        // After movement, still in Executing the movement (allow more moves)
        assertEquals("Executing the movement", getGpc().getInnerStatePhaseDescription());

        // Next overall phase should be advanced via nextPhase()
        getGpc().nextPhase();
        assertEquals(COMBO, getGpc().getStateDescription());
    }
}
