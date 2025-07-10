package it.unibo.risikoop.controller.interfaces;

import java.util.List;

import it.unibo.risikoop.model.interfaces.Combo;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.cards.GameCard;

/**
 * Controller interface for managing card game operations in RisikOOP.
 * This interface defines methods for drawing cards, finding combos, and using combos.
 */
public interface CardGameController {

    /**
     * Draws a card from the deck.
     *
     * @return the drawn GameCard
     */
    GameCard drawCard();


    /**
     * Finds all combos available for the specified player.
     *
     * @param player the player for whom to find combos
     * @return a list of available combos for the player
     */
    List<Combo> findCombos(Player player);


    /**
     * Uses a combo for the specified player.
     *
     * @param player the player who is using the combo
     * @param combo  the combo to be used
     */
    void useCombo(Player player, Combo combo);
}