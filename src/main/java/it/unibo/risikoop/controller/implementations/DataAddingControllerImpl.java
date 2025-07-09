package it.unibo.risikoop.controller.implementations;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.risikoop.controller.interfaces.DataAddingController;
import it.unibo.risikoop.model.implementations.Color;
import it.unibo.risikoop.model.implementations.ContinentImpl;
import it.unibo.risikoop.model.interfaces.Continent;
import it.unibo.risikoop.model.interfaces.GameManager;

/**
 * controller for the operations of data adding like adding a player ecc... .
 */
public final class DataAddingControllerImpl implements DataAddingController {

    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "controllers must access shared GameManager because"
            + "it is a bridge between view and model")
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

    private boolean loadFromJson(final File file) {
        final ObjectMapper mapper = new ObjectMapper();
        final Graph newMap = new MultiGraph(file.getName(), false, true);
        final Map<String, Continent> cm = new HashMap<>();
        try {
            final JsonResult data = mapper.readValue(file, JsonResult.class);
            for (final var edge : data.edges) {
                final String edge1 = edge.get(0);
                final String edge2 = edge.get(1);
                newMap.addEdge(edge1 + "-" + edge2, edge1, edge2);
            }
            gameManager.setWorldMap(newMap);
            for (final var continent : data.continents.entrySet()) {
                final String continentName = continent.getKey();
                final int unitReward = continent.getValue();
                cm.putIfAbsent(continentName, new ContinentImpl(continentName, unitReward));
            }
            for (final var appartenenza : data.appartenenze.entrySet()) {
                for (final String territoryName : appartenenza.getValue()) {
                    gameManager.getTerritory(territoryName)
                            .ifPresent(i -> cm.get(appartenenza.getKey()).addTerritory(i));
                }
            }
            gameManager.setContinents(cm.values().stream().collect(Collectors.toSet()));

        } catch (final IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean loadWorldFromFile(final File file) {
        return loadFromJson(file);
    }

    @Override
    public void setDefaultMap() {
        gameManager.setDefaultWorld();
    }

    private record JsonResult(List<List<String>> edges, Map<String, Integer> continents,
            Map<String, List<String>> appartenenze) {
    }
}
