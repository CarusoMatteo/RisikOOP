package it.unibo.risikoop.model.implementations;

import java.util.HashSet;
import java.util.Set;

import it.unibo.risikoop.model.interfaces.Continent;
import it.unibo.risikoop.model.interfaces.Territory;

public class ContinentImpl implements Continent {
    private final String name;
    private final Integer unitReward;
    private final Set<Territory> territories = new HashSet<>();

    public ContinentImpl(final String name, Integer unitReward) {
        this.name = name;
        this.unitReward = unitReward;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Integer getUnitReward() {
        return unitReward;
    }

    @Override
    public Set<Territory> getTerritories() {
        return territories;
    }

    @Override
    public void addTerritory(Territory territory) {
        territories.add(territory);
    }

}
