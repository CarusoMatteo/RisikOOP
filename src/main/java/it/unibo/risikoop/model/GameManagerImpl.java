package it.unibo.risikoop.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class GameManagerImpl implements GameManager {

    List<Player> players = new LinkedList<>();

    @Override
    public boolean addPlayer(String name, Color col) {
        if (!players.stream().anyMatch(i -> i.getColor().equals(col) || i.getName().equals(name))) {
            players.add(new PlayerImpl(name, col));
            return true;
        }
        return false;
    }

    @Override
    public void finishingInizialization() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'finishingInizialization'");
    }

    @Override
    public void addUnits(Territory territory, int units) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addUnits'");
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

}
