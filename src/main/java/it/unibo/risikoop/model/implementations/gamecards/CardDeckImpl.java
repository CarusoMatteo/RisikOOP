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

public class CardDeckImpl implements CardDeck {

    private static final int DEFAULT_PERCENTAGE = 33;
    private static final int DEFAULT_NUMBER_OF_WILD_CARDS = 2;
    private static final int DEFAULT_ARTILLERY_PERCENTAGE = 34; // 33 + 1 to ensure total is 100
    private static final int SUM_OF_PERCENTAGES = 100;
    private final List<GameCard> deck;
    private final Set<Territory> territories;
    private final int percentageOfInfantry;
    private final int percentageOfCavalry;
    // private final int percentageOfArtillery;
    private final int numberOfWildCards;

    public CardDeckImpl(
            Set<Territory> territories,
            int percentageOfInfantry,
            int percentageOfCavalry,
            int percentageOfArtillery,
            int numberOfWildCards) {
        this.territories = territories;

        if (percentageOfInfantry < 0 || percentageOfCavalry < 0
                || percentageOfArtillery < 0 || numberOfWildCards < 0) {
            throw new IllegalArgumentException("Percentages and number of wild cards must be non-negative");
        }

        if (percentageOfInfantry + percentageOfCavalry + percentageOfArtillery != SUM_OF_PERCENTAGES) {
            throw new IllegalArgumentException("The sum of percentages must equal 100");
        }

        this.percentageOfInfantry = percentageOfInfantry;
        this.percentageOfCavalry = percentageOfCavalry;
        // this.percentageOfArtillery = percentageOfArtillery;
        this.numberOfWildCards = numberOfWildCards;
        deck = createDeck();
    }

    public CardDeckImpl(Set<Territory> territories) {
        this(
                territories,
                DEFAULT_PERCENTAGE,
                DEFAULT_PERCENTAGE,
                DEFAULT_ARTILLERY_PERCENTAGE,
                DEFAULT_NUMBER_OF_WILD_CARDS);
    }

    @Override
    public boolean addCards(Set<GameCard> card) {
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
        List<Territory> shuffledTerritories = new ArrayList<>(territories);
        Collections.shuffle(shuffledTerritories);

        int totalTerritories = shuffledTerritories.size();

        // 2. Calc the number of each type of card based on percentages
        int infantryCount = totalTerritories * percentageOfInfantry / SUM_OF_PERCENTAGES;
        int cavalryCount = totalTerritories * percentageOfCavalry / SUM_OF_PERCENTAGES;
        int artilleryCount = totalTerritories - infantryCount - cavalryCount;

        // 3. Assegna i tipi ai primi N territori
        List<GameCard> cards = new ArrayList<>(totalTerritories + numberOfWildCards);
        int idx = 0;
        for (int i = 0; i < infantryCount; i++, idx++) {
            cards.add(new TerritoryCardImpl(UnitType.INFANTRY, shuffledTerritories.get(idx)));
        }
        for (int i = 0; i < cavalryCount; i++, idx++) {
            cards.add(new TerritoryCardImpl(UnitType.CAVALRY, shuffledTerritories.get(idx)));
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
