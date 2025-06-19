package it.unibo.risikoop.controller.Interfaces;

import java.io.File;

public interface DataAddingController {
    public boolean addPlayer(String nome, int r, int g, int b);

    public boolean setWorldFromFile(File file);

    public default boolean setWorldFromFile(String file) {
        return setWorldFromFile(new File(file));
    }

    public void setDefaultMap();
}
