package it.unibo.risikoop.controller.interfaces;

import java.util.Set;

import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.cards.GameCard;

/**
 * Controller interface for managing card game operations in RisikOOP.
 * This interface defines methods for drawing cards, finding combos, and using
 * combos.
 */
public interface CardGameController {

    /**
     * Draws a card from the deck.
     *
     * @return the drawn GameCard
     */
    GameCard drawCard();

    /**
     * Finds if the player can use any combo.
     *
     * @param player the player for whom to find combos.
     * @return if the player can use any combo.
     */
    Boolean canPlayAnyCombo(Player player);

    /**
     * Uses a combo for the specified player.
     *
     * @param player the player who is using the combo.
     * @param cards  the combo to be used.
     */
    void useCombo(Player player, Set<GameCard> cards);
    // Togli le carte riinserendole nel mazzo, aggiunge le unità al giocatore
}
