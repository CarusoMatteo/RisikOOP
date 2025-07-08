package it.unibo.risikoop.model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

import it.unibo.risikoop.model.implementations.Color;
import it.unibo.risikoop.model.implementations.GameManagerImpl;
import it.unibo.risikoop.model.implementations.PlayerImpl;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;

/**
 * Class to test adding Players.
 */
public class PlayerTest {
    private final GameManager gameManager = new GameManagerImpl();
    private final String armandoName = "Armando";
    private final String diegoName = "Diego";
    private final String bobName = "bob";
    private final String claraName = "Clara";

    @Test
    void addPlayer() {
        final List<Player> players = List.of(new PlayerImpl(armandoName, new Color(0, 0, 0)),
                new PlayerImpl(diegoName, new Color(0, 2, 0)));
        players.forEach(i -> gameManager.addPlayer(i.getName(), i.getColor()));
        assertEquals(players, gameManager.getPlayers());
        gameManager.addPlayer(bobName, new Color(1, 0, 0));
        assertEquals(List.of(new PlayerImpl(armandoName, new Color(0, 0, 0)),
                new PlayerImpl(diegoName, new Color(0, 2, 0)),
                new PlayerImpl(bobName, new Color(1, 0, 0))), gameManager.getPlayers());
        assert (gameManager.removePlayer(bobName));
        assertNotEquals(List.of(new PlayerImpl(armandoName, new Color(0, 0, 0)),
                new PlayerImpl(diegoName, new Color(0, 2, 0)),
                new PlayerImpl(bobName, new Color(1, 0, 0))), gameManager.getPlayers());
        assert (!gameManager.removePlayer(claraName));
        assertEquals(List.of(new PlayerImpl(armandoName, new Color(0, 0, 0)),
                new PlayerImpl(diegoName, new Color(0, 2, 0))), gameManager.getPlayers());
    }
}
