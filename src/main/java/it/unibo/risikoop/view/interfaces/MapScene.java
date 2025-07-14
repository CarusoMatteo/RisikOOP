package it.unibo.risikoop.view.interfaces;

import java.util.List;

import it.unibo.risikoop.model.implementations.Color;
import it.unibo.risikoop.model.interfaces.ObjectiveCard;
import it.unibo.risikoop.model.interfaces.cards.GameCard;

public interface MapScene {

    void updateCurrentPlayer(String playerName,
            Color playerColor,
            ObjectiveCard objectiveCard,
            List<GameCard> cards);
}