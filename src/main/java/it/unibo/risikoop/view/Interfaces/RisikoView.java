package it.unibo.risikoop.view.interfaces;

public interface RisikoView {
    /**
     * 
     */
    public void start();

    /** 
     * 
    */
    public void choose_map();

    /**
     * 
     */
    public void begin_play();

    /**
     * 
     */
    public void game_over();

    /**
     * 
     */
    public void showErrorMessage(String s);
}
