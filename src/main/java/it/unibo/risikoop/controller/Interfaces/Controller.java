package it.unibo.risikoop.controller.Interfaces;

public interface Controller {
    /**
     * @return
     * 
     */

    public DataAddingController getDataAddingController();

    public DataRetrieveController getDataRetrieveController();

    public void start();

    public void beginToPlay();

    public void beginMapSelection();

}
