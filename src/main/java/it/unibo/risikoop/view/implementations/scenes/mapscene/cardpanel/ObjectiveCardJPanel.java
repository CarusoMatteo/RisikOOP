package it.unibo.risikoop.view.implementations.scenes.mapscene.cardpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import it.unibo.risikoop.model.interfaces.ObjectiveCard;

public class ObjectiveCardJPanel extends JPanel {
    private static final String CARD_HIDDEN_TEXT = "Objective Card hidden.";

    private ObjectiveCard currenObjectiveCard;
    private JLabel descriptionLabel;

    public ObjectiveCardJPanel(ObjectiveCard objectiveCard) {
        System.out.println("Creating ObjectiveCardJPanel with objective card: " + objectiveCard.getDescription());

        this.setLayout(new BorderLayout());

        this.descriptionLabel = new JLabel();
        this.descriptionLabel.setPreferredSize(new Dimension(1, 1));
        this.descriptionLabel.setMinimumSize(new Dimension(1, 1));
        this.descriptionLabel.setForeground(Color.WHITE);
        this.descriptionLabel.setHorizontalAlignment(JLabel.CENTER);
        this.descriptionLabel.setVerticalAlignment(JLabel.CENTER);

        add(this.descriptionLabel, BorderLayout.CENTER);

        updateObjectiveCard(objectiveCard);
    }

    public void updateObjectiveCard(ObjectiveCard objectiveCard) {
        System.out.println("Updating Objective Card -> " + objectiveCard.getDescription());
        this.currenObjectiveCard = objectiveCard;
    }

    public void hideInfo() {
        System.out.println("Hide -> Replacing description with hidden text.");

        this.descriptionLabel.setText("<html><div style='text-align: center; word-wrap: break-word;'>"
                + CARD_HIDDEN_TEXT
                + "</div></html>");
    }

    public void showInfo() {
        System.out.println("Show -> Replacing description with current objective card description.");

        this.descriptionLabel.setText("<html><div style='text-align: center; word-wrap: break-word;'>"
                + this.currenObjectiveCard.getDescription()
                + "</div></html>");
    }
}
