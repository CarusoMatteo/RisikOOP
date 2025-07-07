package it.unibo.risikoop.controller.interfaces;

import java.util.List;

import org.graphstream.graph.Graph;

import it.unibo.risikoop.model.interfaces.Player;

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
}
