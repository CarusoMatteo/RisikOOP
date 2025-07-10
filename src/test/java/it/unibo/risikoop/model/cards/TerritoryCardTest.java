package it.unibo.risikoop.model.cards;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.risikoop.model.implementations.Color;
import it.unibo.risikoop.model.implementations.GameManagerImpl;
import it.unibo.risikoop.model.implementations.PlayerGameContext;
import it.unibo.risikoop.model.implementations.PlayerImpl;
import it.unibo.risikoop.model.implementations.TerritoryImpl;
import it.unibo.risikoop.model.implementations.gamecards.territorycard.TerritoryCardImpl;
import it.unibo.risikoop.model.implementations.gamecards.territorycard.WildCardImpl;
import it.unibo.risikoop.model.implementations.specification.ConquerTerritoriesSpec;
import it.unibo.risikoop.model.implementations.specification.KillPlayerSpec;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.cards.GameCard;
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
    private GameManager gameManager;

    @BeforeEach
    void setUp() {
        gameManager = new GameManagerImpl();
        type = UnitType.INFANTRY;
        owner = new PlayerImpl("Player1", new Color(0, 2, 0));
        territory = new TerritoryImpl(gameManager, "TerritoryTest");
    }

    @Test
    void testTerritoryCard() {
        final TerritoryCard territoryCard = new TerritoryCardImpl(type, owner, territory);
        assertEquals(type, territoryCard.getType());
        assertEquals(owner, territoryCard.getOwner());
        assertEquals(territory, territoryCard.getAssociatedTerritory());

        assertThrows(IllegalArgumentException.class, () -> {
            new TerritoryCardImpl(UnitType.WILD, owner, territory);
        });
    }

    @Test
    void testWildCard() {
        final WildCard territoryCard = new WildCardImpl(owner);
        assertEquals(UnitType.WILD, territoryCard.getType());
        assertEquals(owner, territoryCard.getOwner());
    }

}
