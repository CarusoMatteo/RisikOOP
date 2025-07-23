package it.unibo.risikoop.controller.implementations;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import it.unibo.risikoop.controller.interfaces.CardGameController;
import it.unibo.risikoop.model.implementations.gamecards.CardDeckImpl;
import it.unibo.risikoop.model.implementations.gamecards.combos.ComboCheckerImpl;
import it.unibo.risikoop.model.interfaces.CardDeck;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.cards.GameCard;
import it.unibo.risikoop.model.interfaces.cards.TerritoryCard;

/**
 * Controller for managing card game operations such as drawing cards and
 * finding combos.
 * This class implements the CardGameController interface and provides methods
 * to interact
 * with the card deck and player combos.
 */
public final class CardGameControllerImpl implements CardGameController {

    private final CardDeck deck;
    private final ComboCheckerImpl comboChecker = new ComboCheckerImpl();

    /**
     * Constructs a CardGameController with the specified GameManager.
     * Initializes the card deck with the territories from the GameManager.
     *
     * @param gameManager the GameManager that manages the game state
     */
    public CardGameControllerImpl(final GameManager gameManager) {
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
    public boolean isComboValid(final Set<GameCard> cards) {
        if (cards == null || cards.size() != 3) {
            throw new IllegalArgumentException("Set must contain 3 cards.");
        }

        return comboChecker.useCombo(cards).isPresent();
    }

    @Override
    public void useCombo(final Player player, final Set<GameCard> cards) {
        if (player == null || player.getGameCards() == null) {
            throw new IllegalArgumentException("Player or player's hand cannot be null.");
        }

        final Optional<Integer> comboReward = comboChecker.useCombo(cards);
        if (!comboReward.isPresent()) {
            throw new IllegalStateException("A non valid combo was used.");
        }

        final List<Territory> ownedTerritories = List.copyOf(player.getTerritories());
        final int territoryReward = cards.stream()
                .filter(GameCard::isTerritoryCard)
                .map(card -> ((TerritoryCard) card).getAssociatedTerritory())
                .filter(ownedTerritories::contains)
                .mapToInt(t -> 2)
                .sum();

        player.addUnitsToPlace(comboReward.get() + territoryReward);
        player.getHand().removeCards(cards);
    }
}
