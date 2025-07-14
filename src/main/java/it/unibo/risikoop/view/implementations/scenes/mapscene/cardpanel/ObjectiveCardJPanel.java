package it.unibo.risikoop.view.implementations.scenes.mapscene.cardpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import it.unibo.risikoop.model.interfaces.ObjectiveCard;

/**
 * Panel to display the Objective Card in MapScene's CardJPanel.
 */
public class ObjectiveCardJPanel extends JPanel {
    private static final String CARD_HIDDEN_TEXT = "Objective Card hidden.";

    private ObjectiveCard currenObjectiveCard;
    private final JLabel descriptionLabel;

    /**
     * Constructor for ObjectiveCardJPanel.
     * @param objectiveCard The ObjectiveCard to display for the first time.
     */
    public ObjectiveCardJPanel(final ObjectiveCard objectiveCard) {
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

    /**
     * Updates the ObjectiveCard displayed in this panel.
     * @param objectiveCard The new ObjectiveCard to display.
     */
    public void updateObjectiveCard(final ObjectiveCard objectiveCard) {
        this.currenObjectiveCard = objectiveCard;
    }

    /**
     * Hides the ObjectiveCard information.
     */
    public void hideInfo() {
        this.descriptionLabel.setText("<html><div style='text-align: center; word-wrap: break-word;'>"
                + CARD_HIDDEN_TEXT
                + "</div></html>");
    }

    /**
     * Shows the ObjectiveCard information.
     */
    public void showInfo() {
        this.descriptionLabel.setText("<html><div style='text-align: center; word-wrap: break-word;'>"
                + this.currenObjectiveCard.getDescription()
                + "</div></html>");
    }
}
