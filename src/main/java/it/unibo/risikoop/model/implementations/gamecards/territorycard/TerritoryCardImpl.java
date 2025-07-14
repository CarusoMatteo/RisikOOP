package it.unibo.risikoop.model.implementations.gamecards.territorycard;

import java.util.Objects;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.cards.TerritoryCard;
import it.unibo.risikoop.model.interfaces.cards.UnitType;

/**
 * Implementation of the TerritoryCard interface.
 * This class represents a card that corresponds to a specific territory in the
 * game.
 * It extends AbstractGameCard and provides functionality to retrieve the
 * associated territory.
 */
public final class TerritoryCardImpl
        extends AbstractGameCard
        implements TerritoryCard {

    private final Territory territory;

    /**
     * Constructs a TerritoryCardImpl with the specified type, owner, and territory.
     *
     * @param type      the type of the card (JACK, KNIGHT, CANNON)
     * @param territory the territory associated with this card
     * @throws IllegalArgumentException if type is WILD
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "We intentionally store the Territory reference;"
            + "game logic needs mutable state.")
    public TerritoryCardImpl(final UnitType type, final Territory territory) {
        super(type);
        if (type == UnitType.WILD) {
            throw new IllegalArgumentException("Use WildCardImpl for WILD");
        }
        this.territory = Objects.requireNonNull(territory);
    }

    @Override
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "We intentionally store the Territory reference;"
            + "game logic needs mutable state.")
    public Territory getAssociatedTerritory() {
        return territory;
    }
}
