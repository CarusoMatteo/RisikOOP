package it.unibo.risikoop.view.Implementations.Scenes;

import java.awt.Color;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.graphstream.graph.Graph;

import it.unibo.risikoop.controller.Interfaces.Controller;
import it.unibo.risikoop.controller.utilities.EventType;
import it.unibo.risikoop.controller.utilities.RetrieveType;

public class MapChoserScene extends JPanel {
    private final Controller controller;

    public MapChoserScene(final Controller controller) {
        this.controller = controller;
        setBackground(new Color(0, 0, 0));
        JButton but = new JButton("PressMe");
        but.addActionListener(i -> selectedDefaultMap());
        add(but);
    }

    private void selectedDefaultMap() {
        if (controller.retrieveFromModel(RetrieveType.RETRIEVE_DEFAULT_MAP).get() instanceof Graph canMap) {
            controller.eventHandle(EventType.SET_MAP_EVENT, Optional.of(canMap));
        }
    }

}
