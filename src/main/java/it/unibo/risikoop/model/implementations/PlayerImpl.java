package it.unibo.risikoop.model.implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.PlayerHand;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.cards.GameCard;
import it.unibo.risikoop.model.interfaces.cards.TerritoryCard;
import it.unibo.risikoop.model.interfaces.cards.UnitType;

/**
 * 
 */
public final class PlayerImpl implements Player {
    private final String name;
    private final Color color;
    private final List<Territory> territories;
    private final PlayerHand hand;
    private int unitsToPlace;
    private Player killer;

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
    }

    /**
     * @param killer
     */
    @Override
    public void setKiller(final Player killer) {
        this.killer = new PlayerImpl(killer.getName(), killer.getColor());
    }

    @Override
    public Optional<Player> getKiller() {
        // todo: non so se va bene ritornare il giocatore devo ritornare una copia
        // immutabile?
        return Optional.ofNullable(killer);
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
    public void addUnitsToPlace(final int units) {
        this.unitsToPlace += units;
    }

    @Override
    public void removeUnitsToPlace(final int units) {
        this.unitsToPlace -= units;
    }

    @Override
    public PlayerHand getHand() {
        return this.hand;
    }
}
