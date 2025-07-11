package it.unibo.risikoop.controller.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.graphstream.graph.Graph;

import it.unibo.risikoop.model.implementations.Color;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;

/**
 * 
 */
public interface DataRetrieveController {
    /**
     * 
     * @return the list ofd mplayers
     */
    List<Player> getPlayerList();

    /**
     * 
     * @return the world's map
     */
    Graph getActualMap();

    /**
     * a method for getting all the territories of the game.
     * 
     * @return a set of all territories
     */
    Set<Territory> getTerritories();

    /**
     * a method for retrieving a territory by its name.
     * 
     * @param name the name of the territory you want to retrieve
     * @return the number of units of the territory, if no territory has that name
     *         returns Optional.empty()
     */
    Optional<Integer> getTerritoryUnitsFromName(String name);

    /**
     * 
     * @return the current player's name
     */
    String getCurrentPlayerName();

    /**
     * 
     * @return the current player's color
     */
    Color getCurrentPlayerColor();

}
