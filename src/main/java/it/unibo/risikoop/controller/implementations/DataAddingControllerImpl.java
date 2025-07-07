package it.unibo.risikoop.controller.implementations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.implementations.MultiGraph;

import it.unibo.risikoop.controller.interfaces.DataAddingController;
import it.unibo.risikoop.model.implementations.Color;
import it.unibo.risikoop.model.implementations.ContinentImpl;
import it.unibo.risikoop.model.interfaces.Continent;
import it.unibo.risikoop.model.interfaces.GameManager;

public class DataAddingControllerImpl implements DataAddingController {
    private final GameManager gameManager;

    public DataAddingControllerImpl(final GameManager gameManager) {
        this.gameManager = gameManager;

    }

    @Override
    public boolean addPlayer(String nome, int r, int g, int b) {
        return gameManager.addPlayer(nome, new Color(r, g, b));
    }

    @Override
    public boolean setWorldFromFile(File file) {
        final Graph newMap = new MultiGraph(file.getName(), false, true);
        Map<String, Continent> cm = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while (!(line = br.readLine()).equals("begin continent reward units")) {
                if (processMapFromLine(line, newMap))
                    return false;
            }
            gameManager.setWorldMap(newMap);
            /**
             * Creating the graph for the continent and their territories
             */
            while (!(line = br.readLine()).equals("begin territory")) {
                if (processContinentFromLine(line, cm))
                    return false;
            }
            /**
             * Creating the graph for the continent and their territories
             */
            while ((line = br.readLine()) != null) {
                processContinentsTerritoryFromLine(line, cm);
            }
            gameManager.setContinents(cm.values().stream().collect(Collectors.toSet()));
        } catch (Exception e) {
            gameManager.removeAllTerritoriesAndContinents();
            return false;
        }
        return true;
    }

    private void processContinentsTerritoryFromLine(String line, Map<String, Continent> cm) {
        String[] parts = line.split(":");
        String continentName = parts[0];
        String[] territrories = parts[1].split("-");
        for (String territoryName : territrories) {
            gameManager.getTerritory(territoryName).ifPresent(i -> cm.get(continentName).addTerritory(i));
        }
    }

    private boolean processContinentFromLine(String line, Map<String, Continent> cm) {
        String[] parts = line.split("-");
        String continentName = parts[0];
        if (parts.length != 2) {
            return true;
        }
        try {
            int unitReward = Integer.parseInt(parts[1]);
            cm.putIfAbsent(continentName, new ContinentImpl(continentName, unitReward));
        } catch (NumberFormatException e) {
            return true;
        }
        return false;
    }

    private boolean processMapFromLine(String line, final Graph newMap)
            throws ElementNotFoundException, IdAlreadyInUseException, EdgeRejectedException {
        String[] parts = line.split("-");
        if (parts.length != 2) {
            return true;
        }
        newMap.addEdge(line, parts[0], parts[1]);
        return false;
    }

    public void setDefaultWorld() {
        gameManager.setDefaultWorld();
    }

    @Override
    public void setDefaultMap() {
        gameManager.setDefaultWorld();
    }

}
