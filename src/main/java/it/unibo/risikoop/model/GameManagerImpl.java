package it.unibo.risikoop.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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

}
