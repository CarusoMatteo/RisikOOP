package it.unibo.risikoop.controller.implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import it.unibo.risikoop.controller.interfaces.CardGameController;
import it.unibo.risikoop.controller.interfaces.Controller;
import it.unibo.risikoop.controller.interfaces.DataAddingController;
import it.unibo.risikoop.controller.interfaces.DataRetrieveController;
import it.unibo.risikoop.controller.interfaces.GamePhaseController;
import it.unibo.risikoop.model.implementations.GameManagerImpl;
import it.unibo.risikoop.model.implementations.ObjectiveCardFactoryImpl;
import it.unibo.risikoop.model.implementations.TurnManagerImpl;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.Territory;
import it.unibo.risikoop.model.interfaces.TurnManager;
import it.unibo.risikoop.view.implementations.SwingView;
import it.unibo.risikoop.view.interfaces.RisikoView;

/**
 * main controller class.
 */
public final class ControllerImpl implements Controller {

    private final GameManager gameManager = new GameManagerImpl();
    private final List<RisikoView> viewList = new LinkedList<>();
    private TurnManager turnManager;
    private GamePhaseController gamePhaseController;

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
        turnManager = new TurnManagerImpl(gameManager.getPlayers());
        gamePhaseController = new GamePhaseControllerImpl(viewList, turnManager, gameManager, this::gameOver);
        viewList.forEach(RisikoView::beginPlay);

    }

    @Override
    public void gameOver() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'gameOver'");
    }

    @Override
    public DataRetrieveController getDataRetrieveController() {
        return new DataRetrieveControllerImpl(turnManager, gameManager);
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
        for (int i = 0; i < territories.size(); i++) {
            players.get(i % players.size()).addTerritory(territories.get(i));
            territories.get(i).setOwner(players.get(i % players.size()));
        }
        players.forEach(p -> {
            p.setObjectiveCard(new ObjectiveCardFactoryImpl(gameManager).createObjectiveCard(p));
            /**
             * for debug only
             * p.getTerritories().forEach(i -> p.addGameCard(new
             * TerritoryCardImpl(UnitType.CANNON, i)));
             * /** ------------------
             */
        });

    }

    @Override
    public GamePhaseController getGamePhaseController() {
        return gamePhaseController;
    }

    @Override
    public CardGameController getCardGameController() {
        return new CardGameControllerImpl(gameManager);
    }

    @Override
    public boolean isOwned(String territoryName, String playerName) {
        var territoryOptional = gameManager.getTerritory(territoryName);
        var playerOptional = gameManager.getPlayers()
                .stream()
                .filter(p -> p.getName().equals(playerName))
                .findFirst();
        return playerOptional.isPresent()
                && territoryOptional.isPresent()
                && playerOptional.get().getTerritories().contains(territoryOptional.get());
    }
}
