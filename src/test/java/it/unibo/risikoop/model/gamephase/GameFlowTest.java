package it.unibo.risikoop.model.gamephase;

import java.util.List;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.risikoop.controller.implementations.GamePhaseControllerImpl;
import it.unibo.risikoop.controller.interfaces.GamePhaseController;
import it.unibo.risikoop.model.implementations.Color;
import it.unibo.risikoop.model.implementations.GameManagerImpl;
import it.unibo.risikoop.model.implementations.TerritoryImpl;
import it.unibo.risikoop.model.implementations.TurnManagerImpl;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.TurnManager;

/**
 * Unit tests for the {@link TurnManagerImpl} class.
 * <p>
 * Ensures that turn rotation logic behaves correctly, including:
 * <ul>
 * <li>initial current player selection</li>
 * <li>advancing to next player in order</li>
 * <li>detecting the start of a new round after all players have taken a
 * turn</li>
 * <li>skipping eliminated players (test currently commented out)</li>
 * </ul>
 * </p>
 */
class GameFlowTest {

    // evita literal duplicati
    private static final String ALICE = "Alice";
    private static final String BOB = "Bob";
    private static final String CAROL = "Carol";
    private static final String INITIAL_REINFORCEMENT = "Fase di rinforzo iniziale";
    private static final String REINFORCEMENT = "Fase di rinforzo";
    private static final String COMBO = "Fase di gestione combo";
    private static final String ATTACK = "Fase di gestione attacchi";
    private static final String MOVEMENT = "Fase di gestione spostamenti";

    private TurnManager turnManager;
    private GamePhaseController gpc;
    private List<Territory> territories;
    private GameManager gameManager;

    /**
     * Sets up a {@link GameManagerImpl} with three players and assigns one
     * territory to each. Then initializes a {@link TurnManagerImpl}.
     */
    @BeforeEach
    void setUp() {
        // gameManager non serve come campo: lo usiamo solo qui
        gameManager = new GameManagerImpl();
        gameManager.addPlayer(ALICE, new Color(0, 0, 0));
        gameManager.addPlayer(BOB, new Color(1, 0, 0));
        gameManager.addPlayer(CAROL, new Color(2, 0, 0));

        final var players = gameManager.getPlayers();
        territories = List.of(
                new TerritoryImpl(gameManager, "T1"),
                new TerritoryImpl(gameManager, "T2"),
                new TerritoryImpl(gameManager, "T3"),
                new TerritoryImpl(gameManager, "T4"),
                new TerritoryImpl(gameManager, "T5"),
                new TerritoryImpl(gameManager, "T6"),
                new TerritoryImpl(gameManager, "T7"),
                new TerritoryImpl(gameManager, "T8"),
                new TerritoryImpl(gameManager, "T9"));
        final Graph graph = createTestMap();
        gameManager.setWorldMap(graph);
        players.get(0).addTerritory(gameManager.getTerritory("T1").get());
        players.get(1).addTerritory(gameManager.getTerritory("T2").get());
        players.get(2).addTerritory(gameManager.getTerritory("T3").get());
        players.get(0).addTerritory(gameManager.getTerritory("T4").get());
        players.get(1).addTerritory(gameManager.getTerritory("T5").get());
        players.get(2).addTerritory(gameManager.getTerritory("T6").get());
        players.get(0).addTerritory(gameManager.getTerritory("T7").get());
        players.get(1).addTerritory(gameManager.getTerritory("T8").get());
        players.get(2).addTerritory(gameManager.getTerritory("T9").get());

        turnManager = new TurnManagerImpl(players);
        gpc = new GamePhaseControllerImpl(List.of(), turnManager, gameManager);
    }

    private Graph createTestMap() {
        final List<String> territoriesName = List.of(
                "T1",
                "T2",
                "T3",
                "T4",
                "T5",
                "T6",
                "T7",
                "T8",
                "T9");
        Graph graph = new MultiGraph(ALICE, false, true);
        graph.addEdge("1", territoriesName.get(0), territoriesName.get(5));
        graph.addEdge("2", territoriesName.get(2), territoriesName.get(4));
        graph.addEdge("3", territoriesName.get(3), territoriesName.get(1));
        graph.addEdge("6", territoriesName.get(7), territoriesName.get(8));
        graph.addEdge("7", territoriesName.get(8), territoriesName.get(2));
        graph.addEdge("7", territoriesName.get(6), territoriesName.get(2));
        return graph;
    }

    @Test
    void testCurrentPlayer() {
        assertEquals(ALICE, turnManager.getCurrentPlayer().getName());
    }

    @Test
    void testNextPlayerOrder() {
        assertEquals(ALICE, turnManager.getCurrentPlayer().getName());
        assertFalse(turnManager.isNewRound());

        assertEquals(BOB, turnManager.nextPlayer().getName());
        assertFalse(turnManager.isNewRound());

        assertEquals(CAROL, turnManager.nextPlayer().getName());
        assertFalse(turnManager.isNewRound());

        assertEquals(ALICE, turnManager.nextPlayer().getName());
        assertTrue(turnManager.isNewRound());

        assertEquals(BOB, turnManager.nextPlayer().getName());
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

    @Test
    void testInitialReinforcementState() {
        testFirstphase();

    }

    private void testFirstphase() {
        /* first player turn */
        assertEquals(INITIAL_REINFORCEMENT, gpc.getStateDescription());
        assertEquals(gameManager.getPlayers().get(0), turnManager.getCurrentPlayer());
        assertEquals(7, gameManager.getPlayers().get(0).getUnitsToPlace());
        assertEquals(0, territories.get(0).getUnits());
        gpc.selectTerritory(territories.get(0));
        gpc.selectTerritory(territories.get(0));
        gpc.selectTerritory(territories.get(3));
        gpc.selectTerritory(territories.get(3));
        gpc.selectTerritory(territories.get(6));
        gpc.selectTerritory(territories.get(6));
        gpc.selectTerritory(territories.get(6));
        gpc.selectTerritory(territories.get(6));
        assertEquals(2, territories.get(0).getUnits());
        assertEquals(2, territories.get(3).getUnits());
        assertEquals(3, territories.get(6).getUnits());
        /* second player turn */
        gpc.performAction();
        assertEquals(INITIAL_REINFORCEMENT, gpc.getStateDescription());
        assertEquals(gameManager.getPlayers().get(1), turnManager.getCurrentPlayer());
        assertEquals(7, gameManager.getPlayers().get(1).getUnitsToPlace());
        gpc.selectTerritory(territories.get(1));
        gpc.selectTerritory(territories.get(1));
        gpc.selectTerritory(territories.get(4));
        gpc.selectTerritory(territories.get(4));
        gpc.selectTerritory(territories.get(7));
        gpc.selectTerritory(territories.get(7));
        gpc.selectTerritory(territories.get(7));
        assertEquals(2, territories.get(1).getUnits());
        assertEquals(2, territories.get(4).getUnits());
        assertEquals(3, territories.get(7).getUnits());
        /* third player turn */
        gpc.performAction();
        assertEquals(INITIAL_REINFORCEMENT, gpc.getStateDescription());
        assertEquals(gameManager.getPlayers().get(2), turnManager.getCurrentPlayer());
        assertEquals(7, gameManager.getPlayers().get(2).getUnitsToPlace());
        gpc.selectTerritory(territories.get(2));
        gpc.selectTerritory(territories.get(2));
        gpc.selectTerritory(territories.get(5));
        gpc.selectTerritory(territories.get(5));
        gpc.selectTerritory(territories.get(8));
        gpc.selectTerritory(territories.get(8));
        gpc.selectTerritory(territories.get(8));
        assertEquals(2, territories.get(2).getUnits());
        assertEquals(2, territories.get(5).getUnits());
        assertEquals(3, territories.get(8).getUnits());
    }

    @Test
    void testreinforcementState() {
        testFirstphase();
        gpc.performAction();
        gpc.performAction();
        gpc.performAction();
        assertEquals(gameManager.getPlayers().get(0), turnManager.getCurrentPlayer());
        gpc.nextPhase();
        assertEquals(gameManager.getPlayers().get(0), turnManager.getCurrentPlayer());
        assertEquals(REINFORCEMENT, gpc.getStateDescription());
        assertEquals(7, gameManager.getPlayers().get(0).getUnitsToPlace());
        for (int i = 0; i < 9; i++) {
            gpc.selectTerritory(territories.get(0));
        }
        assertEquals(9, territories.get(0).getUnits());
        gpc.performAction();
        assertEquals(gameManager.getPlayers().get(1), turnManager.getCurrentPlayer());
        assertEquals(REINFORCEMENT, gpc.getStateDescription());
        assertEquals(7, gameManager.getPlayers().get(1).getUnitsToPlace());
        for (int i = 0; i < 8; i++) {
            gpc.selectTerritory(territories.get(1));
        }
        assertEquals(9, territories.get(1).getUnits());

    }
}
