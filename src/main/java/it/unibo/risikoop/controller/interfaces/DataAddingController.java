package it.unibo.risikoop.controller.interfaces;

import java.io.File;

/**
 * 
 */
public interface DataAddingController {
    /**
     * 
     * @param nome
     * @param r
     * @param g
     * @param b
     * @return if could succesfully add a player
     */
    boolean addPlayer(String nome, int r, int g, int b);

    /**
     * 
     * @param file
     * @return if could succesfully load the world from the file
     */
    boolean loadWorldFromFile(File file);

    /**
     * 
     * @param file
     * @return @return if could succesfully load the world from the file
     */
    default boolean loadWorldFromFile(final String file) {
        return loadWorldFromFile(new File(file));
    }

    /**
     * 
     */
    void setDefaultMap();
}
