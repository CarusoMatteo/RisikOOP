package it.unibo.risikoop.model.implementations;

import java.util.Objects;

import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;

public record PlayerGameContext(
                Player player,
                GameManager gameManager) {

        public PlayerGameContext {
                Objects.requireNonNull(player, "player must not be null");
                Objects.requireNonNull(gameManager, "gameManager must not be null");
        }
}
