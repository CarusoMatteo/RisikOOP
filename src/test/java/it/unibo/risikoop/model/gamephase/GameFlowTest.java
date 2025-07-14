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
import it.unibo.risikoop.controller.implementations.logicgame.LogicCalcInitialUnitsImpl;
import it.unibo.risikoop.controller.implementations.logicgame.LogicReinforcementCalculatorImpl;
import it.unibo.risikoop.controller.interfaces.GamePhaseController;
import it.unibo.risikoop.controller.interfaces.logicgame.LogicReinforcementCalculator;
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
    private static final List<String> playerNames = List.of("Alice", "Bob");
    private static final String INITIAL_REINFORCEMENT = "Fase di rinforzo iniziale";
    private static final String REINFORCEMENT = "Fase di rinforzo";
    private static final String COMBO = "Fase di gestione combo";
    private static final String ATTACK = "Fase di gestione attacchi";
    private static final String MOVEMENT = "Fase di gestione spostamenti";

    private TurnManager turnManager;
    private GamePhaseController gpc;
    private List<Territory> territories;
    private GameManager gameManager;
    private LogicReinforcementCalculator initialLogic;
    private LogicReinforcementCalculator renforcementLogic;

    /**
     * Sets up a {@link GameManagerImpl} with three players and assigns one
     * territory to each. Then initializes a {@link TurnManagerImpl}.
     */
    @BeforeEach
    void setUp() {
        // gameManager non serve come campo: lo usiamo solo qui
        gameManager = new GameManagerImpl();
        for (int i = 0; i < playerNames.size(); i++) {
            String name = playerNames.get(i);
            gameManager.addPlayer(name, new Color(i, 0, 0));
        }
        // gameManager.addPlayer(playerName.get(0), new Color(0, 0, 0));
        // gameManager.addPlayer(playerName.get(1), new Color(1, 0, 0));

        final var players = gameManager.getPlayers();
        territories = List.of(
                new TerritoryImpl(gameManager, "T1"),
                new TerritoryImpl(gameManager, "T2"),
                new TerritoryImpl(gameManager, "T3"),
                new TerritoryImpl(gameManager, "T4"));
        final Graph graph = createTestMap();
        gameManager.setWorldMap(graph);
        players.get(0).addTerritory(gameManager.getTerritory("T1").get());
        players.get(0).addTerritory(gameManager.getTerritory("T2").get());
        players.get(1).addTerritory(gameManager.getTerritory("T3").get());
        players.get(1).addTerritory(gameManager.getTerritory("T4").get());

        gameManager.getTerritory("T1").get().setOwner(gameManager.getPlayers().get(0));
        gameManager.getTerritory("T2").get().setOwner(gameManager.getPlayers().get(0));
        gameManager.getTerritory("T3").get().setOwner(gameManager.getPlayers().get(1));
        gameManager.getTerritory("T4").get().setOwner(gameManager.getPlayers().get(1));

        turnManager = new TurnManagerImpl(players);
        gpc = new GamePhaseControllerImpl(List.of(), turnManager, gameManager);
        initialLogic = new LogicCalcInitialUnitsImpl(gameManager);
        renforcementLogic = new LogicReinforcementCalculatorImpl(gameManager, turnManager);
    }

    private Graph createTestMap() {
        final List<String> territoriesName = List.of(
                "T1",
                "T2",
                "T3",
                "T4");
        Graph graph = new MultiGraph(playerNames.getFirst(), false, true);
        graph.addEdge("1", territoriesName.get(0), territoriesName.get(1));
        graph.addEdge("2", territoriesName.get(1), territoriesName.get(2));
        graph.addEdge("3", territoriesName.get(2), territoriesName.get(3));
        graph.addEdge("4", territoriesName.get(3), territoriesName.get(0));
        return graph;
    }

    @Test
    void testCurrentPlayer() {
        assertEquals(playerNames.getFirst(), turnManager.getCurrentPlayer().getName());
    }

    @Test
    void flowTest() {
        assertEquals(INITIAL_REINFORCEMENT, gpc.getStateDescription());
        initialRenforcement();
        renforcementOnlyGame(0);
        renforcementOnlyGame(1);
        assertEquals(playerNames.getFirst(), gpc.getTurnManager().getCurrentPlayer().getName());
        // assertEquals(COMBO, gpc.getStateDescription());
        // combo();
        // assertEquals(REINFORCEMENT, gpc.getStateDescription());
        // renforcemente(0);
        // assertEquals(ATTACK, gpc.getStateDescription());
    }

    private void renforcementOnlyGame(int playerIndex) {
        // faccio la fase di combo, non gioco nulla e vado oltre
        assertEquals(
                playerNames.get(playerIndex),
                gpc.getTurnManager().getCurrentPlayer().getName());
        assertEquals(COMBO, gpc.getStateDescription());
        combo();

        // piazzo le truppe
        assertEquals(
                playerNames.get(playerIndex),
                gpc.getTurnManager().getCurrentPlayer().getName());
        assertEquals(REINFORCEMENT, gpc.getStateDescription());
        renforcemente(playerIndex);

        // skippo la fase di attacco
        assertEquals(
                playerNames.get(playerIndex),
                gpc.getTurnManager().getCurrentPlayer().getName());
        assertEquals(ATTACK, gpc.getStateDescription());
        gpc.nextPhase();

        // skippo la fase di spostamento
        assertEquals(
                playerNames.get(playerIndex),
                gpc.getTurnManager().getCurrentPlayer().getName());
        assertEquals(MOVEMENT, gpc.getStateDescription());
        gpc.nextPhase();

        // dopo la fase di sostamento devo essere tornato alla fase di combo con il
        // giocatore successivo
        var i = playerIndex == playerNames.size() - 1 ? 0 : playerIndex + 1;
        assertEquals(
                playerNames.get(i),
                gpc.getTurnManager().getCurrentPlayer().getName());
        assertEquals(COMBO, gpc.getStateDescription());
    }

    //
    // 1) controllo che il giocatore corrente sia lui
    // 2) controllo che la fase sia completa -> false
    // 3) provo a cambiare fase devo restare sulla stessa
    // 4) controllo che se metto le truppe su un territorio non mio non cambi nulla
    // ovvero stesse struppe nel etrritorio selezionato e stesse truppe da
    // posizionare per il giocatore
    // 5) provo a posizionare una truppa sul mio territorio
    // truppe del territorio + 1 truppe del giocatore - 1
    // controllo che la fase sia completa -> true

    private void initialRenforcement() {
        assertEquals(INITIAL_REINFORCEMENT, gpc.getStateDescription());
        for (int i = 0; i < playerNames.size(); i++) {
            InitialTurnExecutor(i);
        }
        gpc.nextPhase();
        assertEquals(COMBO, gpc.getStateDescription());
    }

    private void InitialTurnExecutor(int playerIndex) {
        var p = gpc.getTurnManager().getCurrentPlayer();
        var units = initialLogic.calcPlayerUnits();
        var unitsToPlace = p.getUnitsToPlace();
        var nextPlayerTerritoryIndex = (playerIndex * playerNames.size() + playerNames.size()) % territories.size();
        Territory t = null;
        int tUnit = 0;
        int pUnit = 0;

        for (int i = 0; i < unitsToPlace; i++) {

            // 1) controllo che la fase sia quella corretta
            assertEquals(INITIAL_REINFORCEMENT, gpc.getStateDescription());

            // 2) provo a cambiare fase
            gpc.nextPhase();
            assertEquals(INITIAL_REINFORCEMENT, gpc.getStateDescription());

            // 3) controllo che il giocatore corrente sia lui
            assertEquals(playerNames.get(playerIndex), p.getName());

            // 4) controlle che le unità da piazzare siano units - territories
            assertEquals(units - p.getTerritories().size() - i, p.getUnitsToPlace());

            // 5) provo a cambiare fase devo restare sulla stessa
            gpc.nextPhase();
            assertEquals(INITIAL_REINFORCEMENT, gpc.getStateDescription());

            // 6) controllo che se metto le truppe su un territorio non mio non cambi nulla
            // ovvero stesse struppe nel etrritorio selezionato e stesse truppe da
            // posizionare per il giocatore
            reinforceEnemyTerritoryTest(nextPlayerTerritoryIndex);

            // 7) provo a posizionare una truppa sul mio territorio
            // truppe del territorio + 1 truppe del giocatore - 1
            var tName = "T" + (playerIndex * playerNames.size() + 1);
            t = gameManager.getTerritory(tName).get();
            tUnit = t.getUnits();
            pUnit = p.getUnitsToPlace();
            gpc.selectTerritory(t);
            assertEquals(tUnit + 1, t.getUnits());
            assertEquals(pUnit - 1, p.getUnitsToPlace());
        }

        // 8) provo a posizionare una truppa in più di quelle che ho
        // truppe del territorio e le truppe del giocatore devono restare uguali
        var tName = "T" + (playerIndex * playerNames.size() + 1);
        t = gameManager.getTerritory(tName).get();
        gpc.selectTerritory(t);
        assertEquals(unitsToPlace + 1, t.getUnits());
        assertEquals(0, p.getUnitsToPlace());

        // 9) Posiziono una truppa su un territorio non mio
        reinforceEnemyTerritoryTest(nextPlayerTerritoryIndex);

        // 10) cambio giocatore
        gpc.performAction();
        int pIndex = playerIndex < playerNames.size() - 1 ? playerIndex + 1 : playerIndex;
        p = gpc.getTurnManager().getCurrentPlayer();
        assertEquals(playerNames.get(pIndex), p.getName());
    }

    private void reinforceEnemyTerritoryTest(int emenyTerriotryIndex) {
        var p = gpc.getTurnManager().getCurrentPlayer();
        var tName = "T" + (emenyTerriotryIndex + 1);
        var t = gameManager.getTerritory(tName).get();
        var tUnit = t.getUnits();
        var pUnit = p.getUnitsToPlace();
        gpc.selectTerritory(t);
        assertEquals(tUnit, t.getUnits());
        assertEquals(pUnit, p.getUnitsToPlace());
    }

    // 1) Provo a chiamare nextPhase non deve fare nulla resto nel mio
    // 2) Provo a mettere le truppe in un territorio non mio non deve succedere
    // nulla
    // 3) metto tutte le mie truppe su un territorio
    // 4) provo a mettere truppe avendole già piazzate non deve fae nulla
    // 5) provo a metterle in unterritorio non mio non deve fare nulla
    // 6) chiamo nextPhase e deve passare alla fase di attacco
    private void renforcemente(int playerIndex) {
        Territory t = null;
        int tUnit = 0;
        int pUnit = 0;
        // 1) controllo che il giocatore sia corretto
        var p = gpc.getTurnManager().getCurrentPlayer();
        assertEquals(playerNames.get(playerIndex), p.getName());

        // 2) Provo a chiamare nextPhase non deve fare nulla resto nel mio
        gpc.nextPhase();
        assertEquals(REINFORCEMENT, gpc.getStateDescription());

        // controllo le truppe da piazzare
        assertEquals(renforcementLogic.calcPlayerUnits(), p.getUnitsToPlace());

        // 3) Provo a mettere le truppe in un territorio non mio
        // e non deve succedere nulla
        var emenyTerriotryIndex = (playerIndex * playerNames.size() + playerNames.size()) % territories.size();
        reinforceEnemyTerritoryTest(emenyTerriotryIndex);

        // 4) metto tutte le mie truppe su un territorio
        var units = p.getUnitsToPlace();
        for (int i = 0; i < units; i++) {
            var tName = "T" + (playerIndex * playerNames.size() + 1);
            t = gameManager.getTerritory(tName).get();
            tUnit = t.getUnits();
            pUnit = p.getUnitsToPlace();
            gpc.selectTerritory(t);
            assertEquals(tUnit + 1, t.getUnits());
            assertEquals(pUnit - 1, p.getUnitsToPlace());

            // 5) provo a mettere le truppe in terriorio non mio non deve afe nulla
            reinforceEnemyTerritoryTest(emenyTerriotryIndex);
        }

        // 6) provo a mettere truppe avendole già piazzate non deve fae nulla
        gpc.selectTerritory(t);
        assertEquals(tUnit + 1, t.getUnits());
        assertEquals(0, p.getUnitsToPlace());

        // 7) chiamo nextPhase e deve passare alla fase di attacco
        gpc.nextPhase();
        assertEquals(ATTACK, gpc.getStateDescription());

        // 8) il giocatore deve sempre essere lui
        p = gpc.getTurnManager().getCurrentPlayer();
        assertEquals(playerNames.get(playerIndex), p.getName());
    }

    private void combo() {
        gpc.nextPhase();
    }

    private void attack() {

    }

    private void movement() {

    }

    @Test
    void testNextPlayerOrder() {
        // assertEquals(ALICE, turnManager.getCurrentPlayer().getName());
        // // todo: cambiare metodo assertFalse(turnManager.isNewRound());

        // assertEquals(BOB, turnManager.nextPlayer().getName());
        // // todo: cambiare metodo assertFalse(turnManager.isNewRound());

        // // assertEquals(CAROL, turnManager.nextPlayer().getName());
        // // todo: cambiare metodo assertFalse(turnManager.isNewRound());

        // assertEquals(ALICE, turnManager.nextPlayer().getName());
        // // todo: cambiare metodo assertTrue(turnManager.isNewRound());

        // assertEquals(BOB, turnManager.nextPlayer().getName());
        // // todo: cambiare metodo assertFalse(turnManager.isNewRound());
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

        // todo: cambiare metodo assertTrue(turnManager.isNewRound());
    }

    @Test
    void testInitialReinforcementState() {
        testFirstphase();

    }

    private void testFirstphase() {
        // /* first player turn */
        // assertEquals(INITIAL_REINFORCEMENT, gpc.getStateDescription());
        // assertEquals(gameManager.getPlayers().get(0),
        // turnManager.getCurrentPlayer());
        // assertEquals(7, gameManager.getPlayers().get(0).getUnitsToPlace());
        // assertEquals(0, territories.get(0).getUnits());
        // gpc.selectTerritory(territories.get(0));
        // gpc.selectTerritory(territories.get(0));
        // gpc.selectTerritory(territories.get(3));
        // gpc.selectTerritory(territories.get(3));
        // gpc.selectTerritory(territories.get(6));
        // gpc.selectTerritory(territories.get(6));
        // gpc.selectTerritory(territories.get(6));
        // gpc.selectTerritory(territories.get(6));
        // assertEquals(2, territories.get(0).getUnits());
        // assertEquals(2, territories.get(3).getUnits());
        // assertEquals(3, territories.get(6).getUnits());
        // /* second player turn */
        // gpc.performAction();
        // assertEquals(INITIAL_REINFORCEMENT, gpc.getStateDescription());
        // assertEquals(gameManager.getPlayers().get(1),
        // turnManager.getCurrentPlayer());
        // assertEquals(7, gameManager.getPlayers().get(1).getUnitsToPlace());
        // gpc.selectTerritory(territories.get(1));
        // gpc.selectTerritory(territories.get(1));
        // gpc.selectTerritory(territories.get(4));
        // gpc.selectTerritory(territories.get(4));
        // gpc.selectTerritory(territories.get(7));
        // gpc.selectTerritory(territories.get(7));
        // gpc.selectTerritory(territories.get(7));
        // assertEquals(2, territories.get(1).getUnits());
        // assertEquals(2, territories.get(4).getUnits());
        // assertEquals(3, territories.get(7).getUnits());
        // /* third player turn */
        // gpc.performAction();
        // assertEquals(INITIAL_REINFORCEMENT, gpc.getStateDescription());
        // assertEquals(gameManager.getPlayers().get(2),
        // turnManager.getCurrentPlayer());
        // assertEquals(7, gameManager.getPlayers().get(2).getUnitsToPlace());
        // gpc.selectTerritory(territories.get(2));
        // gpc.selectTerritory(territories.get(2));
        // gpc.selectTerritory(territories.get(5));
        // gpc.selectTerritory(territories.get(5));
        // gpc.selectTerritory(territories.get(8));
        // gpc.selectTerritory(territories.get(8));
        // gpc.selectTerritory(territories.get(8));
        // assertEquals(2, territories.get(2).getUnits());
        // assertEquals(2, territories.get(5).getUnits());
        // assertEquals(3, territories.get(8).getUnits());
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
