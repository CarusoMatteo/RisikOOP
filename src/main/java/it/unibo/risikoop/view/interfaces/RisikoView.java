package it.unibo.risikoop.view.interfaces;

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
}
