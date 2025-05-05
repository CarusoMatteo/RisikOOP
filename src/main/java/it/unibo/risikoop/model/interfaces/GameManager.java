package it.unibo.risikoop.model.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.graphstream.graph.Graph;

import it.unibo.risikoop.model.classes.Color;

public interface GameManager {
    /**
     * Add a new player if not already present
     * 
     * @param name new player's name
     * @return true if the player has been added succesfully
     */
    public boolean addPlayer(String name, Color col);

    /**
     * 
     * @param name
     * @return
     */
    public Optional<Territory> getTerritory(String name);

    /**
     * return a list of all territories
     */
    public Set<Territory> getTerritories();

    /**
     * 
     * @param name
     * @return
     */
    public Set<Territory> getTerritoryNeightbours(String name);

    /**
     * remove a new player with a certain name if already present
     * 
     * @param name player's name
     * @return true if the player has been removed succesfully
     */

    public boolean removePlayer(String name);

    /**
     * Add new units to a territory
     * 
     * @param territory
     * @param units
     */
    public void addUnits(String TerritoryName, int units);

    /**
     * 
     * @param TerritoryName
     * @param units
     */
    public void removeUnits(String TerritoryName, int units);

    /**
     * 
     * @return players list
     */
    public List<Player> getPlayers();

    /**
     * Given a graph, create all the territories
     */
    public void setWorldMap(Graph worldMap);

    public Graph getWorldMap();
    // TODO: Create active player
    // TODO: Create player change

}
