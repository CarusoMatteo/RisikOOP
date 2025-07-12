package it.unibo.risikoop.model.gamephase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.risikoop.model.implementations.Color;
import it.unibo.risikoop.model.implementations.GameManagerImpl;
import it.unibo.risikoop.model.implementations.TerritoryImpl;
import it.unibo.risikoop.model.implementations.TurnManagerImpl;
import it.unibo.risikoop.model.interfaces.TurnManager;

/**
 * Unit tests for the {@link TurnManagerImpl} class.
 * <p>
 * Ensures that turn rotation logic behaves correctly, including:
 * <ul>
 *   <li>initial current player selection</li>
 *   <li>advancing to next player in order</li>
 *   <li>detecting the start of a new round after all players have taken a turn</li>
 *   <li>skipping eliminated players (test currently commented out)</li>
 * </ul>
 * </p>
 */
class TurnManagerTest {


    // evita literal duplicati
    private static final String ALICE = "Alice";
    private static final String BOB   = "Bob";
    private static final String CAROL = "Carol";

    private TurnManager turnManager;

    /**
     * Sets up a {@link GameManagerImpl} with three players and assigns one
     * territory to each. Then initializes a {@link TurnManagerImpl}.
     */
    @BeforeEach
    void setUp() {
        // gameManager non serve come campo: lo usiamo solo qui
        final var gameManager = new GameManagerImpl();
        gameManager.addPlayer(ALICE, new Color(0, 0, 0));
        gameManager.addPlayer(BOB,   new Color(1, 0, 0));
        gameManager.addPlayer(CAROL, new Color(2, 0, 0));

        final var players = gameManager.getPlayers();
        final var territories = List.of(
            new TerritoryImpl(gameManager, "T1"),
            new TerritoryImpl(gameManager, "T2"),
            new TerritoryImpl(gameManager, "T3"),
            new TerritoryImpl(gameManager, "T4"),
            new TerritoryImpl(gameManager, "T5")
        );
        players.get(0).addTerritory(territories.get(0));
        players.get(1).addTerritory(territories.get(1));
        players.get(2).addTerritory(territories.get(2));

        turnManager = new TurnManagerImpl(players);
    }

    @Test
    void testCurrentPlayer() {
        assertEquals(ALICE, turnManager.getCurrentPlayer().getName());
    }

    @Test
    void testNextPlayerOrder() {
        assertEquals(ALICE, turnManager.getCurrentPlayer().getName());
        assertFalse(turnManager.isNewRound());

        assertEquals(BOB,   turnManager.nextPlayer().getName());
        assertFalse(turnManager.isNewRound());

        assertEquals(CAROL, turnManager.nextPlayer().getName());
        assertFalse(turnManager.isNewRound());

        assertEquals(ALICE, turnManager.nextPlayer().getName());
        assertTrue(turnManager.isNewRound());

        assertEquals(BOB,   turnManager.nextPlayer().getName());
        assertFalse(turnManager.isNewRound());
    }

    @Test
    void testSkipEliminatedPlayers() {
        // (test commentato)
    }

    @Test
    void testIsNewRound() {
        turnManager.nextPlayer(); // Bob
        turnManager.nextPlayer(); // Carol
        turnManager.nextPlayer(); // Alice

        assertTrue(turnManager.isNewRound());
    }
}
