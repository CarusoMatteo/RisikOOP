package it.unibo.risikoop.view.implementations.scenes.mapscene.cardpanel;

import java.util.List;

import javax.swing.JPanel;

import it.unibo.risikoop.model.interfaces.cards.GameCard;

public class CardsListJPanel extends JPanel {
    private List<GameCard> currentCards;

    public CardsListJPanel(List<GameCard> cards) {
        updateCards(cards);
    }

    public void updateCards(List<GameCard> cards) {
        this.currentCards = List.copyOf(cards);
    }

	public void hideInfo() {
        System.out.println("Hide -> Hiding all cards.");

		// TODO Auto-generated method stub
	}

	public void showInfo() {
        System.out.println("Show -> Showing all cards.");

        // TODO Auto-generated method stub
	}
}
