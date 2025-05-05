package it.unibo.risikoop.model;

import java.util.Set;
import java.util.stream.Collectors;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

import it.unibo.risikoop.model.Implementations.GameManagerImpl;
import it.unibo.risikoop.model.interfaces.GameManager;

public class TerritoryTest {
        private final GameManager gameManager = new GameManagerImpl();

        @Test
        void addTerritory() {
                Graph map = new MultiGraph("map", false, true);
                map.addEdge("JP-USA", "JP", "USA");
                map.addEdge("USA-UK", "USA", "UK");
                map.addEdge("IT-UK", "IT", "UK");
                gameManager.setWorldMap(map);
                assertEquals(gameManager.getTerritories()
                                .stream()
                                .map(i -> i.getName())
                                .collect(Collectors.toSet()), Set.of("IT", "USA", "JP", "UK"));
                assertNotEquals(gameManager.getTerritories()
                                .stream()
                                .map(i -> i.getName())
                                .collect(Collectors.toSet()), Set.of("id", "USA", "JP", "UK"));
                /*
                 * Checking JP neightbours
                 */
                assertNotEquals(gameManager.getTerritoryNeightbours("JP")
                                .stream()
                                .map(i -> i.getName()).collect(Collectors.toSet()), Set.of("IT", "USA"));
                assertEquals(gameManager.getTerritoryNeightbours("JP")
                                .stream()
                                .map(i -> i.getName()).collect(Collectors.toSet()), Set.of("USA"));
                /*
                 * Checking USA neightbours
                 */
                assertNotEquals(gameManager.getTerritoryNeightbours("USA")
                                .stream()
                                .map(i -> i.getName()).collect(Collectors.toSet()), Set.of("JP", "S"));

                assertEquals(gameManager.getTerritoryNeightbours("USA")
                                .stream()
                                .map(i -> i.getName()).collect(Collectors.toSet()), Set.of("JP", "UK"));
                /*
                 * Checking UK neightbours
                 */
                assertEquals(gameManager.getTerritoryNeightbours("UK")
                                .stream()
                                .map(i -> i.getName()).collect(Collectors.toSet()), Set.of("IT", "USA"));
        }

        @Test
        void addUnits() {
                Graph map = new MultiGraph("map", true, true);
                map.addNode("IT");
                map.addNode("USA");
                map.addNode("UK");
                map.addNode("JP");
                map.addEdge("JP-USA", "JP", "USA");
                map.addEdge("USA-UK", "USA", "UK");
                map.addEdge("IT-UK", "IT", "UK");
                gameManager.setWorldMap(map);
                gameManager.addUnits("IT", 10);
                gameManager.addUnits("IT", 10);
                gameManager.addUnits("IT", -1);
                assertEquals(gameManager.getTerritory("IT").get().getUnits(), 20);
                gameManager.removeUnits("IT", -1);
                gameManager.removeUnits("IT", 1);
                assertEquals(gameManager.getTerritory("IT").get().getUnits(), 19);

        }
}
