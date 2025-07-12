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

    Optional<MapScene> getMapScene();
}
