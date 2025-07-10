package it.unibo.risikoop.model.implementations.gamecards.territorycard;

import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.cards.*;

/**
 * Represents a wild card in the game.
 * Wild cards can be used as any type of unit card (INFANTRY, CAVALRY,
 * ARTILLERY), But they do not correspond to any specific territory.
 */
public class WildCardImpl
        extends AbstractGameCard
        implements WildCard {

    /**
     * Constructs a WilldCardImpl with the specified owner.
     *
     * @param owner the player who owns the card
     */
    public WildCardImpl(final Player owner) {
        super(UnitType.WILD, owner);
    }
}
