package it.unibo.risikoop.model.cards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.risikoop.model.implementations.Color;
import it.unibo.risikoop.model.implementations.GameManagerImpl;
import it.unibo.risikoop.model.implementations.PlayerImpl;
import it.unibo.risikoop.model.implementations.TerritoryImpl;
import it.unibo.risikoop.model.implementations.gamecards.territorycard.TerritoryCardImpl;
import it.unibo.risikoop.model.implementations.gamecards.territorycard.WildCardImpl;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.cards.TerritoryCard;
import it.unibo.risikoop.model.interfaces.cards.UnitType;
import it.unibo.risikoop.model.interfaces.cards.WildCard;

/**
 * Class to test adding Players.
 */
final class TerritoryCardTest {

    private UnitType type;
    private Player owner;
    private Territory territory;

    @BeforeEach
    void setUp() {
        type = UnitType.JACK;
        owner = new PlayerImpl("Player1", new Color(0, 2, 0));
        territory = new TerritoryImpl(new GameManagerImpl(), "TerritoryTest");
    }

    @Test
    void testTerritoryCard() {
        final TerritoryCard territoryCard = new TerritoryCardImpl(type, territory);
        assertEquals(type, territoryCard.getType());
        assertEquals(owner, territoryCard.getOwner());
        assertEquals(territory, territoryCard.getAssociatedTerritory());

        assertThrows(IllegalArgumentException.class, () -> {
            new TerritoryCardImpl(UnitType.WILD, territory);
        });
    }

    @Test
    void testWildCard() {
        final WildCard territoryCard = new WildCardImpl();
        assertEquals(UnitType.WILD, territoryCard.getType());
        assertEquals(owner, territoryCard.getOwner());
    }

}
