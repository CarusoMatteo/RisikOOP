package it.unibo.risikoop.view.Implementations.Scenes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.graphstream.graph.Graph;

import it.unibo.risikoop.controller.Interfaces.Controller;
import it.unibo.risikoop.controller.utilities.EventType;
import it.unibo.risikoop.controller.utilities.RetrieveType;
import it.unibo.risikoop.view.Implementations.SwingView;

public class MapChoserScene extends JPanel {
    private final Controller controller;

    public MapChoserScene(final Controller controller) {
        this.controller = controller;
        setBackground(new Color(0, 0, 0));
        setLayout(new BorderLayout());
        JButton defaultButton = new JButton("Select default map");
        defaultButton.addActionListener(i -> selectedDefaultMap());
        add(defaultButton, BorderLayout.WEST);
        JButton fileChoser = new JButton("Select default map");
        fileChoser.addActionListener(i -> {
        });
        add(fileChoser, BorderLayout.EAST);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                int newSize = Math.max(2, width / 50); // Logica semplice
                for (var comp : getComponents()) {
                    SwingView.setFontRecursively(comp, newSize);

                }
            }
        });
    }

    private void selectedDefaultMap() {
        if (controller.retrieveFromModel(RetrieveType.RETRIEVE_DEFAULT_MAP).get() instanceof Graph canMap) {
            controller.eventHandle(EventType.SET_MAP_EVENT, Optional.of(canMap));
            controller.eventHandle(EventType.BEGIN_PLAY, Optional.empty());

        }
    }

}
