package it.unibo.risikoop.model.gameflowtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import it.unibo.risikoop.controller.interfaces.GamePhaseController.PhaseKey;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;

/**
 * Tests for the Initial Reinforcement phase of the game flow.
 */
@DisplayName("Initial Reinforcement Phase")
class InitialReinforcementPhaseTest extends AbstractGamePhaseTest {

    @Override
    protected PhaseKey startPhase() {
        return PhaseKey.INITIAL_REINFORCEMENT;
    }

    @Test
    @DisplayName("1) Clicking on enemy territory does nothing")
    void clickingEnemyTerritoryDoesNothing() {
        // Arrange
        final Player current = getTm().getCurrentPlayer();
        final Territory enemyTerr = getGm().getTerritory("T3").get();
        final int beforeToPlace = current.getUnitsToPlace();
        final int beforeUnits = enemyTerr.getUnits();

        // Act
        getGpc().selectTerritory(enemyTerr);

        // Assert
        assertEquals(beforeToPlace, current.getUnitsToPlace(),
                "Units to place should remain unchanged");
        assertEquals(beforeUnits, enemyTerr.getUnits(),
                "Enemy territory units should remain unchanged");
        assertEquals(current, getTm().getCurrentPlayer(),
                "Current player should stay the same");

        // Try to change player but it should not change
        getGpc().performAction();
        assertEquals(INITIAL_REINFORCEMENT, getGpc().getStateDescription(),
                "State should remain initial reinforcement");
    }

    @Test
    @DisplayName("2) Selecting own territory places one unit")
    void selectingOwnTerritoryPlacesOneUnit() {
        // Arrange
        final Player current = getTm().getCurrentPlayer();
        final Territory ownTerr = getGm().getTerritory("T1").get();
        final int beforeToPlace = current.getUnitsToPlace();
        final int beforeTerrUnits = ownTerr.getUnits();

        // Act
        getGpc().selectTerritory(ownTerr);

        // Assert
        assertEquals(beforeToPlace - 1, current.getUnitsToPlace(),
                "Units to place should decrement by one");
        assertEquals(beforeTerrUnits + 1, ownTerr.getUnits(),
                "Own territory units should increment by one");
        assertEquals(current, getTm().getCurrentPlayer(),
                "Current player should stay the same");
    }

    @Test
    @DisplayName("3) After all units placed, clicks do nothing")
    void afterAllUnitsPlacedClicksDoNothing() {
        // Arrange
        final Player current = getTm().getCurrentPlayer();
        final Territory mine = getGm().getTerritory("T1").get();
        final Territory enemy = getGm().getTerritory("T3").get();

        // Place all units
        while (current.getUnitsToPlace() > 0) {
            getGpc().selectTerritory(mine);
        }
        final int mineUnits = mine.getUnits();
        final int enemyUnits = enemy.getUnits();

        // Act & Assert: clicks on mine
        getGpc().selectTerritory(mine);
        assertEquals(mineUnits, mine.getUnits(),
                "Mine territory units should not change after full placement");
        // clicks on enemy
        getGpc().selectTerritory(enemy);
        assertEquals(enemyUnits, enemy.getUnits(),
                "Enemy territory units should not change after full placement");
        assertEquals(0, current.getUnitsToPlace(),
                "Units to place remain zero");
    }

    @Test
    @DisplayName("4) Turn change only after full placement")
    void turnChangeOnlyAfterFullPlacement() {
        // Arrange
        final Player current = getTm().getCurrentPlayer();

        // Act & Assert: premature performAction
        getGpc().performAction();
        assertEquals(current, getTm().getCurrentPlayer(),
                "Turn should not change before full placement");

        // Complete placement
        final Territory mine = getGm().getTerritory("T1").get();
        while (current.getUnitsToPlace() > 0) {
            getGpc().selectTerritory(mine);
        }
        // Now perform action to change turn
        getGpc().performAction();
        assertNotEquals(current, getTm().getCurrentPlayer(),
                "Turn should change after full placement");
    }
}
