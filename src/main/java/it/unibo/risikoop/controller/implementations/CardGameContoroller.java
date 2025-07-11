package it.unibo.risikoop.controller.implementations;

import java.util.List;

import it.unibo.risikoop.controller.interfaces.CardGameController;
import it.unibo.risikoop.model.implementations.gamecards.CardDeckImpl;
import it.unibo.risikoop.model.interfaces.CardDeck;
import it.unibo.risikoop.model.interfaces.Combo;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.cards.GameCard;

/**
 * Controller for managing card game operations such as drawing cards and
 * finding combos.
 * This class implements the CardGameController interface and provides methods
 * to interact
 * with the card deck and player combos.
 */
public class CardGameContoroller implements CardGameController {

    private final CardDeck deck;
    private final GameManager gameManager;

    /**
     * Constructs a CardGameController with the specified GameManager.
     * Initializes the card deck with the territories from the GameManager.
     *
     * @param gameManager the GameManager that manages the game state
     */
    public CardGameContoroller(GameManager gameManager) {
        this.gameManager = gameManager;
        deck = new CardDeckImpl(gameManager.getTerritories());
    }

    @Override
    public GameCard drawCard() {
        if (!deck.isEmpty()) {
            return deck.drawCard();
        } else {
            throw new IllegalStateException("The deck is empty, cannot draw a card.");
        }
    }

    @Override
    public List<Combo> findCombos(Player player) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void useCombo(Player player, Combo combo) {
        // TODO Auto-generated method stub

    }
}
