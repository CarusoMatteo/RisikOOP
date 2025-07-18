package it.unibo.risikoop.model.gameflowtest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import it.unibo.risikoop.controller.implementations.GamePhaseControllerImpl;
import it.unibo.risikoop.controller.interfaces.GamePhaseController.PhaseKey;
import it.unibo.risikoop.model.implementations.gamephase.AttackPhase;
import it.unibo.risikoop.model.interfaces.Territory;

/**
 * Tests for the Attack phase state machine.
 */
@DisplayName("Attack Phase")
class AttackPhaseTest extends AbstractGamePhaseTest {

    @Override
    protected PhaseKey startPhase() {
        return PhaseKey.ATTACK;
    }

    @Test
    @DisplayName("1) Only valid attacker territories can be selected")
    void onlyValidAttackerCanBeSelected() {
        // default: all territories units=0
        Territory t1 = gm.getTerritory("T1").get(); // Alice's
        // invalid because units<2
        assertFalse(gpc.selectTerritory(t1));
        assertEquals("Selecting attacker", gpc.getInnerStatePhaseDescription());

        // give enough units but no enemy neighbor? map fully connected => ok
        Territory valid = t1;
        t1.addUnits(1); // total units=2
        assertTrue(gpc.selectTerritory(valid), "Should allow selecting attacker with >=2 units and enemy neighbor");

        // performAction without selection does nothing? internalState SELECT_SRC
        // but since selected, performAction should advance
        gpc.performAction();
        assertEquals("Selecting defender", gpc.getInnerStatePhaseDescription());
    }

    @Test
    @DisplayName("2) Only valid defender territories can be selected after attacker")
    void onlyValidDefenderCanBeSelected() {
        // prepare valid attacker
        Territory att = gm.getTerritory("T1").get();
        att.addUnits(2);
        assertTrue(gpc.selectTerritory(att));
        gpc.performAction(); // to SELECT_DST
        assertEquals("Selecting defender", gpc.getInnerStatePhaseDescription());

        // invalid: select own territory
        Territory own = gm.getTerritory("T2").get();
        own.addUnits(2);
        assertFalse(gpc.selectTerritory(own));
        gpc.performAction();
        assertEquals("Selecting defender", gpc.getInnerStatePhaseDescription());

        // valid: select enemy neighbor
        Territory def = gm.getTerritory("T3").get();
        assertTrue(gpc.selectTerritory(def));
        gpc.performAction();
        assertEquals("Selecting units quantity", gpc.getInnerStatePhaseDescription());
    }

    @Test
    @DisplayName("3) Units selection conditions enforced")
    void unitsSelectionEnforced() {
        // reach SELECT_UNITS_QUANTITY
        Territory att = gm.getTerritory("T1").get();
        att.addUnits(3);
        assertTrue(gpc.selectTerritory(att));
        gpc.performAction();
        Territory def = gm.getTerritory("T3").get();
        assertTrue(gpc.selectTerritory(def));
        gpc.performAction();
        assertEquals("Selecting units quantity", gpc.getInnerStatePhaseDescription());

        // invalid: zero
        gpc.setUnitsToUse(0);
        gpc.performAction();
        assertEquals("Selecting units quantity", gpc.getInnerStatePhaseDescription());

        // invalid: >= attackerUnits
        gpc.setUnitsToUse(att.getUnits());
        gpc.performAction();
        assertEquals("Selecting units quantity", gpc.getInnerStatePhaseDescription());

        // valid
        gpc.setUnitsToUse(2);
        gpc.performAction();
        assertEquals("Executing the attack", gpc.getInnerStatePhaseDescription());
    }

    @Test
    @DisplayName("4) After execution, can loop back to attacker selection")
    void afterExecutionCanLoopBack() {
        // prepare phase until EXECUTE
        Territory att = gm.getTerritory("T1").get();
        att.addUnits(3);
        assertTrue(gpc.selectTerritory(att));
        gpc.performAction();
        Territory def = gm.getTerritory("T3").get();
        assertTrue(gpc.selectTerritory(def));
        gpc.performAction();
        gpc.setUnitsToUse(1);
        gpc.performAction();
        assertEquals("Executing the attack", gpc.getInnerStatePhaseDescription());

        // stub dice results for deterministic attack
        AttackPhase phase = (AttackPhase) ((GamePhaseControllerImpl) gpc).getCurrentPhase();
        phase.setAttackerDice(List.of(6));
        phase.setDefencerDice(List.of(1));

        // performAction executes attack and should reset to SELECT_SRC
        gpc.performAction();
        assertEquals("Selecting attacker", gpc.getInnerStatePhaseDescription());

        // chack tah can skip attack state
        gpc.nextPhase();
        assertEquals("Fase di gestione spostamenti", gpc.getStateDescription());
    }
}
