package it.unibo.risikoop.model.interfaces.holder;

import java.util.Collection;

import it.unibo.risikoop.model.interfaces.Territory;

/**
 * * An interface for holding a list of territory.
 * 
 */
public interface TerritoryHolder {
    /**
     * Gives a territory to the player.
     * 
     * @param territory the territory to give to the player.
     * @return if the addition was successful.
     */
    boolean addTerritory(Territory territory);

    /**
     * @return the player's territories
     */
    Collection<Territory> getTerritories();

    /**
     * remove a territory from the player.
     * 
     * @param territory
     * @return if it was possible to remove the territory
     * 
     */
    boolean removeTerritory(Territory territory);

    /**
     * add a new territory to the player.
     * 
     * @param items the territories to add
     * @return if it was possible to add the territory
     * 
     */
    default boolean addTerritories(final Collection<Territory> items) {
        return items
                .stream()
                .map(this::addTerritory)
                .reduce(true, (i, j) -> i && j);
    }
}
