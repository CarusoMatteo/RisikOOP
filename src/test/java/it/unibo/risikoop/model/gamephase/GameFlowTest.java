package it.unibo.risikoop.model.gamephase;

import java.lang.reflect.Field;
import java.util.List;

import javax.print.DocFlavor.SERVICE_FORMATTED;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.risikoop.controller.implementations.GamePhaseControllerImpl;
import it.unibo.risikoop.controller.implementations.logicgame.LogicAttackImpl;
import it.unibo.risikoop.controller.implementations.logicgame.LogicCalcInitialUnitsImpl;
import it.unibo.risikoop.controller.implementations.logicgame.LogicReinforcementCalculatorImpl;
import it.unibo.risikoop.controller.interfaces.GamePhaseController;
import it.unibo.risikoop.controller.interfaces.logicgame.LogicAttack;
import it.unibo.risikoop.controller.interfaces.logicgame.LogicReinforcementCalculator;
import it.unibo.risikoop.model.implementations.Color;
import it.unibo.risikoop.model.implementations.GameManagerImpl;
import it.unibo.risikoop.model.implementations.TerritoryImpl;
import it.unibo.risikoop.model.implementations.TurnManagerImpl;
import it.unibo.risikoop.model.implementations.gamephase.AttackPhase;
import it.unibo.risikoop.model.interfaces.AttackResult;
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
// committo prima del merge
class GameFlowTest {

    // evita literal duplicati
    private static final List<String> playerNames = List.of("Alice", "Bob");
    private static final String INITIAL_REINFORCEMENT = "Fase di rinforzo iniziale";
    private static final String REINFORCEMENT = "Fase di rinforzo";
    private static final String COMBO = "Fase di gestione combo";
    private static final String ATTACK = "Fase di gestione attacchi";
    private static final String MOVEMENT = "Fase di gestione spostamenti";

    // innerstate attack
    private static final String SELECT_ATTACKER = "Selecting attacker";
    private static final String SELECT_DEFENDER = "Selecting defender";
    private static final String SELECT_UNITS = "Selecting units quantity";
    private static final String EXECUTE_ATTACK = "Executing the attack";

    // inner state movement
    private static final String SELECT_SOURCE = "Selecting the moving from territory";
    private static final String SELECT_DESTINATION = "Selecting the moving to territory";
    private static final String SELECT_UNITS_MOVEMENTS = "Selecting units quantity";
    private static final String MOVE_UNITS = "Executing the movement";

    // possibili attacchi
    private static final List<Integer> LIST_6 = List.of(6, 6);
    private static final List<Integer> LIST_543 = List.of(5, 4, 3);

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
        // graph.addEdge("1", territoriesName.get(0), territoriesName.get(1));
        // graph.addEdge("1", territoriesName.get(1), territoriesName.get(0));
        // graph.addEdge("2", territoriesName.get(1), territoriesName.get(2));
        // graph.addEdge("3", territoriesName.get(2), territoriesName.get(3));
        // graph.addEdge("4", territoriesName.get(3), territoriesName.get(0));
        makeFullyConnected(graph, territoriesName);
        return graph;
    }

    private void makeFullyConnected(Graph graph, List<String> territories) {
        int edgeId = 0;
        // ogni nodo verso ogni altro (dirigendo l'arco)
        for (int i = 0; i < territories.size(); i++) {
            for (int j = 0; j < territories.size(); j++) {
                if (i == j)
                    continue;
                String src = territories.get(i);
                String dst = territories.get(j);
                String id = "e" + edgeId++;
                // true = grafo diretto
                graph.addEdge(id, src, dst, true);
            }
        }
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
        normalGame(0);

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

    private void normalGame(int playerIndex) {
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

        // inizio la fase di attacco
        assertEquals(
                playerNames.get(playerIndex),
                gpc.getTurnManager().getCurrentPlayer().getName());
        assertEquals(ATTACK, gpc.getStateDescription());
        attack(playerIndex);

        assertEquals(
                playerNames.get(playerIndex),
                gpc.getTurnManager().getCurrentPlayer().getName());
        assertEquals(MOVEMENT, gpc.getStateDescription());
        movementPlayer1(playerIndex);

        assertEquals(
                playerNames.get((playerIndex + 1) % playerNames.size()),
                gpc.getTurnManager().getCurrentPlayer().getName());
        assertEquals(COMBO, gpc.getStateDescription());

    }

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
            String state = gpc.getStateDescription();
            assertEquals(INITIAL_REINFORCEMENT, state);

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

    // 1) controllo che il giocatore sia quello corretto
    // 2) contrtollo che la fase sia quella giusta
    // 3) seleziono come territorio uno che non sia mio (Non succede nulla)
    // 4) clicclo su perform action non succede nulla
    // 5) seleziono un mio territorio (lo seleziono come attacante) -> lo imposta
    // 6) selezione un territorio non mio non succede nulla
    // 7) seleziono il mio altro territorio lo imposta
    // 8)
    private void attack(int playerIndex) {
        var p = gpc.getTurnManager().getCurrentPlayer();
        var enemyTerritoryIndex = (playerIndex * playerNames.size() + playerNames.size() + 1) % territories.size();
        Territory enemyT = gameManager.getTerritory("T" + enemyTerritoryIndex).get();
        Territory myT = gameManager.getTerritory("T" + (playerIndex * playerNames.size() + 1)).get();

        // assegno a tutti i territory 5 truppe
        gameManager.getTerritories().stream().forEach(t -> t.addUnits(5));

        // 1) controllo che il giocatore sia quello corretto
        assertEquals(playerNames.get(playerIndex), p.getName());

        // 2) contrtollo che la fase sia quella giusta
        assertEquals(ATTACK, gpc.getStateDescription());

        // 3) controllo che lo stato interno sia seleziona attacante
        assertEquals(SELECT_ATTACKER, gpc.getInnerStatePhaseDescription());

        // 4) seleziono come territorio uno che non sia mio (Non succede nulla)
        gpc.selectTerritory(enemyT);

        // 5) provo a proseguire a devo restare in seleziona territorio
        gpc.performAction();
        assertEquals(SELECT_ATTACKER, gpc.getInnerStatePhaseDescription());

        // 6) seleziono il mio
        gpc.selectTerritory(myT);
        gpc.performAction();
        assertEquals(SELECT_DEFENDER, gpc.getInnerStatePhaseDescription());
        gpc.nextPhase();
        assertEquals(ATTACK, gpc.getStateDescription());

        // 6) seleziono un mio territorioe non succede nulla
        gpc.selectTerritory(myT);
        gpc.performAction();
        assertEquals(SELECT_DEFENDER, gpc.getInnerStatePhaseDescription());
        gpc.nextPhase();
        assertEquals(ATTACK, gpc.getStateDescription());

        // 6) seleziono il territorio avversario e diventa quelle da attaccare
        gpc.selectTerritory(enemyT);
        gpc.performAction();
        assertEquals(SELECT_UNITS, gpc.getInnerStatePhaseDescription());
        gpc.nextPhase();
        assertEquals(ATTACK, gpc.getStateDescription());

        // 7) tocco terrytory a caso non deve fae nulla
        gpc.selectTerritory(enemyT);
        gpc.performAction();
        gpc.selectTerritory(myT);
        gpc.performAction();
        assertEquals(SELECT_UNITS, gpc.getInnerStatePhaseDescription());
        gpc.nextPhase();
        assertEquals(ATTACK, gpc.getStateDescription());

        // imposto le unità con cui attaccare
        var unitsUsed = 2;
        gpc.setUnitsToUse(unitsUsed);

        // 8) imposto le unità con cui attacare
        var unitAttacker = myT.getUnits();
        gpc.performAction();
        assertEquals(EXECUTE_ATTACK, gpc.getInnerStatePhaseDescription());

        // 9) attacco
        // testo attacco lento
        setWinAttacker();
        var enemyName = enemyT.getOwner().getName();
        var enemyTerritoryUnits = enemyT.getUnits();
        gpc.performAction();
        AttackResult res = null;

        if (gpc.showAttackResults().isPresent()) {
            res = gpc.showAttackResults().get();
            assertEquals(res.getAttackerDiceRolls(), LIST_6);
            assertEquals(res.getDefenderDiceRolls(), LIST_543);
            assertEquals(enemyName, enemyT.getOwner().getName());
            assertEquals(enemyTerritoryUnits - unitsUsed, enemyT.getUnits());
            assertEquals(unitAttacker, myT.getUnits());
        }

        // gpc.enableFastAttack();
        // gpc.performAction();

        // assertEquals(p.getName(), enemyT.getOwner().getName());
        // assertEquals(unitsUsed, enemyT.getUnits());
        // assertEquals(unitAttacker - unitsUsed, myT.getUnits());

        // controllo di essere tornato su select attaker
        assertEquals(SELECT_ATTACKER, gpc.getInnerStatePhaseDescription());

        // faccio vincere il difensore
        gpc.selectTerritory(myT);
        gpc.performAction();
        var t = gameManager.getTerritory("T4").get();
        gpc.selectTerritory(t);
        gpc.performAction();
        gpc.setUnitsToUse(unitsUsed);
        gpc.performAction();
        setWinDefender();
        unitAttacker = myT.getUnits();
        var defenderUnit = t.getUnits();
        gpc.performAction();

        res = null;
        if (gpc.showAttackResults().isPresent()) {
            res = gpc.showAttackResults().get();
            assertEquals(res.getAttackerDiceRolls(), LIST_543);
            assertEquals(res.getDefenderDiceRolls(), LIST_6);
            assertEquals(enemyName, enemyT.getOwner().getName());
            assertEquals(defenderUnit, t.getUnits());
            assertEquals(unitAttacker - unitsUsed, myT.getUnits());
        }

        // assertEquals(playerNames.get(1), t.getOwner().getName());
        // assertEquals(defenderUnit, t.getUnits());
        // assertEquals(unitAttacker - unitsUsed, myT.getUnits());

        // controllo di poter passare alla prossima fase
        gpc.nextPhase();
        assertEquals(MOVEMENT, gpc.getStateDescription());

    }

    private void setWinAttacker() {
        AttackPhase phase = (AttackPhase) gpc.getCurrentPhase();
        LogicAttackImpl l = (LogicAttackImpl) phase.getAttackLogic();
        l.setAttackerDice(LIST_6);
        l.setDefencerDice(LIST_543);
    }

    private void setWinDefender() {
        AttackPhase phase = (AttackPhase) gpc.getCurrentPhase();
        LogicAttackImpl l = (LogicAttackImpl) phase.getAttackLogic();

        l.setAttackerDice(LIST_543);
        l.setDefencerDice(LIST_6);
    }

    private void movementPlayer1(int playerIndex) {
        var p = gpc.getTurnManager().getCurrentPlayer();
        Territory enemyT = gameManager.getTerritory("T4").get();
        Territory src = gameManager.getTerritory("T2").get();
        Territory dst = gameManager.getTerritory("T1").get();

        // 1) controllo che il giocatore sia quello corretto
        assertEquals(playerNames.get(playerIndex), p.getName());

        // 2) contrtollo che la fase sia quella giusta
        assertEquals(MOVEMENT, gpc.getStateDescription());

        // 3) controllo che lo stato interno sia seleziona sorgente
        assertEquals(SELECT_SOURCE, gpc.getInnerStatePhaseDescription());

        // 4) seleziono come territorio uno che non sia mio (Non succede nulla)
        gpc.selectTerritory(enemyT);
        gpc.performAction();
        assertEquals(SELECT_SOURCE, gpc.getInnerStatePhaseDescription());

        // seleziono un mio territorio
        gpc.selectTerritory(dst);
        gpc.selectTerritory(src);
        gpc.performAction();
        assertEquals(SELECT_DESTINATION, gpc.getInnerStatePhaseDescription());

        // seleziono un territorio non mio
        gpc.selectTerritory(enemyT);
        gpc.performAction();
        assertEquals(SELECT_DESTINATION, gpc.getInnerStatePhaseDescription());

        gpc.selectTerritory(src);
        gpc.performAction();
        assertEquals(SELECT_DESTINATION, gpc.getInnerStatePhaseDescription());

        gpc.selectTerritory(dst);
        gpc.performAction();
        assertEquals(SELECT_UNITS_MOVEMENTS, gpc.getInnerStatePhaseDescription());

        // imposto le truppe da muovere
        int unitToMove = 3;
        int srcUnit = src.getUnits();
        int dstUnit = dst.getUnits();
        gpc.setUnitsToUse(unitToMove);
        gpc.performAction();

        // controllo se sono nella fase di spostamento truppe e muovo le truppe
        assertEquals(MOVE_UNITS, gpc.getInnerStatePhaseDescription());
        gpc.performAction();
        assertEquals(srcUnit - unitToMove, src.getUnits());
        assertEquals(dstUnit + unitToMove, dst.getUnits());
        assertEquals(MOVE_UNITS, gpc.getInnerStatePhaseDescription());

        // passo al prossimo player
        gpc.nextPhase();
        assertEquals(COMBO, gpc.getStateDescription());
        assertEquals(playerNames.get(1), gpc.getTurnManager().getCurrentPlayer().getName());
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
