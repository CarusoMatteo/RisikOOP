package it.unibo.risikoop.model.implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import it.unibo.risikoop.model.interfaces.ObjectiveCard;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.PlayerHand;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.cards.GameCard;
import it.unibo.risikoop.model.interfaces.cards.TerritoryCard;
import it.unibo.risikoop.model.interfaces.cards.UnitType;

/**
 * player implementation.
 */
public final class PlayerImpl implements Player {
    private final String name;
    private final Color color;
    private final List<Territory> territories;
    private final PlayerHand hand;
    private Optional<Player> killer;
    private int unitsToPlace;
    private Optional<ObjectiveCard> objectiveCard;

    /**
     * @param name
     * @param col
     * @return
     */
    public PlayerImpl(final String name, final Color col) {
        this.name = name;
        this.color = new Color(col.r(), col.g(), col.b());
        territories = new ArrayList<>();
        this.hand = new PlayerHandImpl();
        this.unitsToPlace = 0;
        this.killer = Optional.empty();
        objectiveCard = Optional.empty();
    }

    /**
     * @param killer
     */
    @Override
    public void setKiller(final Player killer) {
        this.killer = Optional.of(new PlayerImpl(killer.getName(), killer.getColor()));
    }

    @Override
    public Optional<Player> getKiller() {
        return killer;
    }

    @Override
    public boolean addTerritory(final Territory territory) {
        return territories.add(territory);
    }

    @Override
    public List<Territory> getTerritories() {
        return Collections.unmodifiableList(territories);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Integer getTotalUnits() {
        return territories.stream().map(Territory::getUnits).reduce(0, Integer::sum);
    }

    @Override
    public List<TerritoryCard> getTerritoryCards() {
        return hand.getCards().stream()
                .filter(gc -> gc.getType() != UnitType.WILD)
                .map(TerritoryCard.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    public Color getColor() {
        return new Color(color.r(), color.g(), color.b());
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof final Player pl) {
            return this.name.equals(pl.getName()) && this.color.equals(pl.getColor());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode() * 10 + this.color.hashCode() * 100;
    }

    @Override
    public void addGameCard(final GameCard card) {
        hand.addCard(card);
    }

    @Override
    public List<GameCard> getGameCards() {
        return hand.getCards().stream()
                .collect(Collectors.toList());
    }

    @Override
    public int getUnitsToPlace() {
        return this.unitsToPlace;
    }

    @Override
    public void addUnitsToPlace(final int units) {
        if (units < 0) {
            throw new IllegalArgumentException("Attempted to add a negative number of units to place.");
        }

        this.unitsToPlace += units;
    }

    @Override
    @SuppressWarnings("EI_EXPOSE_REP")
    /*
     * Suppressed because the hand is given as a way
     * to get/modifiy the player's cards.
     */
    public PlayerHand getHand() {
        return this.hand;
    }

    @Override
    public boolean addTerritories(final List<Territory> territories) {
        Objects.requireNonNull(territories, "territories must not be null");
        return this.territories.addAll(territories);
    }

    @Override
    public boolean removeTerritory(final Territory territory) {
        return territories.remove(territory);
    }

    @Override
    public void removeUnitsToPlace(final int units) {
        if (units < 0) {
            throw new IllegalArgumentException("Cannot remove a negative number of units to place.");
        }
        if (units > this.unitsToPlace) {
            throw new IllegalArgumentException("Cannot remove more units than available to place.");
        }
        this.unitsToPlace -= units;
    }

    @Override
    public boolean isEliminated() {
        return killer.isPresent();
    }

    @Override
    public ObjectiveCard getObjectiveCard() {
        return objectiveCard
                .orElseThrow(() -> new IllegalStateException("Objective card was required when it was not present."));
    }

    @Override
    public void setObjectiveCard(final ObjectiveCard objectiveCard) {
        this.objectiveCard = Optional.ofNullable(objectiveCard);
    }

}
