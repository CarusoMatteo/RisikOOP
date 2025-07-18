package it.unibo.risikoop.model.gameflowtest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;

/**
 * Tests for the Initial Reinforcement phase of the game flow.
 */
@DisplayName("Initial Reinforcement Phase")
class InitialReinforcementPhaseTest extends AbstractGamePhaseTest {

    @Test
    @DisplayName("1) Clicking on enemy territory does nothing")
    void clickingEnemyTerritoryDoesNothing() {
        // Arrange
        Player current = tm.getCurrentPlayer();
        Territory enemyTerr = gm.getTerritory("T3").get();
        int beforeToPlace = current.getUnitsToPlace();
        int beforeUnits = enemyTerr.getUnits();

        // Act
        gpc.selectTerritory(enemyTerr);

        // Assert
        assertEquals(beforeToPlace, current.getUnitsToPlace(),
                "Units to place should remain unchanged");
        assertEquals(beforeUnits, enemyTerr.getUnits(),
                "Enemy territory units should remain unchanged");
        assertEquals(current, tm.getCurrentPlayer(),
                "Current player should stay the same");

        // Try to change player but it should not change
        gpc.performAction();
        assertEquals(INITIAL_REINFORCEMENT, gpc.getStateDescription(),
                "State should remain initial reinforcement");
    }

    @Test
    @DisplayName("2) Selecting own territory places one unit")
    void selectingOwnTerritoryPlacesOneUnit() {
        // Arrange
        Player current = tm.getCurrentPlayer();
        Territory ownTerr = gm.getTerritory("T1").get();
        int beforeToPlace = current.getUnitsToPlace();
        int beforeTerrUnits = ownTerr.getUnits();

        // Act
        gpc.selectTerritory(ownTerr);

        // Assert
        assertEquals(beforeToPlace - 1, current.getUnitsToPlace(),
                "Units to place should decrement by one");
        assertEquals(beforeTerrUnits + 1, ownTerr.getUnits(),
                "Own territory units should increment by one");
        assertEquals(current, tm.getCurrentPlayer(),
                "Current player should stay the same");
    }

    @Test
    @DisplayName("3) After all units placed, clicks do nothing")
    void afterAllUnitsPlacedClicksDoNothing() {
        // Arrange
        Player current = tm.getCurrentPlayer();
        Territory mine = gm.getTerritory("T1").get();
        Territory enemy = gm.getTerritory("T3").get();

        // Place all units
        while (current.getUnitsToPlace() > 0) {
            gpc.selectTerritory(mine);
        }
        int mineUnits = mine.getUnits();
        int enemyUnits = enemy.getUnits();

        // Act & Assert: clicks on mine
        gpc.selectTerritory(mine);
        assertEquals(mineUnits, mine.getUnits(),
                "Mine territory units should not change after full placement");
        // clicks on enemy
        gpc.selectTerritory(enemy);
        assertEquals(enemyUnits, enemy.getUnits(),
                "Enemy territory units should not change after full placement");
        assertEquals(0, current.getUnitsToPlace(),
                "Units to place remain zero");
    }

    @Test
    @DisplayName("4) Turn change only after full placement")
    void turnChangeOnlyAfterFullPlacement() {
        // Arrange
        Player current = tm.getCurrentPlayer();

        // Act & Assert: premature performAction
        gpc.performAction();
        assertEquals(current, tm.getCurrentPlayer(),
                "Turn should not change before full placement");

        // Complete placement
        Territory mine = gm.getTerritory("T1").get();
        while (current.getUnitsToPlace() > 0) {
            gpc.selectTerritory(mine);
        }
        // Now perform action to change turn
        gpc.performAction();
        assertNotEquals(current, tm.getCurrentPlayer(),
                "Turn should change after full placement");
    }
}
