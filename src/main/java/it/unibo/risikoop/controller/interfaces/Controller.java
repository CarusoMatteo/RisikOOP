package it.unibo.risikoop.controller.interfaces;

/**
 * 
 */
public interface Controller {
    /**
     * @return the dataAdding controller.
     * 
     */
    DataAddingController getDataAddingController();

    /**
     * 
     * @return the dataretreving controller.
     * 
     */
    DataRetrieveController getDataRetrieveController();

    /**
     * starts the game.
     */
    void start();

    /**
     * after all the data inserting from the player the game begin.
     */
    void beginToPlay();

    /**
     * gives the option to chose the map.
     */
    void beginMapSelection();

    /**
     * ends the game.
     */
    void gameOver();

}
