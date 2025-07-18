package it.unibo.risikoop.model.gamephase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.risikoop.controller.implementations.GamePhaseControllerImpl;
import it.unibo.risikoop.controller.implementations.logicgame.LogicCalcInitialUnitsImpl;
import it.unibo.risikoop.controller.interfaces.GamePhaseController;
import it.unibo.risikoop.controller.interfaces.logicgame.LogicReinforcementCalculator;
import it.unibo.risikoop.model.implementations.Color;
import it.unibo.risikoop.model.implementations.GameManagerImpl;
import it.unibo.risikoop.model.implementations.TerritoryImpl;
import it.unibo.risikoop.model.implementations.TurnManagerImpl;
import it.unibo.risikoop.model.implementations.gamephase.InitialReinforcementPhase;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.TurnManager;
import it.unibo.risikoop.model.interfaces.gamephase.GamePhase;
import it.unibo.risikoop.model.interfaces.gamephase.PhaseWithActionToPerforme;

class InitialReinforcementTest {

    private static final List<String> NAMES = List.of("Alice", "Bob", "Carol");
    private static final int NUM_TERRITORIES = 9;

    private GameManager gm;
    private TurnManager tm;
    private GamePhaseController gpc;
    private GamePhase gp;
    private LogicReinforcementCalculator logic;
    private List<Territory> territories;

    @BeforeEach
    void setUp() {
        gm = new GameManagerImpl();

        for (int i = 0; i < NAMES.size(); i++) {
            gm.addPlayer(NAMES.get(i), new Color(i, 0, 0));
        }

        territories = createTerritories();
        gm.setWorldMap(createTestMap());
        assignTerritoriesToPlayers();

        tm = new TurnManagerImpl(gm.getPlayers());
        gpc = new GamePhaseControllerImpl(List.of(), tm, gm);
        gp = new InitialReinforcementPhase(gpc, gm);
        logic = new LogicCalcInitialUnitsImpl(gm);
    }

    @Test
    void checkFirstPlayerUnits() {
        var p = tm.getCurrentPlayer();
        assertEquals("Alice", p.getName());
        assertEquals(logic.calcPlayerUnits(), p.getUnitsToPlace());
    }

    @Test
    void placeAllUnitsForEachPlayer() {
        int expectedUnits = logic.calcPlayerUnits();

        for (int playerIndex = 0; playerIndex < NAMES.size(); playerIndex++) {
            String playerName = NAMES.get(playerIndex);
            Territory homeTerritory = territories.get(playerIndex * 3);

            var p = tm.getCurrentPlayer();
            assertEquals(playerName, p.getName());
            assertFalse(gp.isComplete());
            assertEquals(expectedUnits, p.getUnitsToPlace());

            // place all units
            placeUnitsOnTerritory(p, homeTerritory, expectedUnits);

            // cannot place extra units
            gp.selectTerritory(homeTerritory);
            assertEquals(0, p.getUnitsToPlace());
            assertEquals(expectedUnits, homeTerritory.getUnits());

            // clicking foreign territory does nothing
            gp.selectTerritory(territories.get((playerIndex + 1) % 3 * 3));
            assertEquals(0, p.getUnitsToPlace());

            // advance to next player
            // gp.performAction();
            currentPhaseAs(PhaseWithActionToPerforme.class)
                    .ifPresent(PhaseWithActionToPerforme::performAction);
        }

        // After last player, phase should be complete and still Carol's turn
        assertTrue(tm.isLastPlayer());
        assertTrue(gp.isComplete());
        assertEquals("Carol", tm.getCurrentPlayer().getName());
    }

    // --- Helpers ---

    private void placeUnitsOnTerritory(Player player, Territory t, int count) {
        IntStream.rangeClosed(1, count).forEach(i -> {
            // performAction must not switch player prematurely
            currentPhaseAs(PhaseWithActionToPerforme.class)
                    .ifPresent(PhaseWithActionToPerforme::performAction);
            assertEquals(player.getName(), tm.getCurrentPlayer().getName());

            gp.selectTerritory(t);
            assertEquals(count - i, player.getUnitsToPlace());
            assertEquals(i, t.getUnits());
        });
    }

    private List<Territory> createTerritories() {
        var list = new ArrayList<Territory>();
        for (int i = 1; i <= NUM_TERRITORIES; i++) {
            list.add(new TerritoryImpl(gm, "T" + i));
        }
        return list;
    }

    private Graph createTestMap() {
        Graph graph = new MultiGraph("map", false, true);
        var names = territories.stream().map(Territory::getName).toList();
        graph.addEdge("1", names.get(0), names.get(5));
        graph.addEdge("2", names.get(2), names.get(4));
        graph.addEdge("3", names.get(3), names.get(1));
        graph.addEdge("4", names.get(7), names.get(8));
        graph.addEdge("5", names.get(8), names.get(2));
        graph.addEdge("6", names.get(6), names.get(2));
        return graph;
    }

    private void assignTerritoriesToPlayers() {
        var players = gm.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            var p = players.get(i);
            for (int j = 0; j < 3; j++) {
                p.addTerritory(gm.getTerritory("T" + (i * 3 + j + 1)).get());
            }
        }
    }

    private <T> Optional<T> currentPhaseAs(Class<T> iface) {
        if (iface.isInstance(gp)) {
            return Optional.of(iface.cast(gp));
        }
        return Optional.empty();
    }
}
