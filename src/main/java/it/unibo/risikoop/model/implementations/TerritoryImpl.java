package it.unibo.risikoop.model.implementations;

import java.util.Set;
import java.util.stream.Collectors;

import org.graphstream.graph.Graph;

import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;

/**
 * terriory implementantion.
 */
public final class TerritoryImpl implements Territory {
    private final GameManager gameManager;
    private final String name;
    private Player owner;
    private int units;

    /**
     * constructor.
     * 
     * @param gameManager
     * @param name
     */
    public TerritoryImpl(final GameManager gameManager, final String name) {
        this.name = name;
        this.gameManager = gameManager;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Player getOwner() {
        return owner;
    }

    @Override
    public boolean changeOwner(final Player newOwner) {
        if (gameManager.getPlayers().contains(newOwner)) {
            owner = new PlayerImpl(newOwner.getName(), newOwner.getColor());
            return true;
        }
        return false;
    }

    @Override
    public void addUnits(final int addedUnits) {
        if (addedUnits > 0) {
            this.units = this.units + addedUnits;
        }
    }

    @Override
    public void removeUnits(final int removedUnits) {
        if (removedUnits > 0) {
            this.units = this.units - removedUnits < 0 ? 0 : this.units - removedUnits;
        }

    }

    @Override
    public Integer getUnits() {
        return units;
    }

    @Override
    public Set<Territory> getNeightbours() {
        final Graph map = gameManager.getActualWorldMap();
        final Set<Territory> territories = gameManager.getTerritories();
        return map.getNode(name)
                .neighborNodes()
                .flatMap(i -> territories.stream().filter(j -> j.getName().equals(i.getId())))
                .collect(Collectors.toSet());
    }

}
