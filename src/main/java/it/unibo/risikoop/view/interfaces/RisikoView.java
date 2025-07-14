package it.unibo.risikoop.view.interfaces;

import java.util.Optional;

/**
 * view interfaces.
 */
public interface RisikoView {
    /**
     * 
     */
    void start();

    /** 
     * 
    */
    void chooseMap();

    /**
     * 
     */
    void beginPlay();

    /**
     * 
     */
    void gameOver();

    /**
     * @param s
     */
    void showErrorMessage(String s);

    /**
     * Returns the MapScene if it exists.
     * 
     * @return Optional containing the MapScene if it exists, otherwise empty,
     *         since it may not be created at the beginning.
     */
    Optional<MapScene> getMapScene();
}
