package it.unibo.risikoop.view.implementations.scenes.mapscene.cardpanel;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import it.unibo.risikoop.controller.interfaces.Controller;
import it.unibo.risikoop.controller.interfaces.GamePhaseController;
import it.unibo.risikoop.model.interfaces.cards.GameCard;
import it.unibo.risikoop.model.interfaces.cards.TerritoryCard;

/**
 * JPanel to display a list of cards within a ScrollPane.
 * Allows selection of cards with checkboxes.
 */
public final class CardsListJPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private final transient Controller controller;
    private final JButton playComboButton;
    private final JScrollPane scrollPane;
    private final CardEntryListJPanel cardEntryListPanel;
    private final transient Set<GameCard> selectedCards = new HashSet<>();

    /**
     * Constructor for CardsListJPanel.
     * 
     * @param cards           The cards to display in the list for the first time.
     * @param controller      The controller to retrieve the player's cards and to
     *                        check if a combo is vaild.
     * @param playComboButton The button that needs to be enabled to play a combo of
     *                        selected cards.
     *                        Suppressed because the comboButton is not changed.
     */
    @SuppressWarnings("EI_EXPOSE_REP")
    public CardsListJPanel(final List<GameCard> cards,
            final Controller controller,
            final JButton playComboButton) {
        this.setLayout(new GridLayout(1, 1));

        this.setPreferredSize(new Dimension(1, 1));
        this.setMinimumSize(new Dimension(1, 1));

        this.cardEntryListPanel = new CardEntryListJPanel(cards);
        this.scrollPane = new JScrollPane(this.cardEntryListPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(scrollPane);
        this.controller = controller;
        this.playComboButton = playComboButton;

        updateCards(cards);
    }

    /**
     * Updates the list of cards displayed in the panel.
     * 
     * @param cards
     */
    public void updateCards(final List<GameCard> cards) {
        this.cardEntryListPanel.updateCards(cards);
    }

    /**
     * Hides the card list in the panel.
     */
    public void hideInfo() {
        this.cardEntryListPanel.setVisible(false);
    }

    /**
     * Shows the card list in the panel.
     */
    public void showInfo() {
        this.cardEntryListPanel.setVisible(true);
    }

    /**
     * Tracks selection and deselection of cards.
     * Enables the play combo button if three valid cards are selected.
     * 
     * @param card       The card that is being selected or deselected.
     * @param isSelected True if the card was just selected, false if it was just
     *                   deselected.
     */
    public void selectCard(final GameCard card, final boolean isSelected) {
        if (isSelected) {
            this.selectedCards.add(card);
        } else {
            this.selectedCards.remove(card);
        }

        if (selectedCards.size() == 3
                && controller.getCardGameController().isComboValid(selectedCards)
                && isComboPhase()) {
            this.playComboButton.setEnabled(true);
        } else {
            this.playComboButton.setEnabled(false);
        }
    }

    private boolean isComboPhase() {
        return controller.getGamePhaseController().getPhaseKey() == GamePhaseController.PhaseKey.COMBO;
    }

    /**
     * Plays the combo of selected cards.
     * This method should be called when the play combo button is clicked.
     */
    public void playCombo() {
        this.controller.getCardGameController().useCombo(
                controller.getDataRetrieveController().getCurrentPlayer(),
                selectedCards);
        this.updateCards(
                controller.getDataRetrieveController().getCurrentPlayerGameCards());
        this.selectedCards.clear();
        this.playComboButton.setEnabled(false);
    }

    /**
     * Class to display a list of card entries.
     */
    private final class CardEntryListJPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        /**
         * Constructor for CardEntryListJPanel.
         * 
         * @param cards The list of cards to display in the panel for the first time.
         */
        CardEntryListJPanel(final List<GameCard> cards) {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            updateCards(cards);
        }

        /**
         * Updates the list of cards displayed in the panel.
         * 
         * @param cards The new list of cards to display.
         */
        public void updateCards(final List<GameCard> cards) {
            this.removeAll();
            cards.stream().forEach(card -> this.add(new CardEntryJPanel(card)));
        }
    }

    /**
     * Class to display a single card entry with a checkbox, an icon and the name.
     */
    private final class CardEntryJPanel extends JPanel {
        private static final double ICON_ASPECT_RATIO = 1.8;
        private static final long serialVersionUID = 1L;
        private static final int ICON_HEIGHT = 64;

        private final GameCard card;
        private final JCheckBox checkBox;

        /**
         * Constructor for CardEntryJPanel.
         * 
         * @param card The card to display in the entry.
         */
        CardEntryJPanel(final GameCard card) {
            this.setLayout(new FlowLayout(FlowLayout.LEFT));

            this.card = card;

            final StringBuilder resoucePath = new StringBuilder("/cards/");
            switch (card.getType()) {
                case CANNON -> resoucePath.append("cannon.png");
                case JACK -> resoucePath.append("jack.png");
                case KNIGHT -> resoucePath.append("knight.png");
                case WILD -> resoucePath.append("wild.png");
            }
            final ImageIcon resizedIcon = new ImageIcon(new ImageIcon(getClass().getResource(resoucePath.toString()))
                    .getImage()
                    .getScaledInstance((int) (ICON_HEIGHT * ICON_ASPECT_RATIO), ICON_HEIGHT, Image.SCALE_SMOOTH));

            final String text;
            if (card.isTerritoryCard()) {
                text = ((TerritoryCard) card).getAssociatedTerritory().getName();
            } else {
                text = "Wild";
            }

            this.checkBox = new JCheckBox();
            this.checkBox.setHorizontalTextPosition(SwingConstants.RIGHT);
            this.checkBox.addActionListener((e) -> {
                selectCard(this.card, this.checkBox.isSelected());
            });

            this.add(this.checkBox);
            this.add(new JLabel(resizedIcon));
            this.add(new JLabel(text));
        }
    }
}
