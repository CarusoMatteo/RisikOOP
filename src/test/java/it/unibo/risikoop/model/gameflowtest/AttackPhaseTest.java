package it.unibo.risikoop.model.gameflowtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    private static final String SELECTING_UNITS_QUANTITY = "Selecting units quantity";
    private static final int ATTACKER_DICE_VALUE = 6;

    @Override
    protected PhaseKey startPhase() {
        return PhaseKey.ATTACK;
    }

    @Test
    @DisplayName("1) Only valid attacker territories can be selected")
    void onlyValidAttackerCanBeSelected() {
        // default: all territories units=0
        final Territory t1 = getGm().getTerritory("T1").get(); // Alice's
        // invalid because units<2
        assertFalse(getGpc().selectTerritory(t1));
        assertEquals("Selecting attacker", getGpc().getInnerStatePhaseDescription());

        // give enough units but no enemy neighbor? map fully connected => ok
        final Territory valid = t1;
        t1.addUnits(1); // total units=2
        assertTrue(getGpc().selectTerritory(valid),
                "Should allow selecting attacker with >=2 units and enemy neighbor");

        // performAction without selection does nothing? internalState SELECT_SRC
        // but since selected, performAction should advance
        getGpc().performAction();
        assertEquals("Selecting defender", getGpc().getInnerStatePhaseDescription());
    }

    @Test
    @DisplayName("2) Only valid defender territories can be selected after attacker")
    void onlyValidDefenderCanBeSelected() {
        // prepare valid attacker
        final Territory att = getGm().getTerritory("T1").get();
        att.addUnits(2);
        assertTrue(getGpc().selectTerritory(att));
        getGpc().performAction(); // to SELECT_DST
        assertEquals("Selecting defender", getGpc().getInnerStatePhaseDescription());

        // invalid: select own territory
        final Territory own = getGm().getTerritory("T2").get();
        own.addUnits(2);
        assertFalse(getGpc().selectTerritory(own));
        getGpc().performAction();
        assertEquals("Selecting defender", getGpc().getInnerStatePhaseDescription());

        // valid: select enemy neighbor
        final Territory def = getGm().getTerritory("T3").get();
        assertTrue(getGpc().selectTerritory(def));
        getGpc().performAction();
        assertEquals(SELECTING_UNITS_QUANTITY, getGpc().getInnerStatePhaseDescription());
    }

    @Test
    @DisplayName("3) Units selection conditions enforced")
    void unitsSelectionEnforced() {
        // reach SELECT_UNITS_QUANTITY
        final Territory att = getGm().getTerritory("T1").get();
        att.addUnits(3);
        assertTrue(getGpc().selectTerritory(att));
        getGpc().performAction();
        final Territory def = getGm().getTerritory("T3").get();
        assertTrue(getGpc().selectTerritory(def));
        getGpc().performAction();
        assertEquals(SELECTING_UNITS_QUANTITY, getGpc().getInnerStatePhaseDescription());

        // invalid: zero
        getGpc().setUnitsToUse(0);
        getGpc().performAction();
        assertEquals(SELECTING_UNITS_QUANTITY, getGpc().getInnerStatePhaseDescription());

        // invalid: >= attackerUnits
        getGpc().setUnitsToUse(att.getUnits());
        getGpc().performAction();
        assertEquals(SELECTING_UNITS_QUANTITY, getGpc().getInnerStatePhaseDescription());

        // valid
        getGpc().setUnitsToUse(2);
        getGpc().performAction();
        assertEquals("Executing the attack", getGpc().getInnerStatePhaseDescription());
    }

    @Test
    @DisplayName("4) After execution, can loop back to attacker selection")
    void afterExecutionCanLoopBack() {
        // prepare phase until EXECUTE
        final Territory att = getGm().getTerritory("T1").get();
        att.addUnits(3);
        assertTrue(getGpc().selectTerritory(att));
        getGpc().performAction();
        final Territory def = getGm().getTerritory("T3").get();
        assertTrue(getGpc().selectTerritory(def));
        getGpc().performAction();
        getGpc().setUnitsToUse(1);
        getGpc().performAction();
        assertEquals("Executing the attack", getGpc().getInnerStatePhaseDescription());

        // stub dice results for deterministic attack
        final AttackPhase phase = (AttackPhase) ((GamePhaseControllerImpl) getGpc()).getCurrentPhase();
        phase.setAttackerDice(List.of(ATTACKER_DICE_VALUE));
        phase.setDefencerDice(List.of(1));

        // performAction executes attack and should reset to SELECT_SRC
        getGpc().performAction();
        assertEquals("Selecting attacker", getGpc().getInnerStatePhaseDescription());

        // chack tah can skip attack state
        getGpc().nextPhase();
        assertEquals("Fase di gestione spostamenti", getGpc().getStateDescription());
    }
}
