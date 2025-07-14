package it.unibo.risikoop.view.implementations.scenes.mapscene;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.util.List;

import javax.swing.JPanel;

import it.unibo.risikoop.model.interfaces.ObjectiveCard;
import it.unibo.risikoop.model.interfaces.cards.GameCard;

/**
 * Panel to display the Cards in the MapScene.
 */
public final class CardJpanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JPanel hideButtonPanel;
    private JPanel objectiveCardPanel;
    private JPanel cardsPanel;

    public CardJpanel(ObjectiveCard objectiveCard, List<GameCard> cards) {

        setupPanels();

        updateCurrentPlayerCards(objectiveCard, cards);
    }

    private void setupPanels() {
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        this.hideButtonPanel = new HideButtonPanel();
        this.objectiveCardPanel = new ObjectiveCardPanel();
        this.cardsPanel = new JPanel();

        this.hideButtonPanel.setBackground(Color.LIGHT_GRAY);
        this.objectiveCardPanel.setBackground(Color.BLACK);
        this.objectiveCardPanel.setBackground(Color.GREEN);

        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.1;
        add(hideButtonPanel, gbc);

        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 0.2;
        add(objectiveCardPanel, gbc);

        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.weighty = 0.7;
        add(cardsPanel, gbc);
    }

    public void updateCurrentPlayerCards(ObjectiveCard objectiveCard, List<GameCard> cards) {

    }

    private class HideButtonPanel extends JPanel {
        public HideButtonPanel() {

        }
    }

    private class ObjectiveCardPanel extends JPanel {
        public ObjectiveCardPanel() {

        }
    }
}
