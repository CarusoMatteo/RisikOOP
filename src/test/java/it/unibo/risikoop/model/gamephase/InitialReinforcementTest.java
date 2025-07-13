package it.unibo.risikoop.model.gamephase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

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
import it.unibo.risikoop.model.interfaces.GamePhase;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.TurnManager;

class InitialReinforcementTest {
    
    private static final String ALICE = "Alice";
    private static final String BOB = "Bob";
    private static final String CAROL = "Carol";
    private static final int NUMB_TERRITORY = 9;
    private TurnManager tm;
    private GamePhaseController gpc;
    private List<Territory> territories;
    private GameManager gm;
    private GamePhase gp;
    private LogicReinforcementCalculator logic;

        @BeforeEach
    void setUp() {
        // gameManager non serve come campo: lo usiamo solo qui
        gm = new GameManagerImpl();
        gm.addPlayer(ALICE, new Color(0, 0, 0));
        gm.addPlayer(BOB, new Color(1, 0, 0));
        gm.addPlayer(CAROL, new Color(2, 0, 0));

        final var players = gm.getPlayers();
        territories = List.copyOf(createTerritoryList());

        final Graph graph = createTestMap();
        gm.setWorldMap(graph);
        players.get(0).addTerritory(gm.getTerritory("T1").get());
        players.get(0).addTerritory(gm.getTerritory("T2").get());
        players.get(0).addTerritory(gm.getTerritory("T3").get());

        players.get(1).addTerritory(gm.getTerritory("T4").get());
        players.get(1).addTerritory(gm.getTerritory("T5").get());
        players.get(1).addTerritory(gm.getTerritory("T6").get());

        players.get(2).addTerritory(gm.getTerritory("T7").get());
        players.get(2).addTerritory(gm.getTerritory("T8").get());
        players.get(2).addTerritory(gm.getTerritory("T9").get());

        tm = new TurnManagerImpl(players); 
        gpc = new GamePhaseControllerImpl(List.of(), tm, gm);
        gp = new InitialReinforcementPhase(gpc, gm);
        logic = new LogicCalcInitialUnitsImpl(gm);
    }

    @Test
    void checkFirstPlayerUnits(){
        var p = gpc.getTurnManager().getCurrentPlayer();
        assertEquals(ALICE, p.getName());
        int trup = logic.calcPlayerUnits();
        assertEquals(trup, p.getUnitsToPlace());
    }

    @Test
    void checkPlaceUnits(){

        // Calcolo le truppe di ogni giocatore
        int units = logic.calcPlayerUnits();
        var p = gpc.getTurnManager().getCurrentPlayer();

        assertFalse(gp.isComplete());

        // ? TESTO LA LOGCA DEL PRIMO GIOCATORE

        // testo il posizionamento del primo player
        assertEquals(ALICE, p.getName());
        assertEquals(units, p.getUnitsToPlace());
        var t = territories.get(0);
        for(int i = 1; i <= units; i++){
            // provo a cambiare gioacatore NOn deve cambiare
            gp.performAction();
            assertEquals(ALICE, gpc.getTurnManager().getCurrentPlayer().getName());

            // assegno una truppa al territorio
            gp.selectTerritory(t);
            assertEquals(units - i , p.getUnitsToPlace());
            assertEquals(i, t.getUnits());
        }

        // Non deve assegnare nulla perchè ha messo già tutte le truppe
        gp.selectTerritory(t);
        assertEquals(0 , p.getUnitsToPlace());
        assertEquals(units, t.getUnits());

        // Non deve posizionare nulla perhè ha finito le truppe e non è un suo territorio
        gp.selectTerritory(territories.get(3));
        assertEquals(0 , p.getUnitsToPlace());
        assertEquals(0, territories.get(3).getUnits());

        assertFalse(gp.isComplete());

        // ? TESTO LA LOGCA DEL SECONDO GIOCATORE

        // deve cambiare giocatore
        gp.performAction();
        p = gpc.getTurnManager().getCurrentPlayer();
        assertEquals(BOB, p.getName());

        // testo il posizionamneto del secondo player
        assertEquals(units, p.getUnitsToPlace());
        t = territories.get(3);
        for(int i = 1; i <= units; i++){

            // provo a cambiare gioacatore NOn deve cambiare
            gp.performAction();
            assertEquals(BOB, gpc.getTurnManager().getCurrentPlayer().getName());

            gp.selectTerritory(t);
            assertEquals(units - i , p.getUnitsToPlace());
            assertEquals(i, t.getUnits());
        }

        assertFalse(gp.isComplete());

        // ? TESTO LA LOGCA DEL TERZO GIOCATORE

        // deve cambiare giocatore
        gp.performAction();
        p = gpc.getTurnManager().getCurrentPlayer();
        assertEquals(CAROL, p.getName());
        assertEquals(units, p.getUnitsToPlace());

        // provo ad assegnare le truppe ad un territorio che non è mio
        gp.selectTerritory(t);
        assertEquals(units, p.getUnitsToPlace());
        assertEquals(units, t.getUnits());

        // posiziono tutte le truppe dell'ultimo giocatore
        t = territories.get(6);
        for(int i = 1; i <= units; i++){

            // provo a cambiare gioacatore NOn deve cambiare
            gp.performAction();
            assertEquals(CAROL, gpc.getTurnManager().getCurrentPlayer().getName());

            gp.selectTerritory(t);
            assertEquals(units - i , p.getUnitsToPlace());
            assertEquals(i, t.getUnits());
        }

        assertTrue(gpc.getTurnManager().isLastPlayer());

        // Ho finito i giocatori quindi devo restare su Carol anche se ha finito i territori
        gp.performAction();
        p = gpc.getTurnManager().getCurrentPlayer();
        assertEquals(CAROL, p.getName());

        // Controllo se la fase Iniziale è comletata
        assertTrue(gp.isComplete());
    }


    private List<String> listTerritoryName(){
        List<String> list = new ArrayList<>();
        for(int i = 1; i <= NUMB_TERRITORY; i++){
            list.add("T"+i);
        }
        return list;
    }

    private List<Territory> createTerritoryList(){
        List<Territory> list = new ArrayList<>();
        for(var name : listTerritoryName()){
            var t =  new TerritoryImpl(gm, name);
            list.add(t);
        }
        return list;
    }

    private Graph createTestMap() {
        final List<String> territoriesName = List.copyOf(listTerritoryName());
        Graph graph = new MultiGraph(ALICE, false, true);
        graph.addEdge("1", territoriesName.get(0), territoriesName.get(5));
        graph.addEdge("2", territoriesName.get(2), territoriesName.get(4));
        graph.addEdge("3", territoriesName.get(3), territoriesName.get(1));
        graph.addEdge("6", territoriesName.get(7), territoriesName.get(8));
        graph.addEdge("7", territoriesName.get(8), territoriesName.get(2));
        graph.addEdge("7", territoriesName.get(6), territoriesName.get(2));
        return graph;
    }

}
