package it.unibo.risikoop.model.implementations.gamecards.territorycard;

import java.util.Objects;
import java.util.Optional;

import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.cards.GameCard;
import it.unibo.risikoop.model.interfaces.cards.UnitType;

/**
 * Abstract base class for game cards that provides common functionality for all
 * game cards.
 * <p>
 * This class implements the GameCard interface and provides methods to retrieve
 * the type of card
 * and the owner of the card.
 */
public abstract class AbstractGameCard implements GameCard {
    private final UnitType type;
    private Optional<Player> owner;

    /**
     * Constructs an AbstractGameCard with the specified type and owner.
     *
     * @param type  the type of the card (INFANTRY, CAVALRY, ARTILLERY, or WILD)
     */
    protected AbstractGameCard(final UnitType type) {
        this.type = Objects.requireNonNull(type, "type must not be null");
        this.owner = Optional.empty();
    }

    @Override
    public final UnitType getType() {
        return type;
    }

    @Override
    public final Optional<Player> getOwner() {
        return owner;
    }

    @Override
    public final boolean isTerritoryCard() {
        return type != UnitType.WILD;
    }

    @Override
    public final boolean setOwner(final Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        owner = Optional.of(player);
        return false;
    }
}
