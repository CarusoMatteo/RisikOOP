package it.unibo.risikoop.view.implementations.scenes.mapscene.cardpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import it.unibo.risikoop.controller.interfaces.CardGameController;
import it.unibo.risikoop.controller.interfaces.Controller;
import it.unibo.risikoop.model.interfaces.ObjectiveCard;
import it.unibo.risikoop.model.interfaces.cards.GameCard;

/**
 * Panel to display the Cards in the MapScene.
 * Initial state is with cards hidden.
 */
public final class CardJpanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private final CardGameController controller;

    private final HideButtonJPanel hideButtonPanel;
    private final ObjectiveCardJPanel objectiveCardPanel;
    private final CardsListJPanel cardsListPanel;
    private final PlayComboJPanel playComboPanel;

    private boolean isInfoVisible = false;

    public CardJpanel(final ObjectiveCard objectiveCard, final List<GameCard> cards, Controller controller) {
        this.hideButtonPanel = new HideButtonJPanel();
        this.objectiveCardPanel = new ObjectiveCardJPanel(objectiveCard);
        this.cardsListPanel = new CardsListJPanel(cards);
        this.playComboPanel = new PlayComboJPanel();

        this.controller = controller.getCardGameController();

        setupPanels();
        hideInfo();
    }

    private void setupPanels() {
        this.setLayout(new GridBagLayout());

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        this.objectiveCardPanel.setBackground(new Color(0x23241E));

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
        gbc.weighty = 0.6;
        add(cardsListPanel, gbc);

        gbc.gridy = 3;
        gbc.weightx = 1;
        gbc.weighty = 0.1;
        add(playComboPanel, gbc);
    }

    public void updateCurrentPlayerCards(final ObjectiveCard objectiveCard, final List<GameCard> cards) {
        this.objectiveCardPanel.updateObjectiveCard(objectiveCard);
        this.cardsListPanel.updateCards(List.copyOf(cards));
        hideInfo();
    }

    private void toggleInfoVisibility() {
        System.out.println("Toggle -> Toggling info visibility.");

        if (isInfoVisible) {
            hideInfo();
        } else {
            showInfo();
        }
    }

    /**
     * Hide objective card description.
     * Hide the cards.
     */
    private void hideInfo() {
        System.out.println("Hide -> Hiding everything");

        this.objectiveCardPanel.hideInfo();
        this.cardsListPanel.hideInfo();

        isInfoVisible = false;
        this.hideButtonPanel.toggleText(isInfoVisible);
    }

    /**
     * Show objective card description.
     * Show the cards.
     */
    private void showInfo() {
        this.objectiveCardPanel.showInfo();
        this.cardsListPanel.showInfo();

        isInfoVisible = true;
        this.hideButtonPanel.toggleText(isInfoVisible);
    }

    private class HideButtonJPanel extends JPanel {
        private static final String HIDE_CARDS_TEXT = "Hide Cards";
        private static final String SWOW_CARDS_TEXT = "Show Cards";

        private final JButton hideButton;

        public HideButtonJPanel() {
            this.setLayout(new BorderLayout());

            this.hideButton = new JButton(SWOW_CARDS_TEXT);
            this.hideButton.addActionListener(e -> toggleInfoVisibility());
            this.hideButton.setPreferredSize(new Dimension(1, 1));
            this.hideButton.setMinimumSize(new Dimension(1, 1));

            this.add(this.hideButton, BorderLayout.CENTER);
        }

        public void toggleText(boolean newVisibility) {
            if (newVisibility) {
                this.hideButton.setText(HIDE_CARDS_TEXT);
            } else {
                this.hideButton.setText(SWOW_CARDS_TEXT);
            }
        }
    }

    private class PlayComboJPanel extends JPanel {
        private static final String PLAY_COMBO_TEXT = "Play combo";
        private final JButton playComboButton;

        public PlayComboJPanel() {
            this.setLayout(new BorderLayout());
            this.playComboButton = new JButton(PLAY_COMBO_TEXT);
            this.playComboButton.setPreferredSize(new Dimension(1, 1));
            this.playComboButton.setMinimumSize(new Dimension(1, 1));
            this.add(this.playComboButton, BorderLayout.CENTER);
        }
    }
}
