package it.unibo.risikoop.model.gameflowtest;

import java.util.List;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.junit.jupiter.api.BeforeEach;

import it.unibo.risikoop.controller.implementations.GamePhaseControllerImpl;
import it.unibo.risikoop.controller.interfaces.GamePhaseController;
import it.unibo.risikoop.controller.implementations.logicgame.LogicCalcInitialUnitsImpl;
import it.unibo.risikoop.controller.implementations.logicgame.LogicReinforcementCalculatorImpl;
import it.unibo.risikoop.model.implementations.Color;
import it.unibo.risikoop.model.implementations.GameManagerImpl;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.TurnManager;
import it.unibo.risikoop.model.implementations.TurnManagerImpl;

/**
 * Base test fixture for all GameFlow (GamePhaseController) tests.
 * Sets up a GameManager with two players and four fully-connected territories,
 * initializes TurnManager and GamePhaseController, and provides common logic
 * calculators.
 */
abstract class AbstractGamePhaseTest {

    protected static final List<String> PLAYER_NAMES = List.of("Alice", "Bob");
    protected static final List<String> TERRITORY_NAMES = List.of("T1", "T2", "T3", "T4");

    protected static final String INITIAL_REINFORCEMENT = "Fase di rinforzo iniziale";
    protected static final String REINFORCEMENT = "Fase di rinforzo";
    protected static final String COMBO = "Fase di gestione combo";
    protected static final String ATTACK = "Fase di gestione attacchi";
    protected static final String MOVEMENT = "Fase di gestione spostamenti";

    protected GameManager gm;
    protected TurnManager tm;
    protected GamePhaseController gpc;
    protected LogicCalcInitialUnitsImpl initialLogic;
    protected LogicReinforcementCalculatorImpl reinforcementLogic;

    @BeforeEach
    void setUp() {
        // 1) Create GameManager and add two players
        gm = new GameManagerImpl();
        for (int i = 0; i < PLAYER_NAMES.size(); i++) {
            gm.addPlayer(PLAYER_NAMES.get(i), new Color(i, 0, 0));
        }

        // 2) Build a fully-connected map of four territories
        Graph map = new MultiGraph("testMap", false, true);
        TERRITORY_NAMES.forEach(map::addNode);
        int edgeId = 0;
        for (int i = 0; i < TERRITORY_NAMES.size(); i++) {
            for (int j = 0; j < TERRITORY_NAMES.size(); j++) {
                if (i == j)
                    continue;
                map.addEdge("e" + (edgeId++), TERRITORY_NAMES.get(i), TERRITORY_NAMES.get(j), true);
            }
        }
        gm.setWorldMap(map);

        // 3) Assign two territories to each player and set ownership
        var players = gm.getPlayers();
        players.get(0).addTerritory(gm.getTerritory("T1").get());
        players.get(0).addTerritory(gm.getTerritory("T2").get());
        players.get(1).addTerritory(gm.getTerritory("T3").get());
        players.get(1).addTerritory(gm.getTerritory("T4").get());
        gm.getTerritory("T1").get().setOwner(players.get(0));
        gm.getTerritory("T2").get().setOwner(players.get(0));
        gm.getTerritory("T3").get().setOwner(players.get(1));
        gm.getTerritory("T4").get().setOwner(players.get(1));

        // 4) Initialize TurnManager and GamePhaseController
        tm = new TurnManagerImpl(players);
        gpc = new GamePhaseControllerImpl(List.of(), tm, gm);

        // 5) Prepare logic calculators for assertions
        initialLogic = new LogicCalcInitialUnitsImpl(gm);
        reinforcementLogic = new LogicReinforcementCalculatorImpl(gm, tm);
    }
}
