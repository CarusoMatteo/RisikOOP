package it.unibo.risikoop.model.implementations;

import java.util.HashSet;
import java.util.Set;

import it.unibo.risikoop.model.interfaces.Continent;
import it.unibo.risikoop.model.interfaces.Territory;

/**
 * continent implementation.
 */
public final class ContinentImpl implements Continent {
    private final String name;
    private final Integer unitReward;
    private final Set<Territory> territories = new HashSet<>();

    /**
     * constructor.
     * 
     * @param name
     * @param unitReward
     */
    public ContinentImpl(final String name, final Integer unitReward) {
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
        return Set.copyOf(territories);
    }

    @Override
    public void addTerritory(final Territory territory) {
        territories.add(territory);
    }

}
