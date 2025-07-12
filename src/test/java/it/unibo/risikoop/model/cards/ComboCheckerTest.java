package it.unibo.risikoop.model.cards;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.risikoop.model.implementations.GameManagerImpl;
import it.unibo.risikoop.model.implementations.TerritoryImpl;
import it.unibo.risikoop.model.implementations.gamecards.combos.ComboCheckerImpl;
import it.unibo.risikoop.model.implementations.gamecards.territorycard.TerritoryCardImpl;
import it.unibo.risikoop.model.implementations.gamecards.territorycard.WildCardImpl;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.cards.ComboChecker;
import it.unibo.risikoop.model.interfaces.cards.UnitType;

/**
 * Class to test Combo Checker.
 */
final class ComboCheckerTest {
    private static final Optional<Integer> WILD_ALL_EQUAL_UNIT_REWARD = Optional.of(12);
    private static final Optional<Integer> ALL_DIFFERENT_UNIT_REWARD = Optional.of(10);
    private static final Optional<Integer> KNIGHT_UNIT_REWARD = Optional.of(8);
    private static final Optional<Integer> JACK_UNIT_REWARD = Optional.of(6);
    private static final Optional<Integer> CANNON_UNIT_REWARD = Optional.of(4);

    private GameManager gameManager;
    private ComboChecker checker;

    @BeforeEach
    void setUp() {
        this.gameManager = new GameManagerImpl();
        this.checker = new ComboCheckerImpl();
    }

    @Test
    void testUseCombo() {

        // WildAllEqualCombo()
        // - Hand that only has WildAllEqualCombo
        // \ WILD_ALL_EQUAL_UNIT_REWARD
        // - Hand that has WildAllEqualCombo and any other inferior combo (inf)
        // | (ex: AllDifferentCombo)
        // \ WILD_ALL_EQUAL_UNIT_REWARD
        assertEquals(WILD_ALL_EQUAL_UNIT_REWARD, checker.useCombo(Set.of(
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")))));
        assertEquals(Optional.empty(), checker.useCombo(Set.of(
                new WildCardImpl(),
                new WildCardImpl(),
                new WildCardImpl())));

        // AllDifferentCombo()
        // - Hand that only has AllDifferentCombo
        // \ ALL_DIFFERENT_UNIT_REWARD
        // - Hand that has AllDifferentCombo and WildAllEqualCombo (sup)
        // \ WILD_ALL_EQUAL_UNIT_REWARD
        // - Hand that has AllDifferentCombo and AllKnightEqualCombo (inf)
        // \ ALL_DIFFERENT_UNIT_REWARD
        assertEquals(ALL_DIFFERENT_UNIT_REWARD, checker.useCombo(Set.of(
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")))));
        assertEquals(Optional.empty(), checker.useCombo(Set.of(
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")))));

        // AllKnightEqualCombo()
        // - Hand that only has AllKnightEqualCombo
        // \ KNIGHT_UNIT_REWARD
        // - Hand that has AllKnightEqualCombo and AllDifferentCombo (sup)
        // \ ALL_DIFFERENT_UNIT_REWARD
        // - Hand that has AllKnightEqualCombo and AllJackEqualCombo (inf)
        // \ KNIGHT_UNIT_REWARD
        assertEquals(KNIGHT_UNIT_REWARD, checker.useCombo(Set.of(
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(gameManager, "")))));

        // AllJackEqualCombo()
        // - Hand that only has AllJackEqualCombo
        // \ JACK_UNIT_REWARD
        // - Hand that has AllJackEqualCombo and AllKnightEqualCombo (sup)
        // \ KNIGHT_UNIT_REWARD
        // - Hand that has AllJackEqualCombo and AllCannonEqualCombo (inf)
        // \ JACK_UNIT_REWARD
        assertEquals(JACK_UNIT_REWARD, checker.useCombo(Set.of(
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(gameManager, "")))));

        // AllCannonEqualCombo()
        // - Hand that only has AllCannonEqualCombo
        // \ CANNON_UNIT_REWARD
        // - Hand that has AllCannonEqualCombo and any other superior combo (sup)
        // | (ex: AllJackEqualCombo)
        // \ JACK_UNIT_REWARD
        assertEquals(CANNON_UNIT_REWARD, checker.useCombo(Set.of(
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(gameManager, "")))));
    }

}
