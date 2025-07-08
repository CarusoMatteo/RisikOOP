package it.unibo.risikoop.model.implementations;

import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;

record PlayerGameContext(
        Player player,
        GameManager gameManager) {
}
