package it.unibo.risikoop.model.interfaces;

import java.util.Optional;
import java.util.Set;

import org.graphstream.graph.Graph;

import it.unibo.risikoop.model.interfaces.holder.PlayersHolder;

/**
 * 
 */
public interface GameManager extends PlayersHolder {

    /**
     * 
     * @param name
     * @return the optional for the territory asked for
     */
    Optional<Territory> getTerritory(String name);

    /**
     * 
     * @param name
     * @return the optional of the continent asked for
     */
    Optional<Continent> getContinent(String name);

    /**
     * return a list of all territories.
     * 
     * @return the set of possessed territories
     */
    Set<Territory> getTerritories();

    /**
     * 
     */
    void removeAllTerritoriesAndContinents();

    /**
     * 
     * @param name
     * @return the territory neightbours
     */
    Set<Territory> getTerritoryNeightbours(String name);

    /**
     * Add new units to a territory.
     * 
     * @param territoryName
     * @param units
     */
    void addUnits(String territoryName, int units);

    /**
     * remove units from a territory.
     * 
     * @param territoryName
     * @param units
     */
    void removeUnits(String territoryName, int units);

    /**
     * Given a graph and the continents, create all the territories and set the
     * continents.
     * 
     * @param worldMap
     */
    void setWorldMap(Graph worldMap);

    /**
     * 
     * @param continents
     */
    void setContinents(Set<Continent> continents);

    /**
     * 
     * @return the continents present in the game
     */
    Set<Continent> getContinents();

    /**
     * 
     * @return the world's map
     */
    Graph getActualWorldMap();

}
