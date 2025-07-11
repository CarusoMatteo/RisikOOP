package it.unibo.risikoop.model.gamephase;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.risikoop.model.implementations.Color;
import it.unibo.risikoop.model.implementations.GameManagerImpl;
import it.unibo.risikoop.model.implementations.PlayerImpl;
import it.unibo.risikoop.model.implementations.TurnManagerImpl;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.TurnManager;

public class TurnManagerTest {

    private TurnManager turnManager;
    private GameManager gameManager;

    @BeforeEach
    void setUp() {
        gameManager = new GameManagerImpl();
        gameManager.addPlayer("Alice", new Color(0,0,0));
        gameManager.addPlayer("Bolb", new Color(1,0,0));
        gameManager.addPlayer("Carol", new Color(2,0,0));
        turnManager = new TurnManagerImpl(gameManager.getPlayers());
    }

    @Test
    void testCurrentPlayer() {
        assertEquals("Alice", turnManager.getCurrentPlayer().getName());
    }

    @Test
    void testNextPlayerOrder() {
        assertEquals("Bob", turnManager.nextPlayer().getName());
        assertEquals("Carol", turnManager.nextPlayer().getName());
        assertEquals("Alice", turnManager.nextPlayer().getName());
    }

    @Test
    void testSkipEliminatedPlayers() {
        // ((DummyPlayer) players.get(1)).eliminate(); // Elimina Bob
        assertEquals("Bob", turnManager.nextPlayer().getName()); // Bob eliminato, ma viene saltato
        assertEquals("Carol", turnManager.nextPlayer().getName());
        assertEquals("Alice", turnManager.nextPlayer().getName());
        assertEquals("Carol", turnManager.nextPlayer().getName());
    }

    @Test
    void testIsNewRound() {
        turnManager.nextPlayer(); // Bob
        turnManager.nextPlayer(); // Carol
        assertTrue(turnManager.isNewRound()); // Dopo Carol torna Alice, nuovo round
    }
}