package it.unibo.risikoop.controller.implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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

    private GameManager gameManager;
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
        gameManager = new GameManagerImpl();
        viewList.forEach(RisikoView::start);
    }

    @Override
    public void beginMapSelection() {
        viewList.forEach(RisikoView::chooseMap);
    }

    @Override
    public void beginToPlay() {
        if (gameManager == null) {
            throw new IllegalStateException("GameManager is not initialized.");
        }

        assignTerritory();
        turnManager = new TurnManagerImpl(gameManager.getPlayers());
        gamePhaseController = new GamePhaseControllerImpl(viewList, turnManager, gameManager, this::gameOver);
        viewList.forEach(RisikoView::beginPlay);

    }

    @Override
    public void gameOver() {
        viewList.forEach(RisikoView::gameOver);
    }

    @Override
    public DataRetrieveController getDataRetrieveController() {
        return new DataRetrieveControllerImpl(turnManager, gameManager);
    }

    private void assignTerritory() {
        if (gameManager == null) {
            throw new IllegalStateException("GameManager is not initialized.");
        }

        final var players = gameManager.getPlayers();
        final List<Territory> territories = new ArrayList<>(gameManager.getTerritories().stream().toList());
        Collections.shuffle(territories);
        for (int i = 0; i < territories.size(); i++) {
            players.get(i % players.size()).addTerritory(territories.get(i));
            territories.get(i).setOwner(players.get(i % players.size()));
        }
        players.forEach(p -> {
            p.setObjectiveCard(new ObjectiveCardFactoryImpl(gameManager).createObjectiveCard(p));

        });

    }

    @Override
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public GamePhaseController getGamePhaseController() {
        return gamePhaseController;
    }

    @Override
    public CardGameController getCardGameController() {
        return new CardGameControllerImpl(gameManager);
    }

}
