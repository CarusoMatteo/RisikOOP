package it.unibo.risikoop.model.gameflowtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import it.unibo.risikoop.controller.implementations.logicgame.LogicAttackImpl;
import it.unibo.risikoop.model.implementations.Color;
import it.unibo.risikoop.model.implementations.GameManagerImpl;
import it.unibo.risikoop.model.interfaces.AttackResult;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;

@DisplayName("LogicAttackImpl Tests")
class LogicAttackImplTest {

    private static final int FIVE = 5;
    private static final int SIX = 6;
    private static final int NINE = 9;
    // private GameManager gm;
    private Player attacker;
    private Player defender;
    private Territory src;
    private Territory dst;
    private LogicAttackImpl logic;

    @BeforeEach
    void setUp() {
        // Create a minimal world with two connected territories
        final GameManager gm = new GameManagerImpl();
        gm.addPlayer("Alice", new Color(0, 0, 0));
        gm.addPlayer("Bob", new Color(1, 0, 0));
        attacker = gm.getPlayers().get(0);
        defender = gm.getPlayers().get(1);

        final Graph map = new MultiGraph("map", false, true);
        map.addNode("T1");
        map.addNode("T2");
        map.addEdge("e1", "T1", "T2", true);
        map.addEdge("e2", "T2", "T1", true);
        gm.setWorldMap(map);

        // Assign ownership
        src = gm.getTerritory("T1").get();
        dst = gm.getTerritory("T2").get();
        src.setOwner(attacker);
        dst.setOwner(defender);

        logic = new LogicAttackImpl();
    }

    @Test
    @DisplayName("Normal attack not finished returns false and updates units")
    void normalAttackNotFinished() {
        // Given: 3 attacker units, 5 defender units, using 2 units to attack
        src.addUnits(3);
        dst.addUnits(FIVE);

        // Force dice: attacker loses both rounds
        logic.setAttackerDice(List.of(1, 1));
        logic.setDefencerDice(List.of(2, 2));

        // When
        final boolean result = logic.attack(attacker, defender, src, dst, 2);

        // Then
        assertFalse(result, "Attack not finished should return false");
        final AttackResult res = logic.showAttackResults().orElseThrow();
        assertEquals(List.of(1, 1), res.getAttackerDiceRolls());
        assertEquals(List.of(2, 2), res.getDefenderDiceRolls());

        // Units updated: attacker src lost 2, defender dst unchanged
        assertEquals(3 - 2, src.getUnits());
        assertEquals(FIVE, dst.getUnits());
        assertEquals(defender, dst.getOwner());
    }

    @Test
    @DisplayName("Normal attack capture returns true and transfers ownership and units")
    void normalAttackCapture() {
        // Given: 3 attacker units, 2 defender units, using 2 units to attack
        src.addUnits(3);
        dst.addUnits(2);

        // Force dice: attacker wins both rounds
        logic.setAttackerDice(List.of(SIX, SIX));
        logic.setDefencerDice(List.of(1, 1));

        // When
        final boolean result = logic.attack(attacker, defender, src, dst, 2);

        // Then
        assertTrue(result, "Capture should return true");
        final AttackResult res = logic.showAttackResults().orElseThrow();
        assertEquals(List.of(SIX, SIX), res.getAttackerDiceRolls());
        assertEquals(List.of(1, 1), res.getDefenderDiceRolls());

        // Ownership and units updated
        assertEquals(attacker, dst.getOwner(), "Territory should be conquered");
        // Defender lost all and then received attackerUnits
        assertEquals(2, dst.getUnits(), "Conquered territory should have moving units");
        // Source lost moved units
        assertEquals(3 - 2, src.getUnits(), "Source lost moved units");
    }

    @Test
    @DisplayName("Normal attack defender wins returns true and updates units")
    void normalAttackDefenderWins() {
        // Given: 3 attacker units, 2 defender units, using 2 units to attack
        src.addUnits(10);
        dst.addUnits(10);

        // Force dice: defender wins both rounds
        logic.setAttackerDice(List.of(1, 1));
        logic.setDefencerDice(List.of(SIX, SIX));

        // When
        boolean result = logic.attack(attacker, defender, src, dst, 2);

        // Then
        assertFalse(result, "Defender win should return true");
        AttackResult res = logic.showAttackResults().orElseThrow();
        assertEquals(List.of(1, 1), res.getAttackerDiceRolls());
        assertEquals(List.of(SIX, SIX), res.getDefenderDiceRolls());

        // Units updated: attacker src lost 2, defender dst unchanged
        assertEquals(10 - 2, src.getUnits());
        assertEquals(10, dst.getUnits());
        assertEquals(defender, dst.getOwner(), "Owner remains defender");

        // Force dice: defender wins both rounds
        logic.setAttackerDice(List.of(SIX, SIX, 4));
        logic.setDefencerDice(List.of(SIX, FIVE, FIVE));
        result = logic.attack(attacker, defender, src, dst, 3);

        assertFalse(result, "Defender win should return true");
        res = logic.showAttackResults().orElseThrow();
        assertEquals(List.of(SIX, SIX, 4), res.getAttackerDiceRolls());
        assertEquals(List.of(SIX, FIVE, FIVE), res.getDefenderDiceRolls());

        assertEquals(SIX, src.getUnits());
        assertEquals(NINE, dst.getUnits());
        assertEquals(defender, dst.getOwner(), "Owner remains defender");
    }

    @Test
    @DisplayName("Fast attack mode finishes capture")
    void fastAttackModeFinishesCapture() {
        // Given: capture scenario as above
        src.addUnits(3);
        dst.addUnits(2);
        logic.enableFastAttack();

        logic.setAttackerDice(List.of(SIX, SIX));
        logic.setDefencerDice(List.of(1, 1));

        // When
        final boolean result = logic.attack(attacker, defender, src, dst, 2);

        // Then
        assertTrue(result, "Fast attack capture should return true");
        final AttackResult res = logic.showAttackResults().orElseThrow();
        assertEquals(List.of(SIX, SIX), res.getAttackerDiceRolls());
        assertEquals(List.of(1, 1), res.getDefenderDiceRolls());

        assertEquals(attacker, dst.getOwner());
        assertEquals(2, dst.getUnits());
        assertEquals(3 - 2, src.getUnits());
    }
}
