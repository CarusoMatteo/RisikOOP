package it.unibo.risikoop.model.implementations;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.graphstream.graph.Graph;

import it.unibo.risikoop.model.interfaces.Continent;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;

/**
 * game manager implementation.
 */
public final class GameManagerImpl implements GameManager {
    private final List<Player> players = new LinkedList<>();
    private final Set<Territory> territories = new HashSet<>();
    private final Set<Continent> continents = new HashSet<>();
    private Optional<Graph> worldMap = Optional.empty();

    @Override
    public boolean addPlayer(final String name, final Color col) {
        if (!players.stream().anyMatch(i -> i.getColor().equals(col) || i.getName().equals(name))) {
            players.add(new PlayerImpl(name, col));
            return true;
        }
        return false;
    }

    @Override
    public Optional<Territory> getTerritory(final String name) {
        return territories.stream()
                .filter(i -> i.getName().equals(name))
                .findFirst();
    }

    @Override
    public Optional<Continent> getContinent(final String name) {
        return continents.stream()
                .filter(i -> i.getName().equals(name))
                .findFirst();
    }

    @Override
    public void addUnits(final String name, final int units) {
        getTerritory(name).ifPresent(i -> i.addUnits(units));
    }

    @Override
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    @Override
    public boolean removePlayer(final String name) {
        final Optional<Player> remove = players.stream().filter(i -> i.getName().equals(name)).findAny();
        if (remove.isPresent()) {
            players.remove(remove.get());
            return true;
        }
        return false;
    }

    @Override
    public Set<Territory> getTerritories() {
        return Collections.unmodifiableSet(territories);
    }

    @Override
    public void setWorldMap(final Graph worldMap) {
        this.worldMap = Optional.of(worldMap);
        territories.addAll(worldMap.nodes().map(i -> new TerritoryImpl(this, i.getId())).collect(Collectors.toSet()));

    }

    @Override
    public void setContinents(final Set<Continent> continents) {
        this.continents.addAll(continents);

    }

    @Override
    public Set<Continent> getContinents() {
        return Collections.unmodifiableSet(continents);
    }

    @Override
    public Graph getActualWorldMap() {
        return worldMap.get();
    }

    @Override
    public Set<Territory> getTerritoryNeightbours(final String name) {
        final Optional<Territory> territory = getTerritory(name);
        if (territory.isEmpty()) {
            return Set.of();
        }
        return territory.get().getNeightbours();

    }

    @Override
    public void removeUnits(final String territoryName, final int units) {
        getTerritory(territoryName).ifPresent(i -> i.removeUnits(units));
    }

    @Override
    public void removeAllTerritoriesAndContinents() {
        territories.removeIf(i -> true);
        continents.removeIf(i -> true);
        worldMap.ifPresent(Graph::clear);
    }

}
