package it.unibo.risikoop.controller.Interfaces;

import java.util.List;

import org.graphstream.graph.Graph;

import it.unibo.risikoop.model.interfaces.Player;

public interface DataRetrieveController {
    public List<Player> getPlayerList();

    public Graph getActualMap();
}
