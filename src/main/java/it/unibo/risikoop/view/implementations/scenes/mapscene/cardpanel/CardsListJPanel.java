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
import it.unibo.risikoop.model.interfaces.cards.GameCard;
import it.unibo.risikoop.model.interfaces.cards.TerritoryCard;

public class CardsListJPanel extends JPanel {
    private final Controller controller;
    private final JButton playComboButton;
    private final JScrollPane scrollPane;
    private final CardEntryListJPanel cardEntryListPanel;
    private Set<GameCard> selectedCards = new HashSet<>();

    public CardsListJPanel(final List<GameCard> cards, final Controller controller,
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

    public void updateCards(final List<GameCard> cards) {
        this.cardEntryListPanel.updateCards(cards);
    }

    public void hideInfo() {
        System.out.println("Hide -> Hiding all cards.");
        this.cardEntryListPanel.setVisible(false);
    }

    public void showInfo() {
        System.out.println("Show -> Showing all cards.");
        this.cardEntryListPanel.setVisible(true);
    }

    public void selectCard(final GameCard card, final boolean isSelected) {
        if (isSelected) {
            System.out.println("Select -> Card " + card.getType() + " was selected.");
            this.selectedCards.add(card);
        } else {
            System.out.println("Deselect -> Card " + card.getType() + " was deselected.");
            this.selectedCards.remove(card);
        }

        if (selectedCards.size() == 3 && controller.getCardGameController().isComboValid(selectedCards)) {
            System.out.println("Select -> Combo is valid, enabling button.");
            this.playComboButton.setEnabled(true);
        } else {
            this.playComboButton.setEnabled(false);
        }
    }

    public void playCombo() {
        System.out.println("Play Combo -> Playing combo with selected cards: " + selectedCards);
        this.controller.getCardGameController().useCombo(
                controller.getDataRetrieveController().getCurrentPlayer(),
                selectedCards);
        this.updateCards(
                controller.getDataRetrieveController().getCurrentPlayerGameCards());
    }

    private class CardEntryListJPanel extends JPanel {

        public CardEntryListJPanel(final List<GameCard> cards) {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            updateCards(cards);
        }

        public void updateCards(final List<GameCard> cards) {
            this.removeAll();
            cards.stream().forEach(card -> this.add(new CardEntryJPanel(card)));
            this.revalidate();
        }
    }

    private class CardEntryJPanel extends JPanel {
        private static final int ICON_HEIGHT = 64;

        private final GameCard card;
        private final JCheckBox checkBox;

        public CardEntryJPanel(final GameCard card) {
            this.setLayout(new FlowLayout(FlowLayout.LEFT));

            this.card = card;

            final StringBuilder resoucePath = new StringBuilder("/cards/");
            switch (card.getType()) {
                case CANNON:
                    resoucePath.append("cannon.png");
                    break;
                case JACK:
                    resoucePath.append("jack.png");
                    break;
                case KNIGHT:
                    resoucePath.append("knight.png");
                    break;
                case WILD:
                    resoucePath.append("wild.png");
                    break;
            }
            ImageIcon icon = new ImageIcon(getClass().getResource(resoucePath.toString()));
            Image scaledImage = icon.getImage()
                    .getScaledInstance((int) (ICON_HEIGHT * 1.8), (int) ICON_HEIGHT, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(scaledImage);

            String text;
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
