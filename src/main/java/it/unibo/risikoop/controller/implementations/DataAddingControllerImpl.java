package it.unibo.risikoop.controller.implementations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;

import it.unibo.risikoop.controller.interfaces.DataAddingController;
import it.unibo.risikoop.model.implementations.Color;
import it.unibo.risikoop.model.implementations.ContinentImpl;
import it.unibo.risikoop.model.interfaces.Continent;
import it.unibo.risikoop.model.interfaces.GameManager;

/**
 * controller for the operations of data adding like adding a player ecc... .
 */
public final class DataAddingControllerImpl implements DataAddingController {
    private final GameManager gameManager;

    /**
     * constructor.
     * 
     * @param gameManager
     */
    public DataAddingControllerImpl(final GameManager gameManager) {
        this.gameManager = gameManager;

    }

    @Override
    public boolean addPlayer(final String nome, final int r, final int g, final int b) {
        return gameManager.addPlayer(nome, new Color(r, g, b));
    }

    @Override
    public boolean loadWorldFromFile(final File file) {
        final Graph newMap = new MultiGraph(file.getName(), false, true);
        final Map<String, Continent> cm = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            while (line != null) {
                if ("begin continent reward units".equals(line)) {
                    line = br.readLine();
                    break;
                }
                if (processMapFromLine(line, newMap)) {
                    return false;
                }
                line = br.readLine();
            }

            gameManager.setWorldMap(newMap);

            while (line != null && !"begin territory".equals(line)) {
                if (processContinentFromLine(line, cm)) {
                    return false;
                }
                line = br.readLine();
            }

            line = br.readLine();
            while (line != null) {
                processContinentsTerritoryFromLine(line, cm);
                line = br.readLine();
            }

            gameManager.setContinents(cm.values().stream().collect(Collectors.toSet()));

        } catch (final IOException e) {
            return false;
        }
        return true;
    }

    private void processContinentsTerritoryFromLine(final String line, final Map<String, Continent> cm) {
        final String[] parts = line.split(":");
        final String continentName = parts[0];
        final String[] territrories = parts[1].split("-");
        for (final String territoryName : territrories) {
            gameManager.getTerritory(territoryName).ifPresent(i -> cm.get(continentName).addTerritory(i));
        }
    }

    private boolean processContinentFromLine(final String line, final Map<String, Continent> cm) {
        final String[] parts = line.split("-");
        if (parts.length != 2) {
            return true;
        }
        try {
            final String continentName = parts[0];
            final int unitReward = Integer.parseInt(parts[1]);
            cm.putIfAbsent(continentName, new ContinentImpl(continentName, unitReward));
        } catch (final NumberFormatException e) {
            return true;
        }
        return false;
    }

    private boolean processMapFromLine(final String line, final Graph newMap) {
        final String[] parts = line.split("-");
        if (parts.length != 2) {
            return true;
        }
        newMap.addEdge(line, parts[0], parts[1]);
        return false;
    }

    @Override
    public void setDefaultMap() {
        gameManager.setDefaultWorld();
    }

}
