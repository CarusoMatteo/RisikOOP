package it.unibo.risikoop.view.implementations.scenes.mapscene.cardpanel;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import it.unibo.risikoop.model.interfaces.cards.GameCard;
import it.unibo.risikoop.model.interfaces.cards.TerritoryCard;

public class CardsListJPanel extends JPanel {
    private final JScrollPane scrollPane;
    private final CardEntryListJPanel cardEntryListPanel;

    public CardsListJPanel(final List<GameCard> cards) {
        this.setLayout(new GridLayout(1, 1));

        this.setPreferredSize(new Dimension(1, 1));
        this.setMinimumSize(new Dimension(1, 1));

        this.cardEntryListPanel = new CardEntryListJPanel(cards);
        this.scrollPane = new JScrollPane(this.cardEntryListPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.add(scrollPane);

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

    private class CardEntryListJPanel extends JPanel {
        // private List<GameCard> currentCards;

        public CardEntryListJPanel(final List<GameCard> cards) {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            updateCards(cards);
        }

        public void updateCards(final List<GameCard> cards) {
            this.removeAll();
            // this.currentCards = List.copyOf(cards);

            for (final GameCard card : cards) {

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
                Icon icon = new ImageIcon(getClass().getResource(resoucePath.toString()));

                String text;
                if (card.isTerritoryCard()) {
                    text = ((TerritoryCard) card).getAssociatedTerritory().getName();
                } else {
                    text = "Wild";
                }

                this.add(new JCheckBox(text/* , icon */));
                // this.add(new CardEntryJPanel(card));
            }
        }

    }

    private class CardEntryJPanel extends JPanel {

        public CardEntryJPanel(final GameCard card) {
            this.setLayout(new FlowLayout(FlowLayout.LEFT));

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
            final JLabel iconLabel = new JLabel(new ImageIcon(getClass().getResource(resoucePath.toString())));
            final JLabel nameLabel = new JLabel();
            if (card.isTerritoryCard()) {
                nameLabel.setText(((TerritoryCard) card).getAssociatedTerritory().getName());
            } else {
                nameLabel.setText("Wild");
            }
            this.add(iconLabel);
            this.add(nameLabel);
        }
    }
}
