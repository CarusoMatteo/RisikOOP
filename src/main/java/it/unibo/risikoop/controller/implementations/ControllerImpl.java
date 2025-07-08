package it.unibo.risikoop.controller.implementations;

import java.util.LinkedList;
import java.util.List;

import it.unibo.risikoop.controller.interfaces.Controller;
import it.unibo.risikoop.controller.interfaces.DataAddingController;
import it.unibo.risikoop.controller.interfaces.DataRetrieveController;
import it.unibo.risikoop.model.implementations.GameManagerImpl;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.view.implementations.SwingView;
import it.unibo.risikoop.view.interfaces.RisikoView;

/**
 * main controller class.
 */
public final class ControllerImpl implements Controller {
    private static final long serialVersionUID = 1L;

    private final GameManager gameManager = new GameManagerImpl();
    private final List<RisikoView> viewList = new LinkedList<>();
    private final DataAddingController dataAddController;
    private final DataRetrieveController dataRetrieveController;

    /**
     * constructor.
     */
    public ControllerImpl() {
        viewList.add(new SwingView(this));
        dataAddController = new DataAddingControllerImpl(gameManager);
        dataRetrieveController = new DataRetrieveControllerImpl(gameManager);
    }

    @Override
    public DataAddingController getDataAddingController() {
        return dataAddController;
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
        viewList.forEach(RisikoView::beginPlay);
    }

    @Override
    public DataRetrieveController getDataRetrieveController() {
        return dataRetrieveController;
    }

    @Override
    public void gameOver() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'gameOver'");
    }

}
