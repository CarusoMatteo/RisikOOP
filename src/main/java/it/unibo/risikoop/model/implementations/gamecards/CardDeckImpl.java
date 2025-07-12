package it.unibo.risikoop.model.implementations.gamecards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import it.unibo.risikoop.model.implementations.gamecards.territorycard.TerritoryCardImpl;
import it.unibo.risikoop.model.implementations.gamecards.territorycard.WildCardImpl;
import it.unibo.risikoop.model.interfaces.CardDeck;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.cards.GameCard;
import it.unibo.risikoop.model.interfaces.cards.UnitType;

/**
 * This class represents the deck of cards in the game.
 */
public final class CardDeckImpl implements CardDeck {

    private static final int DEFAULT_PERCENTAGE = 33;
    private static final int DEFAULT_NUMBER_OF_WILD_CARDS = 2;
    private static final int DEFAULT_ARTILLERY_PERCENTAGE = 34; // 33 + 1 to ensure total is 100
    private static final int SUM_OF_PERCENTAGES = 100;
    private final List<GameCard> deck;
    private final Set<Territory> territories;
    private final int percentageOfJack;
    private final int percentageOfKnight;
    // private final int percentageOfArtillery;
    private final int numberOfWildCards;

    /**
     * Constructor for CardDeckImpl.
     * 
     * @param territories
     * @param percentageOfJack
     * @param percentageOfKnight
     * @param percentageOfArtillery
     * @param numberOfWildCards
     */
    public CardDeckImpl(
            final Set<Territory> territories,
            final int percentageOfJack,
            final int percentageOfKnight,
            final int percentageOfArtillery,
            final int numberOfWildCards) {
        this.territories = territories;

        if (percentageOfJack < 0 || percentageOfKnight < 0
                || percentageOfArtillery < 0 || numberOfWildCards < 0) {
            throw new IllegalArgumentException("Percentages and number of wild cards must be non-negative");
        }

        if (percentageOfJack + percentageOfKnight + percentageOfArtillery != SUM_OF_PERCENTAGES) {
            throw new IllegalArgumentException("The sum of percentages must equal 100");
        }

        this.percentageOfJack = percentageOfJack;
        this.percentageOfKnight = percentageOfKnight;
        // this.percentageOfArtillery = percentageOfArtillery;
        this.numberOfWildCards = numberOfWildCards;
        deck = createDeck();
    }

    /**
     * Default constructor for CardDeckImpl, that uses data from the official Risiko
     * game.
     * 
     * @param territories
     */
    public CardDeckImpl(final Set<Territory> territories) {
        this(territories,
                DEFAULT_PERCENTAGE,
                DEFAULT_PERCENTAGE,
                DEFAULT_ARTILLERY_PERCENTAGE,
                DEFAULT_NUMBER_OF_WILD_CARDS);
    }

    @Override
    public boolean addCards(final Set<GameCard> card) {
        return deck.addAll(card);
    }

    @Override
    public GameCard drawCard() {
        if (deck.isEmpty()) {
            throw new IllegalStateException("No cards left in the deck");
        }
        return deck.remove(deck.size() - 1); // Draw the last card
    }

    @Override
    public boolean isEmpty() {
        return deck.isEmpty();
    }

    private List<GameCard> createDeck() {
        // 1. Copy and shuffle the territories
        final List<Territory> shuffledTerritories = new ArrayList<>(territories);
        Collections.shuffle(shuffledTerritories);

        final int totalTerritories = shuffledTerritories.size();

        // 2. Calc the number of each type of card based on percentages
        final int jackCount = totalTerritories * percentageOfJack / SUM_OF_PERCENTAGES;
        final int knightCount = totalTerritories * percentageOfKnight / SUM_OF_PERCENTAGES;
        final int artilleryCount = totalTerritories - jackCount - knightCount;

        // 3. Assegna i tipi ai primi N territori
        final List<GameCard> cards = new ArrayList<>(totalTerritories + numberOfWildCards);
        int idx = 0;
        for (int i = 0; i < jackCount; i++, idx++) {
            cards.add(new TerritoryCardImpl(UnitType.JACK, shuffledTerritories.get(idx)));
        }
        for (int i = 0; i < knightCount; i++, idx++) {
            cards.add(new TerritoryCardImpl(UnitType.KNIGHT, shuffledTerritories.get(idx)));
        }
        for (int i = 0; i < artilleryCount; i++, idx++) {
            cards.add(new TerritoryCardImpl(UnitType.ARTILLERY, shuffledTerritories.get(idx)));
        }

        // 4. Aggiunge le carte Wild (Jolly)
        for (int i = 0; i < numberOfWildCards; i++) {
            cards.add(new WildCardImpl());
        }

        // 5. Mescola il mazzo completo e restituisci
        Collections.shuffle(cards);
        return cards;
    }

}
