package it.unibo.risikoop.model.implementations.gamecards.territorycard;

import it.unibo.risikoop.model.interfaces.cards.UnitType;
import it.unibo.risikoop.model.interfaces.cards.WildCard;

/**
 * Represents a wild card in the game.
 * Wild cards can be used as any type of unit card (JACK, CAVALRY,
 * ARTILLERY), But they do not correspond to any specific territory.
 */
public class WildCardImpl extends AbstractGameCard implements WildCard {

    /**
     * Constructs a WilldCardImpl with the specified owner.
     */
    public WildCardImpl() {
        super(UnitType.WILD);
    }
}
