package it.unibo.risikoop.model.implementations;

import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;

public record PlayerGameContext(
        Player player,
        GameManager gameManager) {
}
