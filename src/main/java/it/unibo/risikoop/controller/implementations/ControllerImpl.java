package it.unibo.risikoop.controller.implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import it.unibo.risikoop.controller.interfaces.Controller;
import it.unibo.risikoop.controller.interfaces.DataAddingController;
import it.unibo.risikoop.controller.interfaces.DataRetrieveController;
import it.unibo.risikoop.model.implementations.GameManagerImpl;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.view.implementations.SwingView;
import it.unibo.risikoop.view.interfaces.RisikoView;

/**
 * main controller class.
 */
public final class ControllerImpl implements Controller {

    private final GameManager gameManager = new GameManagerImpl();
    private final List<RisikoView> viewList = new LinkedList<>();

    /**
     * constructor.
     */
    public ControllerImpl() {
        viewList.add(new SwingView(this));
    }

    @Override
    public DataAddingController getDataAddingController() {
        return new DataAddingControllerImpl(gameManager);
    }

    @Override
    public void start() {
        viewList.forEach(RisikoView::start);
    }

    @Override
    public void beginMapSelection() {
        viewList.forEach(RisikoView::chooseMap);
    }

    @Override
    public void beginToPlay() {
        assignTerritory();
        viewList.forEach(RisikoView::beginPlay);
    }

    @Override
    public DataRetrieveController getDataRetrieveController() {
        return new DataRetrieveControllerImpl(gameManager);
    }

    @Override
    public void gameOver() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'gameOver'");
    }

    /*
     * private void readObject(final java.io.ObjectInputStream in) throws
     * java.io.IOException, ClassNotFoundException {
     * in.defaultReadObject();
     * // Reinitialize transient fields
     * this.gameManager = new GameManagerImpl();
     * this.viewList = List.of(new SwingView(this));
     * }
     */
    private void assignTerritory() {
        final var players = gameManager.getPlayers();
        final List<Territory> territories = new ArrayList<>(gameManager.getTerritories().stream().toList());
        Collections.shuffle(territories);
        Stream.iterate(0, i -> i++)
                .limit(territories.size())
                .forEach(i -> players.get(i % players.size()).addTerritory(territories.get(i)));
    }

    @Override
    public void showActualPlayer(Player player) {
        viewList.stream().map(v -> v.getMapScene())
                .forEach(s -> s.ifPresent(m -> m.updateCurrentPlayer(player.getName(), player.getColor())));
    }
}
