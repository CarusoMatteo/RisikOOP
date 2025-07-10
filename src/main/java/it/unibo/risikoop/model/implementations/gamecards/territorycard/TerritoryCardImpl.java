package it.unibo.risikoop.model.implementations.gamecards.territorycard;

import java.util.Objects;

import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.cards.TerritoryCard;
import it.unibo.risikoop.model.interfaces.cards.UnitType;

/**
 * Implementation of the TerritoryCard interface.
 * This class represents a card that corresponds to a specific territory in the game.
 * It extends AbstractGameCard and provides functionality to retrieve the associated territory.
 */
public final class TerritoryCardImpl
    extends AbstractGameCard
    implements TerritoryCard {

    private final Territory territory;

    /**
     * Constructs a TerritoryCardImpl with the specified type, owner, and territory.
     *
     * @param type       the type of the card (INFANTRY, CAVALRY, ARTILLERY)
     * @param owner      the player who owns the card
     * @param territory  the territory associated with this card
     * @throws IllegalArgumentException if type is WILD
     */
    public TerritoryCardImpl(UnitType type, Player owner, Territory territory) {
        super(type, owner);
        if (type == UnitType.WILD) {
            throw new IllegalArgumentException("Use WildCardImpl for WILD");
        }
        this.territory = Objects.requireNonNull(territory);
    }

    @Override
    public Territory getAssociatedTerritory() {
        return territory;
    }
}