package it.unibo.risikoop.view.implementations.scenes.mapscene;

import java.util.List;

import javax.swing.JPanel;

import it.unibo.risikoop.model.interfaces.ObjectiveCard;
import it.unibo.risikoop.model.interfaces.cards.GameCard;

/**
 * Panel to display the Cards in the MapScene.
 */
public final class CardJpanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public CardJpanel(ObjectiveCard objectiveCard, List<GameCard> cards) {

        updateCurrentPlayerCards(objectiveCard, cards);
    }

    public void updateCurrentPlayerCards(ObjectiveCard objectiveCard, List<GameCard> cards) {

    }
}
