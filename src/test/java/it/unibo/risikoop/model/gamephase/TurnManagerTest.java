package it.unibo.risikoop.model.gamephase;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.risikoop.model.implementations.Color;
import it.unibo.risikoop.model.implementations.GameManagerImpl;
import it.unibo.risikoop.model.implementations.PlayerImpl;
import it.unibo.risikoop.model.implementations.TerritoryImpl;
import it.unibo.risikoop.model.implementations.TurnManagerImpl;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.TurnManager;

public class TurnManagerTest {

    private TurnManager turnManager;
    private GameManager gameManager;

    @BeforeEach
    void setUp() {
        gameManager = new GameManagerImpl();

        gameManager.addPlayer("Alice", new Color(0, 0, 0));
        gameManager.addPlayer("Bob", new Color(1, 0, 0));
        gameManager.addPlayer("Carol", new Color(2, 0, 0));

        var players = gameManager.getPlayers();
        List<Territory> territories = List.of(
                new TerritoryImpl(gameManager, "T1"),
                new TerritoryImpl(gameManager, "T2"),
                new TerritoryImpl(gameManager, "T3"),
                new TerritoryImpl(gameManager, "T4"),
                new TerritoryImpl(gameManager, "T5"));

        players.get(0).addTerritory(territories.get(0));
        players.get(1).addTerritory(territories.get(1));
        players.get(2).addTerritory(territories.get(2));

        turnManager = new TurnManagerImpl(gameManager.getPlayers());
    }

    @Test
    void testCurrentPlayer() {
        assertEquals("Alice", turnManager.getCurrentPlayer().getName());
    }

    @Test
    void testNextPlayerOrder() {

        assertEquals("Alice", turnManager.getCurrentPlayer().getName());
        assertFalse(turnManager.isNewRound());

        assertEquals("Bob", turnManager.nextPlayer().getName());
        assertFalse(turnManager.isNewRound());

        assertEquals("Carol", turnManager.nextPlayer().getName());
        assertFalse(turnManager.isNewRound());

        assertEquals("Alice", turnManager.nextPlayer().getName());
        assertTrue(turnManager.isNewRound());

        assertEquals("Bob", turnManager.nextPlayer().getName());
        assertFalse(turnManager.isNewRound());
    }

    @Test
    void testSkipEliminatedPlayers() {
        // ((DummyPlayer) players.get(1)).eliminate(); // Elimina Bob
        // Player bob = gameManager.getPlayers().get(1);

        // assertEquals("Bob", turnManager.nextPlayer().getName()); // Bob eliminato, ma
        // viene saltato
        // assertEquals("Carol", turnManager.nextPlayer().getName());
        // assertEquals("Alice", turnManager.nextPlayer().getName());
        // assertEquals("Carol", turnManager.nextPlayer().getName());
    }

    @Test
    void testIsNewRound() {
        turnManager.nextPlayer(); // Bob
        turnManager.nextPlayer(); // Carol
        turnManager.nextPlayer(); // Alice

        assertTrue(turnManager.isNewRound()); // Dopo Carol torna Alice, nuovo round
    }

}