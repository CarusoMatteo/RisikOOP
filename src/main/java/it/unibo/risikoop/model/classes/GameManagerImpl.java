package it.unibo.risikoop.model.classes;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.graphstream.graph.Graph;

import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;

public class GameManagerImpl implements GameManager {

    private final List<Player> players = new LinkedList<>();
    private final Set<Territory> territories = new HashSet<>();
    private Graph worldMap;

    public GameManagerImpl() {
    }

    @Override
    public boolean addPlayer(String name, Color col) {
        if (!players.stream().anyMatch(i -> i.getColor().equals(col) || i.getName().equals(name))) {
            players.add(new PlayerImpl(name, col));
            return true;
        }
        return false;
    }

    @Override
    public Optional<Territory> getTerritory(String name) {
        return territories.stream()
                .filter(i -> i.getName().equals(name))
                .findFirst();
    }

    @Override
    public void addUnits(String name, int units) {
        getTerritory(name).ifPresent(i -> i.addUnits(units));
    }

    @Override
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    @Override
    public boolean removePlayer(String name) {
        Optional<Player> remove = players.stream().filter(i -> i.getName().equals(name)).findAny();
        if (remove.isPresent()) {
            players.remove(remove.get());
            return true;
        }
        return false;
    }

    @Override
    public Set<Territory> getTerritories() {
        return territories;
    }

    @Override
    public void setWorldMap(Graph worldMap) {
        this.worldMap = worldMap;
        territories.addAll(worldMap.nodes().map(i -> new TerritoryImpl(this, i.getId())).collect(Collectors.toSet()));

    }

    @Override
    public Graph getWorldMap() {
        return worldMap;
    }

    @Override
    public Set<Territory> getTerritoryNeightbours(String name) {
        Optional<Territory> territory = getTerritory(name);
        if (territory.isEmpty()) {
            return Set.of();
        }
        return territory.get().getNeightbours();

    }

    @Override
    public void removeUnits(String TerritoryName, int units) {
        getTerritory(TerritoryName).ifPresent(i -> i.removeUnits(units));
    }

}
