package it.unibo.risikoop.model.implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.TerritoryCard;

public class PlayerImpl implements Player {
    private final String name;
    private final Color color;
    private final List<Territory> territories;
    private final List<TerritoryCard> territoryCards;
    private Player killer;

    /**
     * 
     * @return
     */
    public PlayerImpl(final String name, final Color col) {
        this.name = name;
        this.color = new Color(col.r(), col.g(), col.b());
        territories = new ArrayList<>();
        territoryCards = new ArrayList<>();

    }

    public void setKiller(Player killer) {
        this.killer = killer;
    }

    @Override
    public Optional<Player> getKiller() {
        // todo: non so se va bene ritornare il giocatore devo ritornare una copia immutabile?
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
        return territories.stream().map(i -> i.getUnits()).reduce(0, Integer::sum);
    }

    @Override
    public List<TerritoryCard> getTerritoryCards() {
        return Collections.unmodifiableList(territoryCards);
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
    public void addTerritoryCard(final TerritoryCard card) {
        territoryCards.add(card);
    }

}
