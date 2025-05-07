package it.unibo.risikoop.view.Implementations.Scenes;

import java.awt.Color;
import java.util.List;

import javax.swing.JPanel;

import it.unibo.risikoop.controller.Interfaces.Controller;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.view.Interfaces.Scene;

public class MapChoser extends JPanel implements Scene {
    private final Controller controller;

    public MapChoser(final Controller controller) {
        this.controller = controller;
        setBackground(new Color(0, 0, 0));
    }

    @Override
    public void updatePlayerList(List<Player> newPlayerList) {
    }

}
