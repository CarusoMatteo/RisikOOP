package it.unibo.risikoop.model;

import java.util.Collections;
import java.util.List;

public class TerritoryImpl implements Territory {
    private final GameManager gameManager;
    private final String name;
    private final List<Territory> neighbours;
    private Player owner;
    private int units = 0;

    public TerritoryImpl(GameManager gameManager, String name, List<Territory> neighbours) {
        this.name = name;
        this.gameManager = gameManager;
        this.neighbours = Collections.unmodifiableList(neighbours);
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
        this.units = this.units + addedUnits;
    }

    @Override
    public void removeUnits(int removedUnits) {
        this.units = this.units - removedUnits < 0 ? 0 : this.units - removedUnits;

    }

    @Override
    public Integer getUnits() {
        return units;
    }

    @Override
    public List<Territory> getNeightbours() {
        return neighbours;
    }

}
