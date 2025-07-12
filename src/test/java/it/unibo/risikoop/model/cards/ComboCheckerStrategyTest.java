package it.unibo.risikoop.model.cards;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import it.unibo.risikoop.model.implementations.GameManagerImpl;
import it.unibo.risikoop.model.implementations.PlayerHandImpl;
import it.unibo.risikoop.model.implementations.TerritoryImpl;
import it.unibo.risikoop.model.implementations.gamecards.combos.AllDifferentCombo;
import it.unibo.risikoop.model.implementations.gamecards.combos.WildAllEqualCombo;
import it.unibo.risikoop.model.implementations.gamecards.territorycard.TerritoryCardImpl;
import it.unibo.risikoop.model.implementations.gamecards.territorycard.WildCardImpl;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.PlayerHand;
import it.unibo.risikoop.model.interfaces.cards.ComboCheckStrategy;
import it.unibo.risikoop.model.interfaces.cards.UnitType;

public class ComboCheckerStrategyTest {

    final GameManager gameManager = new GameManagerImpl();

    @Test
    void testWildAllEqualCombo() {
        final ComboCheckStrategy combo = new WildAllEqualCombo();
        final PlayerHand hand = new PlayerHandImpl();

        // Three nw -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, ""))));
        assertTrue(!combo.comboIsPossibile(hand));
        assertTrue(!combo.comboIsValid(hand.getCards()));
        hand.clear();

        // One wild and two nw_different -> valid
        hand.addCards(Set.of(
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, ""))));
        assertTrue(combo.comboIsPossibile(hand));
        assertTrue(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // One wild and two nw_equal -> not valid
        hand.addCards(Set.of(
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, ""))));
        assertTrue(!combo.comboIsPossibile(hand));
        assertTrue(!combo.comboIsValid(hand.getCards()));
        hand.clear();

        // Two wild and one nw -> not valid
        hand.addCards(Set.of(
                new WildCardImpl(),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, ""))));
        assertTrue(!combo.comboIsPossibile(hand));
        assertTrue(!combo.comboIsValid(hand.getCards()));
        hand.clear();

        // Three wild -> not valid
        hand.addCards(Set.of(
                new WildCardImpl(),
                new WildCardImpl(),
                new WildCardImpl()));
        assertTrue(!combo.comboIsPossibile(hand));
        assertTrue(!combo.comboIsValid(hand.getCards()));
        hand.clear();

        // Many cards and valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new WildCardImpl(),
                new WildCardImpl()));

        assertTrue(combo.comboIsPossibile(hand));
        assertThrows(IllegalArgumentException.class, () -> combo.comboIsValid(hand.getCards()));
        hand.clear();

        // Many cards but not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new WildCardImpl(),
                new WildCardImpl()));
        assertTrue(!combo.comboIsPossibile(hand));
        assertThrows(IllegalArgumentException.class, () -> combo.comboIsValid(hand.getCards()));
        hand.clear();
    }

    @Test
    void testAllDifferentCombo() {
        final ComboCheckStrategy combo = new AllDifferentCombo();
        final PlayerHand hand = new PlayerHandImpl();

        // a,c,i -> valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, ""))));
        assertTrue(combo.comboIsPossibile(hand));
        assertTrue(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // w,c,i -> not valid
        hand.addCards(Set.of(
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, ""))));
        assertTrue(!combo.comboIsPossibile(hand));
        assertTrue(!combo.comboIsValid(hand.getCards()));
        hand.clear();

        // a,w,i -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, ""))));
        assertTrue(!combo.comboIsPossibile(hand));
        assertTrue(!combo.comboIsValid(hand.getCards()));
        hand.clear();

        // a,c,w -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new WildCardImpl()));
        assertTrue(!combo.comboIsPossibile(hand));
        assertTrue(!combo.comboIsValid(hand.getCards()));
        hand.clear();

        // a,a,i -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, ""))));
        assertTrue(!combo.comboIsPossibile(hand));
        assertTrue(!combo.comboIsValid(hand.getCards()));
        hand.clear();

        // a,c,c -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, ""))));
        assertTrue(!combo.comboIsPossibile(hand));
        assertTrue(!combo.comboIsValid(hand.getCards()));
        hand.clear();

        // a,i,i -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, ""))));
        assertTrue(!combo.comboIsPossibile(hand));
        assertTrue(!combo.comboIsValid(hand.getCards()));
        hand.clear();

        // w,w,i -> not valid
        hand.addCards(Set.of(
                new WildCardImpl(),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, ""))));
        assertTrue(!combo.comboIsPossibile(hand));
        assertTrue(!combo.comboIsValid(hand.getCards()));
        hand.clear();

        // a,a,a -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, ""))));
        assertTrue(!combo.comboIsPossibile(hand));
        assertTrue(!combo.comboIsValid(hand.getCards()));
        hand.clear();

        // c,c,c -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, ""))));
        assertTrue(!combo.comboIsPossibile(hand));
        assertTrue(!combo.comboIsValid(hand.getCards()));
        hand.clear();

        // i,i,i -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, ""))));
        assertTrue(!combo.comboIsPossibile(hand));
        assertTrue(!combo.comboIsValid(hand.getCards()));
        hand.clear();

        // w,w,w -> not valid
        hand.addCards(Set.of(
                new WildCardImpl(),
                new WildCardImpl(),
                new WildCardImpl()));
        assertTrue(!combo.comboIsPossibile(hand));
        assertTrue(!combo.comboIsValid(hand.getCards()));
        hand.clear();

        // many cards and valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new WildCardImpl(),
                new WildCardImpl()));
        assertTrue(combo.comboIsPossibile(hand));
        assertThrows(IllegalArgumentException.class, () -> combo.comboIsValid(hand.getCards()));
        hand.clear();

        // many cards but not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new WildCardImpl(),
                new WildCardImpl()));
        assertTrue(!combo.comboIsPossibile(hand));
        assertThrows(IllegalArgumentException.class, () -> combo.comboIsValid(hand.getCards()));
        hand.clear();

    }

    @Test
    void testAllCavarlyEqualCombo() {
    }

    @Test
    void testAllInfantryEqualCombo() {
    }

    @Test
    void testAllArtilleryEqualCombo() {
    }
}
