package it.unibo.risikoop.model.interfaces;

import java.util.Set;

public interface Continent {
    public String getName();

    public Integer getUnitReward();

    public Set<Territory> getTerritories();

    public void addTerritory(Territory territory);

}
