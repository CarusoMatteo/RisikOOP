package it.unibo.risikoop.model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

import it.unibo.risikoop.model.classes.Color;
import it.unibo.risikoop.model.classes.GameManagerImpl;
import it.unibo.risikoop.model.classes.PlayerImpl;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;

public class PlayerTest {
        private GameManager gameManager = new GameManagerImpl();

        @Test
        void addPlayer() {
                List<Player> players = List.of(
                                new PlayerImpl("Armando", new Color(0, 0, 0)),
                                new PlayerImpl("Diego", new Color(0, 2, 0)));
                players.forEach(i -> gameManager.addPlayer(i.getName(), i.getColor()));
                assertEquals(players, gameManager.getPlayers());
                gameManager.addPlayer("bob", new Color(1, 0, 0));
                assertEquals(List.of(
                                new PlayerImpl("Armando", new Color(0, 0, 0)),
                                new PlayerImpl("Diego", new Color(0, 2, 0)),
                                new PlayerImpl("bob", new Color(1, 0, 0))), gameManager.getPlayers());
                assert (gameManager.removePlayer("bob"));
                assertNotEquals(List.of(
                                new PlayerImpl("Armando", new Color(0, 0, 0)),
                                new PlayerImpl("Diego", new Color(0, 2, 0)),
                                new PlayerImpl("bob", new Color(1, 0, 0))), gameManager.getPlayers());
                assert (!gameManager.removePlayer("Clara"));
                assertEquals(List.of(
                                new PlayerImpl("Armando", new Color(0, 0, 0)),
                                new PlayerImpl("Diego", new Color(0, 2, 0))), gameManager.getPlayers());
        }
}
