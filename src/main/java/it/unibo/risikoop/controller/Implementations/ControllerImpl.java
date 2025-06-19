package it.unibo.risikoop.controller.Implementations;

import java.util.LinkedList;
import java.util.List;

import it.unibo.risikoop.controller.Interfaces.Controller;
import it.unibo.risikoop.controller.Interfaces.DataAddingController;
import it.unibo.risikoop.controller.Interfaces.DataRetrieveController;
import it.unibo.risikoop.model.Implementations.GameManagerImpl;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.view.Implementations.SwingView;
import it.unibo.risikoop.view.Interfaces.RisikoView;

public class ControllerImpl implements Controller {
    private final GameManager gameManager = new GameManagerImpl();
    private final List<RisikoView> viewList = new LinkedList<>();
    private final DataAddingController dataAddController;
    private final DataRetrieveController dataRetrieveController;

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
        viewList.forEach(i -> i.start());
    }

    @Override
    public void beginMapSelection() {
        viewList.forEach(i -> i.choose_map());
    }

    @Override
    public void beginToPlay() {
        viewList.forEach(i -> i.begin_play());
    }

    @Override
    public DataRetrieveController getDataRetrieveController() {
        return dataRetrieveController;
    }

}
