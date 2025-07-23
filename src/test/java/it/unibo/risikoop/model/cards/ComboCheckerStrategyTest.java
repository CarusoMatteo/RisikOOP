package it.unibo.risikoop.model.cards;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.risikoop.model.implementations.GameManagerImpl;
import it.unibo.risikoop.model.implementations.PlayerHandImpl;
import it.unibo.risikoop.model.implementations.TerritoryImpl;
import it.unibo.risikoop.model.implementations.gamecards.combos.AllCannonEqualCombo;
import it.unibo.risikoop.model.implementations.gamecards.combos.AllKnightEqualCombo;
import it.unibo.risikoop.model.implementations.gamecards.combos.AllDifferentCombo;
import it.unibo.risikoop.model.implementations.gamecards.combos.AllJackEqualCombo;
import it.unibo.risikoop.model.implementations.gamecards.combos.WildAllEqualCombo;
import it.unibo.risikoop.model.implementations.gamecards.territorycard.TerritoryCardImpl;
import it.unibo.risikoop.model.implementations.gamecards.territorycard.WildCardImpl;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.PlayerHand;
import it.unibo.risikoop.model.interfaces.cards.ComboCheckStrategy;
import it.unibo.risikoop.model.interfaces.cards.UnitType;

/**
 * Test class for every ComboCheckerStrategy.
 */
final class ComboCheckerStrategyTest {

    private GameManager gameManager;
    private PlayerHand hand;

    @BeforeEach
    void setUp() {
        this.gameManager = new GameManagerImpl();
        this.hand = new PlayerHandImpl();
    }

    @Test
    void testWildAllEqualCombo() {
        final ComboCheckStrategy combo = new WildAllEqualCombo();

        // Three notWild -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // One wild and two notWild_different -> not valid
        hand.addCards(Set.of(
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // One wild and two notWild_equal -> valid
        hand.addCards(Set.of(
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, ""))));
        assertTrue(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // Two wild and one notWild -> not valid
        hand.addCards(Set.of(
                new WildCardImpl(),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // Three wild -> not valid
        hand.addCards(Set.of(
                new WildCardImpl(),
                new WildCardImpl(),
                new WildCardImpl()));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // Many cards and valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new WildCardImpl(),
                new WildCardImpl()));
        assertThrows(IllegalArgumentException.class, () -> combo.comboIsValid(hand.getCards()));
        hand.clear();

        // Many cards but not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new WildCardImpl(),
                new WildCardImpl(),
                new WildCardImpl(),
                new WildCardImpl(),
                new WildCardImpl()));
        assertThrows(IllegalArgumentException.class, () -> combo.comboIsValid(hand.getCards()));
        hand.clear();
    }

    @Test
    void testAllDifferentCombo() {
        final ComboCheckStrategy combo = new AllDifferentCombo();

        // a,c,i -> valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, ""))));
        assertTrue(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // w,c,i -> not valid
        hand.addCards(Set.of(
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // a,w,i -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // a,c,w -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new WildCardImpl()));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // a,a,i -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // a,c,c -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // a,i,i -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // w,w,i -> not valid
        hand.addCards(Set.of(
                new WildCardImpl(),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // a,a,a -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // c,c,c -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // i,i,i -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // w,w,w -> not valid
        hand.addCards(Set.of(
                new WildCardImpl(),
                new WildCardImpl(),
                new WildCardImpl()));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // many cards and valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new WildCardImpl(),
                new WildCardImpl()));
        assertThrows(IllegalArgumentException.class, () -> combo.comboIsValid(hand.getCards()));
        hand.clear();

        // many cards but not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new WildCardImpl(),
                new WildCardImpl()));
        assertThrows(IllegalArgumentException.class, () -> combo.comboIsValid(hand.getCards()));
        hand.clear();

    }

    @Test
    void testAllKnightEqualCombo() {
        final ComboCheckStrategy combo = new AllKnightEqualCombo();

        // c,c,c -> valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, ""))));
        assertTrue(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // a,c,c -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // i,c,c -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // w,c,c -> not valid
        hand.addCards(Set.of(
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // a,i,c -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // w,i,c -> not valid
        hand.addCards(Set.of(
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // a,w,c -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // a,a,a -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // i,i,i -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // w,w,w -> not valid
        hand.addCards(Set.of(
                new WildCardImpl(),
                new WildCardImpl(),
                new WildCardImpl()));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // many cards and valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new WildCardImpl(),
                new WildCardImpl()));
        assertThrows(IllegalArgumentException.class, () -> combo.comboIsValid(hand.getCards()));
        hand.clear();

        // many cards but not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new WildCardImpl(),
                new WildCardImpl()));
        assertThrows(IllegalArgumentException.class, () -> combo.comboIsValid(hand.getCards()));
        hand.clear();
    }

    @Test
    void testAllJackEqualCombo() {
        final ComboCheckStrategy combo = new AllJackEqualCombo();

        // c,c,c -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // a,c,c -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // i,c,c -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // w,c,c -> not valid
        hand.addCards(Set.of(
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // a,i,c -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // w,i,c -> not valid
        hand.addCards(Set.of(
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // a,w,c -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // a,a,a -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // i,i,i -> valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, ""))));
        assertTrue(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // w,w,w -> not valid
        hand.addCards(Set.of(
                new WildCardImpl(),
                new WildCardImpl(),
                new WildCardImpl()));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // many cards and valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new WildCardImpl(),
                new WildCardImpl()));
        assertThrows(IllegalArgumentException.class, () -> combo.comboIsValid(hand.getCards()));
        hand.clear();

        // many cards but not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new WildCardImpl(),
                new WildCardImpl()));
        assertThrows(IllegalArgumentException.class, () -> combo.comboIsValid(hand.getCards()));
        hand.clear();
    }

    @Test
    void testAllCannonEqualCombo() {
        final ComboCheckStrategy combo = new AllCannonEqualCombo();

        // c,c,c -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // a,c,c -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // i,c,c -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // w,c,c -> not valid
        hand.addCards(Set.of(
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // a,i,c -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // w,i,c -> not valid
        hand.addCards(Set.of(
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // a,w,c -> not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // a,a,a -> valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, ""))));
        assertTrue(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // i,i,i -> valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, ""))));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // w,w,w -> not valid
        hand.addCards(Set.of(
                new WildCardImpl(),
                new WildCardImpl(),
                new WildCardImpl()));
        assertFalse(combo.comboIsValid(hand.getCards()));
        hand.clear();

        // many cards and valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new WildCardImpl(),
                new WildCardImpl()));
        assertThrows(IllegalArgumentException.class, () -> combo.comboIsValid(hand.getCards()));
        hand.clear();

        // many cards but not valid
        hand.addCards(Set.of(
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new WildCardImpl(),
                new WildCardImpl(),
                new WildCardImpl()));
        assertThrows(IllegalArgumentException.class, () -> combo.comboIsValid(hand.getCards()));
        hand.clear();
    }
}
