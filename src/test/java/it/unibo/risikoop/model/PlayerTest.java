package it.unibo.risikoop.model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.risikoop.model.implementations.Color;
import it.unibo.risikoop.model.implementations.GameManagerImpl;
import it.unibo.risikoop.model.implementations.PlayerGameContext;
import it.unibo.risikoop.model.implementations.PlayerImpl;
import it.unibo.risikoop.model.implementations.specification.KillPlayerSpec;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;

/**
 * Class to test adding Players.
 */
public class PlayerTest {
        private final GameManager gameManager = new GameManagerImpl();
        private List<Player> players;
        // private PlayerGameContext ctx;

        @BeforeEach
        void setUp() {
                players = List.of(
                                new PlayerImpl("Armando", new Color(0, 0, 0)),
                                new PlayerImpl("Diego", new Color(0, 2, 0)));
                players.forEach(i -> gameManager.addPlayer(i.getName(), i.getColor()));

        }

        @Test
        void killPlayer() {
                Player target = new PlayerImpl("Victim", new Color(0, 3, 0));
                Player killer = new PlayerImpl("Killer", new Color(0, 4, 0));

                var ctxVctim = new PlayerGameContext(target, gameManager);
                var ctxKiller = new PlayerGameContext(killer, gameManager);

                target.setKiller(killer);

                // testo se la specifica di uccisione è soddisfatta
                KillPlayerSpec killVictimSpec = new KillPlayerSpec(target);
                KillPlayerSpec killKillerSpec = new KillPlayerSpec(killer);


                // controllo se la vittima è stata uccisa dal killer
                assertEquals(true, killVictimSpec.isSatisfiedBy(ctxKiller),
                                "KillPlayerSpec should be satisfied by the killer's context");

                // controllo se il killer è stato ucciso dalla vittima
                assertEquals(false, killKillerSpec.isSatisfiedBy(ctxVctim),
                                "KillPlayerSpec should be satisfied by the killer's context");

        }
}
