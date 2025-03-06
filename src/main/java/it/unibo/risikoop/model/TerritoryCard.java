package it.unibo.risikoop.model;

public interface TerritoryCard {
    public enum CardType {
        CARD_A,
        CARD_B,
        CARD_C
    }

    public CardType getCardType();

    /**
     * 
     * @return
     */
    public Territory getAssociatedTerritory();

}
