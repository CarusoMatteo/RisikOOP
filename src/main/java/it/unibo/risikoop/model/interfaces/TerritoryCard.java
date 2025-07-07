package it.unibo.risikoop.model.interfaces;

/**
 * territory cards.
 */
public interface TerritoryCard {
    /**
     * types of card.
     */
    enum CardType {
        /**
         * 
         */
        CARD_A,
        /**
         * 
         */
        CARD_B,
        /**
         * 
         */
        CARD_C
    }

    /**
     * 
     * @return the types of this card.
     */
    CardType getCardType();

    /**
     * 
     * @return the territory associated with
     */
    Territory getAssociatedTerritory();
}
