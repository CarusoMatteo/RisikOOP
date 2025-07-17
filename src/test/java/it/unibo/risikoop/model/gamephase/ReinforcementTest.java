package it.unibo.risikoop.model.gamephase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.risikoop.controller.implementations.GamePhaseControllerImpl;
import it.unibo.risikoop.controller.implementations.logicgame.LogicReinforcementCalculatorImpl;
import it.unibo.risikoop.controller.interfaces.GamePhaseController;
import it.unibo.risikoop.controller.interfaces.logicgame.LogicReinforcementCalculator;
import it.unibo.risikoop.model.implementations.Color;
import it.unibo.risikoop.model.implementations.ContinentImpl;
import it.unibo.risikoop.model.implementations.GameManagerImpl;
import it.unibo.risikoop.model.implementations.TerritoryImpl;
import it.unibo.risikoop.model.implementations.TurnManagerImpl;
import it.unibo.risikoop.model.implementations.gamephase.ReinforcementPhase;
import it.unibo.risikoop.model.interfaces.Continent;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.TurnManager;
import it.unibo.risikoop.model.interfaces.gamephase.GamePhase;

class ReinforcementTest {

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

        gm.setContinents(createContinents());

        tm = new TurnManagerImpl(gm.getPlayers());
        gpc = new GamePhaseControllerImpl(List.of(), tm, gm);
        gp = new ReinforcementPhase(gm, gpc);
        logic = new LogicReinforcementCalculatorImpl(gm, tm);
    }

    @Test
    void checkUnitsToPlayer() {
        var p = tm.getCurrentPlayer();
        assertEquals("Alice", p.getName());
        p.addTerritory(territories.get(0));
        assertEquals(1, logic.calcPlayerUnits());

        p.addTerritories(List.of(
                territories.get(1),
                territories.get(2),
                territories.get(3)));
        assertEquals(1 + gm.getContinent("C1").get().getUnitReward(), logic.calcPlayerUnits());

        for (int i = 4; i < NUM_TERRITORIES; i++) {
            p.addTerritory(territories.get(i));
        }

        assertEquals(
                NUM_TERRITORIES / 3 +
                        gm.getContinent("C1").get().getUnitReward() +
                        gm.getContinent("C2").get().getUnitReward(),
                logic.calcPlayerUnits());
    }

    @Test
    void placeAllUnitsForEachPlayer() {
        assignAllTerritoriesToPlayers();

        var u = gpc.getTurnManager().getCurrentPlayer().getUnitsToPlace();
        gp.initializationPhase();
        assertEquals(18, gpc.getTurnManager().getCurrentPlayer().getUnitsToPlace());

    }

    // --- Helpers ---

    private void placeUnitsOnTerritory(Player player, Territory t, int count) {
        IntStream.rangeClosed(1, count).forEach(i -> {
            // performAction must not switch player prematurely
            gp.performAction();
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

    private void assignAllTerritoriesToPlayers() {
        var p = gm.getPlayers().getFirst();
        p.addTerritories(createTerritories());
    }

    private void assignSomeTerritoriesToPlayers(int n) {
        var p = gm.getPlayers().getFirst();
        int min = Math.min(NUM_TERRITORIES, n);
        for (int i = 0; i < min; i++) {
            p.addTerritory(gm.getTerritory("T" + i).get());
        }
    }

    private Set<Continent> createContinents() {

        var c1 = new ContinentImpl("C1", 3);

        c1.addTerritory(territories.get(0));
        c1.addTerritory(territories.get(1));
        c1.addTerritory(territories.get(2));

        var c2 = new ContinentImpl("C2", 5);

        c2.addTerritory(territories.get(3));
        c2.addTerritory(territories.get(4));
        c2.addTerritory(territories.get(5));

        return Set.of(c1, c2);
    }

}
