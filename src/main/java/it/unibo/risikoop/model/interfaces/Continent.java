package it.unibo.risikoop.model.interfaces;

import it.unibo.risikoop.model.interfaces.holder.TerritoryHolder;

/**
 * continent interfaces.
 */
public interface Continent extends TerritoryHolder {
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

}
