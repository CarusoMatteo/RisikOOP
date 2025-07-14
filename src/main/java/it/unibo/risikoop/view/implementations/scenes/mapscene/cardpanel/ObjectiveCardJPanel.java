package it.unibo.risikoop.view.implementations.scenes.mapscene.cardpanel;

import javax.swing.JPanel;

import it.unibo.risikoop.model.interfaces.ObjectiveCard;

public class ObjectiveCardJPanel extends JPanel {
    private ObjectiveCard currenObjectiveCard;

    public ObjectiveCardJPanel(ObjectiveCard objectiveCard) {
        updateObjectiveCard(currenObjectiveCard);
    }

    public void updateObjectiveCard(ObjectiveCard objectiveCard) {
        this.currenObjectiveCard = objectiveCard;
    }

    public void hideInfo() {
    }

    public void showInfo() {
    }

}
