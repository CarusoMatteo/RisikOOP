package it.unibo.risikoop.view.implementations.scenes.mapscene.cardpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import it.unibo.risikoop.controller.interfaces.Controller;
import it.unibo.risikoop.model.interfaces.ObjectiveCard;
import it.unibo.risikoop.model.interfaces.cards.GameCard;

/**
 * Panel to display the Cards in the MapScene.
 * Initial state is with cards hidden.
 */
public final class CardJpanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private static final double BUTTON_PROPORTIONS = 0.1;
    private static final double SMALL_PANEL_PROPORTIONS = 0.2;
    private static final double BIG_PANEL_PROPORTIONS = 0.6;

    private static final int OBJECTIVE_PANEL_BACKGROUND_COLOR = 0x23241E;

    private final HideButtonJPanel hideButtonPanel;
    private final ObjectiveCardJPanel objectiveCardPanel;
    private final CardsListJPanel cardsListPanel;
    private final PlayComboJPanel playComboPanel;

    private boolean isInfoVisible;

    /**
     * Constructor for CardJpanel.
     * 
     * @param objectiveCard The objective card to display for the first time.
     * @param cards         The list of game cards to display for the first time.
     * @param controller    The controller to retrieve the player's cards and to
     *                      check if a combo is vaild.
     */
    public CardJpanel(final ObjectiveCard objectiveCard, final List<GameCard> cards, final Controller controller) {
        this.hideButtonPanel = new HideButtonJPanel();
        this.objectiveCardPanel = new ObjectiveCardJPanel(objectiveCard);
        this.playComboPanel = new PlayComboJPanel();
        this.cardsListPanel = new CardsListJPanel(cards, controller, playComboPanel.getPlayComboButton());

        setupPanels();
        hideInfo();
    }

    private void setupPanels() {
        this.setLayout(new GridBagLayout());

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        this.objectiveCardPanel.setBackground(new Color(OBJECTIVE_PANEL_BACKGROUND_COLOR));

        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = BUTTON_PROPORTIONS;
        add(hideButtonPanel, gbc);

        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = SMALL_PANEL_PROPORTIONS;
        add(objectiveCardPanel, gbc);

        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.weighty = BIG_PANEL_PROPORTIONS;
        add(cardsListPanel, gbc);

        gbc.gridy = 3;
        gbc.weightx = 1;
        gbc.weighty = BUTTON_PROPORTIONS;
        add(playComboPanel, gbc);
    }

    /**
     * Update the current player's objective card and cards.
     * 
     * @param objectiveCard The objective card to display.
     * @param cards         The list of game cards to display.
     */
    public void updateCurrentPlayerCards(final ObjectiveCard objectiveCard, final List<GameCard> cards) {
        this.objectiveCardPanel.updateObjectiveCard(objectiveCard);
        this.cardsListPanel.updateCards(List.copyOf(cards));
        hideInfo();
    }

    private void toggleInfoVisibility() {
        if (isInfoVisible) {
            hideInfo();
        } else {
            showInfo();
        }
    }

    /**
     * Hide objective card description and the cards.
     */
    private void hideInfo() {
        this.objectiveCardPanel.hideInfo();
        this.cardsListPanel.hideInfo();

        isInfoVisible = false;
        this.hideButtonPanel.toggleText(isInfoVisible);
    }

    /**
     * Show objective card description and the cards.
     */
    private void showInfo() {
        this.objectiveCardPanel.showInfo();
        this.cardsListPanel.showInfo();

        isInfoVisible = true;
        this.hideButtonPanel.toggleText(isInfoVisible);
    }

    /**
     * Class to create a panel with a button to toggle the cards' visibility.
     */
    private final class HideButtonJPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        private static final String HIDE_CARDS_TEXT = "Hide Cards";
        private static final String SWOW_CARDS_TEXT = "Show Cards";

        private final JButton hideButton;

        /**
         * Constructor for HideButtonJPanel.
         */
        HideButtonJPanel() {
            this.setLayout(new BorderLayout());

            this.hideButton = new JButton(SWOW_CARDS_TEXT);
            this.hideButton.addActionListener(e -> toggleInfoVisibility());
            this.hideButton.setPreferredSize(new Dimension(1, 1));
            this.hideButton.setMinimumSize(new Dimension(1, 1));

            this.add(this.hideButton, BorderLayout.CENTER);
        }

        /**
         * Toggle the text of the button based on the visibility state.
         * 
         * @param newVisibility The new visibility state.
         */
        public void toggleText(final boolean newVisibility) {
            if (newVisibility) {
                this.hideButton.setText(HIDE_CARDS_TEXT);
            } else {
                this.hideButton.setText(SWOW_CARDS_TEXT);
            }
        }
    }

    /**
     * Class to create a panel with a button to play the card combo.
     */
    private final class PlayComboJPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        private static final String PLAY_COMBO_TEXT = "Play combo";
        private final JButton playComboButton;

        /**
         * Constructor for PlayComboJPanel.
         */
        PlayComboJPanel() {
            this.setLayout(new BorderLayout());
            this.playComboButton = new JButton(PLAY_COMBO_TEXT);
            this.playComboButton.setPreferredSize(new Dimension(1, 1));
            this.playComboButton.setMinimumSize(new Dimension(1, 1));
            this.playComboButton.setEnabled(false);
            this.playComboButton.addActionListener(e -> {
                cardsListPanel.playCombo();
            });
            this.add(this.playComboButton, BorderLayout.CENTER);
        }

        /**
         * Returns the button to play the combo.
         * 
         * @return the button to play the combo.
         */
        public JButton getPlayComboButton() {
            return this.playComboButton;
        }
    }
}
