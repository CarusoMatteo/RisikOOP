package it.unibo.risikoop.model.interfaces.cards;

/**
 * Types of units in the game.
 * <p>
 * Each unit type corresponds to a specific card type in the game.
 */
public enum UnitType {
    /**
     * Jack unit type.
     */
    JACK,
    /**
     * Knight unit type.
     */
    KNIGHT,
    /**
     * Cannon unit type.
     */
    CANNON,
    /**
     * Wild unit type, which can be used in a special combo.
     */
    WILD
}
