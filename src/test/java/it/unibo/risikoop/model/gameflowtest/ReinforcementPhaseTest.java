package it.unibo.risikoop.model.gameflowtest;

// package it.unibo.risikoop.model.gamephase.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import it.unibo.risikoop.controller.interfaces.GamePhaseController.PhaseKey;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;

/**
 * Tests for the Reinforcement phase of the game flow.
 */
@DisplayName("Reinforcement Phase")
class ReinforcementPhaseTest extends AbstractGamePhaseTest {

    @Override
    protected PhaseKey startPhase() {
        return PhaseKey.REINFORCEMENT;
    }

    @Test
    @DisplayName("1) Selecting enemy territory does nothing")
    void selectingEnemyTerritoryDoesNothing() {
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
        assertEquals(REINFORCEMENT, gpc.getStateDescription(),
                "State should remain Reinforcement");
        assertEquals(current, tm.getCurrentPlayer(),
                "Current player should remain unchanged");
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
        assertEquals(REINFORCEMENT, gpc.getStateDescription(),
                "State should remain Reinforcement");
        assertEquals(current, tm.getCurrentPlayer(),
                "Current player should remain unchanged");
    }

    @Test
    @DisplayName("3) After full placement, clicks do nothing")
    void afterFullPlacementClicksDoNothing() {
        // Arrange
        Player current = tm.getCurrentPlayer();
        Territory mine = gm.getTerritory("T1").get();
        Territory enemy = gm.getTerritory("T3").get();

        // Place all units on own territory
        while (current.getUnitsToPlace() > 0) {
            gpc.selectTerritory(mine);
        }
        int mineUnits = mine.getUnits();
        int enemyUnits = enemy.getUnits();

        // Act & Assert: clicks post-placement
        gpc.selectTerritory(mine);
        assertEquals(mineUnits, mine.getUnits(),
                "Own territory units should not change after full placement");
        gpc.selectTerritory(enemy);
        assertEquals(enemyUnits, enemy.getUnits(),
                "Enemy territory units should not change after full placement");
        assertEquals(0, current.getUnitsToPlace(),
                "Units to place should remain zero");
    }

    @Test
    @DisplayName("4) nextPhase before full placement does nothing")
    void nextPhaseBeforeFullPlacementDoesNothing() {
        // Arrange
        Player current = tm.getCurrentPlayer();

        // Act
        gpc.nextPhase();

        // Assert
        assertEquals(REINFORCEMENT, gpc.getStateDescription(),
                "State should remain Reinforcement");
        assertEquals(current, tm.getCurrentPlayer(),
                "Current player should remain unchanged");
    }

    @Test
    @DisplayName("5) nextPhase after full placement advances to Attack phase")
    void nextPhaseAfterFullPlacementAdvancesToAttack() {
        // Arrange
        Player current = tm.getCurrentPlayer();
        Territory mine = gm.getTerritory("T1").get();

        // Place all units
        while (current.getUnitsToPlace() > 0) {
            gpc.selectTerritory(mine);
        }

        // Act
        gpc.nextPhase();

        // Assert
        assertEquals(ATTACK, gpc.getStateDescription(),
                "State should advance to Attack");
        assertEquals(current, tm.getCurrentPlayer(),
                "Current player should remain unchanged");
    }
}
