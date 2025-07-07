package it.unibo.risikoop.model;

import java.io.File;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import it.unibo.risikoop.controller.implementations.DataAddingControllerImpl;
import it.unibo.risikoop.controller.interfaces.DataAddingController;
import it.unibo.risikoop.model.implementations.GameManagerImpl;
import it.unibo.risikoop.model.interfaces.Continent;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Territory;

public class WorldTest {
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
        void addUnitToTerritory() {
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

        @Test
        void territoryFromFile() {
                DataAddingController controller = new DataAddingControllerImpl(gameManager);
                assertTrue(controller.setWorldFromFile("src" + File.separator + "test" + File.separator
                                + "resources" + File.separator + "model" + File.separator + "mapTest1.txt"));

                assertNotEquals(Set.of("Inghilterra", "Francia", "StatiUniti", "Lussemburgo"),
                                gameManager.getTerritories()
                                                .stream().map(Territory::getName)
                                                .collect(Collectors.toSet()));
                assertEquals(Set.of("Inghilterra", "Francia", "StatiUniti", "Lussemburgo", "Russia", "Italia", "China",
                                "Giappone"),
                                gameManager.getTerritories()
                                                .stream().map(Territory::getName)
                                                .collect(Collectors.toSet()));
                assertEquals(Set.of("Asia", "Europa", "America"),
                                gameManager.getContinents()
                                                .stream()
                                                .map(i -> i.getName())
                                                .collect(Collectors.toSet()));
        }

        @Test
        void clearingTest() {
                DataAddingController controller = new DataAddingControllerImpl(gameManager);
                assertTrue(controller.setWorldFromFile("src" + File.separator + "test" + File.separator
                                + "resources" + File.separator + "model" + File.separator + "mapTest1.txt"));
                gameManager.removeAllTerritoriesAndContinents();
                assertEquals(Set.of(), gameManager.getContinents());
                assertEquals(Set.of(), gameManager.getTerritories());
                assertFalse(controller.setWorldFromFile("src" + File.separator + "test" + File.separator
                                + "java" + File.separator + "model" + File.separator + ".java"));
                assertEquals(Set.of(), gameManager.getTerritories());
                assertEquals(Set.of(), gameManager.getContinents());
        }

        @Test
        void setDefaultWorld() {
                DataAddingController controller = new DataAddingControllerImpl(gameManager);
                controller.setDefaultMap();
                Optional<Continent> africa = gameManager.getContinent("Africa");
                assertTrue(africa.isPresent());
                assertEquals(Set.of("North-Africa", "Egypt", "Congo", "South-Africa", "Est-Africa"),
                                africa.get().getTerritories().stream().map(i -> i.getName())
                                                .collect(Collectors.toSet()));

        }
}
