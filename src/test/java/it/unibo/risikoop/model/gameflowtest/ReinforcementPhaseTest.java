package it.unibo.risikoop.model.gameflowtest;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    private static final String CURRENT_PLAYER_SHOULD_REMAIN_UNCHANGED = "Current player should remain unchanged";

    @Override
    protected PhaseKey startPhase() {
        return PhaseKey.REINFORCEMENT;
    }

    @Test
    @DisplayName("1) Selecting enemy territory does nothing")
    void selectingEnemyTerritoryDoesNothing() {
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
        assertEquals(REINFORCEMENT, getGpc().getStateDescription(),
                "State should remain Reinforcement");
        assertEquals(current, getTm().getCurrentPlayer(),
                CURRENT_PLAYER_SHOULD_REMAIN_UNCHANGED);
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
        assertEquals(REINFORCEMENT, getGpc().getStateDescription(),
                "State should remain Reinforcement");
        assertEquals(current, getTm().getCurrentPlayer(),
                CURRENT_PLAYER_SHOULD_REMAIN_UNCHANGED);
    }

    @Test
    @DisplayName("3) After full placement, clicks do nothing")
    void afterFullPlacementClicksDoNothing() {
        // Arrange
        final Player current = getTm().getCurrentPlayer();
        final Territory mine = getGm().getTerritory("T1").get();
        final Territory enemy = getGm().getTerritory("T3").get();

        // Place all units on own territory
        while (current.getUnitsToPlace() > 0) {
            getGpc().selectTerritory(mine);
        }
        final int mineUnits = mine.getUnits();
        final int enemyUnits = enemy.getUnits();

        // Act & Assert: clicks post-placement
        getGpc().selectTerritory(mine);
        assertEquals(mineUnits, mine.getUnits(),
                "Own territory units should not change after full placement");
        getGpc().selectTerritory(enemy);
        assertEquals(enemyUnits, enemy.getUnits(),
                "Enemy territory units should not change after full placement");
        assertEquals(0, current.getUnitsToPlace(),
                "Units to place should remain zero");
    }

    @Test
    @DisplayName("4) nextPhase before full placement does nothing")
    void nextPhaseBeforeFullPlacementDoesNothing() {
        // Arrange
        final Player current = getTm().getCurrentPlayer();

        // Act
        getGpc().nextPhase();

        // Assert
        assertEquals(REINFORCEMENT, getGpc().getStateDescription(),
                "State should remain Reinforcement");
        assertEquals(current, getTm().getCurrentPlayer(),
                CURRENT_PLAYER_SHOULD_REMAIN_UNCHANGED);
    }

    @Test
    @DisplayName("5) nextPhase after full placement advances to Attack phase")
    void nextPhaseAfterFullPlacementAdvancesToAttack() {
        // Arrange
        final Player current = getTm().getCurrentPlayer();
        final Territory mine = getGm().getTerritory("T1").get();

        // Place all units
        while (current.getUnitsToPlace() > 0) {
            getGpc().selectTerritory(mine);
        }

        // Act
        getGpc().nextPhase();

        // Assert
        assertEquals(ATTACK, getGpc().getStateDescription(),
                "State should advance to Attack");
        assertEquals(current, getTm().getCurrentPlayer(),
                CURRENT_PLAYER_SHOULD_REMAIN_UNCHANGED);
    }
}
