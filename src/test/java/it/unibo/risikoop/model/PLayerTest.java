package it.unibo.risikoop.model;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import it.unibo.risikoop.model.implementations.Color;
import it.unibo.risikoop.model.implementations.GameManagerImpl;
import it.unibo.risikoop.model.implementations.PlayerImpl;
import it.unibo.risikoop.model.implementations.TerritoryImpl;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;

/**
 * Class to test adding Players.
 */
class PlayerTest {
        private static final String ARMANDO = "Armando";
        private static final String DIEGO = "Diego";
        private static final String BOB = "bob";
        private static final String CLARA = "Clara";
        private final GameManager gameManager = new GameManagerImpl();

        @Test
        void addPlayer() {
                final List<Player> players = List.of(new PlayerImpl(ARMANDO, new Color(0, 0, 0)),
                                new PlayerImpl(DIEGO, new Color(0, 2, 0)));
                players.forEach(i -> gameManager.addPlayer(i.getName(), i.getColor()));
                assertEquals(players, gameManager.getPlayers());
                gameManager.addPlayer(BOB, new Color(1, 0, 0));
                assertEquals(List.of(new PlayerImpl(ARMANDO, new Color(0, 0, 0)),
                                new PlayerImpl(DIEGO, new Color(0, 2, 0)),
                                new PlayerImpl(BOB, new Color(1, 0, 0))), gameManager.getPlayers());
                assertTrue(gameManager.removePlayer(BOB));
                assertNotEquals(List.of(new PlayerImpl(ARMANDO, new Color(0, 0, 0)),
                                new PlayerImpl(DIEGO, new Color(0, 2, 0)),
                                new PlayerImpl(BOB, new Color(1, 0, 0))), gameManager.getPlayers());
                assertFalse(gameManager.removePlayer(CLARA));
                assertEquals(List.of(new PlayerImpl(ARMANDO, new Color(0, 0, 0)),
                                new PlayerImpl(DIEGO, new Color(0, 2, 0))), gameManager.getPlayers());
        }

        @Test
        void addTerritory() {
                final Player player = new PlayerImpl(ARMANDO, new Color(0, 0, 0));
                assertTrue(player.addTerritory(new TerritoryImpl(gameManager, "Afgan")));
                assertEquals(player.getTerritories()
                                .stream()
                                .map(i -> i.getName())
                                .collect(Collectors.toSet()), Set.of("Afgan"));
                assertFalse(player.removeTerritory(new TerritoryImpl(gameManager, ARMANDO)));
                assertTrue(player.removeTerritory(new TerritoryImpl(gameManager, "Afgan")));
                assertEquals(player.getTerritories()
                                .stream()
                                .map(i -> i.getName())
                                .collect(Collectors.toSet()), Set.of());

        }
}
