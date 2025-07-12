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
    private GameManager gameManager;
    private ComboChecker checker;
    private static final Optional<Integer> WILD_ALL_EQUAL_UNIT_REWARD = Optional.of(12);
    private static final Optional<Integer> ALL_DIFFERENT_UNIT_REWARD = Optional.of(10);
    private static final Optional<Integer> CAVALRY_UNIT_REWARD = Optional.of(8);
    private static final Optional<Integer> INFANTRY_UNIT_REWARD = Optional.of(6);
    private static final Optional<Integer> ARTILLERY_UNIT_REWARD = Optional.of(4);

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
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")))));
        assertEquals(Optional.empty(), checker.useCombo(Set.of(
                new WildCardImpl(),
                new WildCardImpl(),
                new WildCardImpl())));

        // AllDifferentCombo()
        // - Hand that only has AllDifferentCombo
        // \ ALL_DIFFERENT_UNIT_REWARD
        // - Hand that has AllDifferentCombo and WildAllEqualCombo (sup)
        // \ WILD_ALL_EQUAL_UNIT_REWARD
        // - Hand that has AllDifferentCombo and AllCavarlyEqualCombo (inf)
        // \ ALL_DIFFERENT_UNIT_REWARD
        assertEquals(ALL_DIFFERENT_UNIT_REWARD, checker.useCombo(Set.of(
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")))));
        assertEquals(Optional.empty(), checker.useCombo(Set.of(
                new WildCardImpl(),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")))));

        // AllCavarlyEqualCombo()
        // - Hand that only has AllCavarlyEqualCombo
        // \ CAVALRY_UNIT_REWARD
        // - Hand that has AllCavarlyEqualCombo and AllDifferentCombo (sup)
        // \ ALL_DIFFERENT_UNIT_REWARD
        // - Hand that has AllCavarlyEqualCombo and AllInfantryEqualCombo (inf)
        // \ CAVALRY_UNIT_REWARD
        assertEquals(CAVALRY_UNIT_REWARD, checker.useCombo(Set.of(
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.CAVALRY, new TerritoryImpl(gameManager, "")))));

        // AllInfantryEqualCombo()
        // - Hand that only has AllInfantryEqualCombo
        // \ INFANTRY_UNIT_REWARD
        // - Hand that has AllInfantryEqualCombo and AllCavarlyEqualCombo (sup)
        // \ CAVALRY_UNIT_REWARD
        // - Hand that has AllInfantryEqualCombo and AllArtilleryEqualCombo (inf)
        // \ INFANTRY_UNIT_REWARD
        assertEquals(INFANTRY_UNIT_REWARD, checker.useCombo(Set.of(
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.INFANTRY, new TerritoryImpl(gameManager, "")))));

        // AllArtilleryEqualCombo()
        // - Hand that only has AllArtilleryEqualCombo
        // \ ARTILLERY_UNIT_REWARD
        // - Hand that has AllArtilleryEqualCombo and any other superior combo (sup)
        // | (ex: AllInfantryEqualCombo)
        // \ INFANTRY_UNIT_REWARD
        assertEquals(ARTILLERY_UNIT_REWARD, checker.useCombo(Set.of(
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")),
                new TerritoryCardImpl(UnitType.ARTILLERY, new TerritoryImpl(gameManager, "")))));
    }

}
