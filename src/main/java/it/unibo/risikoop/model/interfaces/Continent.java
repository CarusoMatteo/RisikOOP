package it.unibo.risikoop.model.interfaces;

import java.util.Set;

/**
 * continent interfaces.
 */
public interface Continent {
    /**
     * 
     * @return the name.
     */
    String getName();

    /**
     * 
     * @return the unit rewards.
     */
    Integer getUnitReward();

    /**
     * 
     * 
     * @return the set of all the territory of the conmtinent
     */
    Set<Territory> getTerritories();

    /**
     * add a new territory.
     * 
     * @param territory
     */
    void addTerritory(Territory territory);

}
