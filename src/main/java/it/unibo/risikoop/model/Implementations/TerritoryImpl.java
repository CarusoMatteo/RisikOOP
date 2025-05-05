package it.unibo.risikoop.model.Implementations;

import java.util.Set;
import java.util.stream.Collectors;

import org.graphstream.graph.Graph;

import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;

public class TerritoryImpl implements Territory {
    private final GameManager gameManager;
    private final String name;
    private Player owner;
    private int units = 0;

    public TerritoryImpl(GameManager gameManager, String name) {
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
    public boolean setOwner(Player newOwner) {
        if (gameManager.getPlayers().contains(newOwner)) {
            owner = new PlayerImpl(newOwner.getName(), newOwner.getColor());
            return true;
        }
        return false;
    }

    @Override
    public void addUnits(int addedUnits) {
        if (addedUnits > 0) {
            this.units = this.units + addedUnits;
        }
    }

    @Override
    public void removeUnits(int removedUnits) {
        if (removedUnits > 0) {
            this.units = this.units - removedUnits < 0 ? 0 : this.units - removedUnits;
        }

    }

    @Override
    public Integer getUnits() {
        int returnedValue = units;
        return returnedValue;
    }

    @Override
    public Set<Territory> getNeightbours() {
        Graph map = gameManager.getWorldMap();
        Set<Territory> territories = gameManager.getTerritories();
        return map.getNode(name)
                .neighborNodes()
                .flatMap(i -> territories.stream().filter(j -> j.getName().equals(i.getId())))
                .collect(Collectors.toSet());
    }

}
