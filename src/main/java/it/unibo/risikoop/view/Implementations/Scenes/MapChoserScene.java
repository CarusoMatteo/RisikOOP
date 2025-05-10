package it.unibo.risikoop.view.Implementations.Scenes;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.graphstream.graph.Graph;

import it.unibo.risikoop.controller.Interfaces.Controller;
import it.unibo.risikoop.controller.utilities.EventType;
import it.unibo.risikoop.controller.utilities.MapExtractor;
import it.unibo.risikoop.controller.utilities.RetrieveType;
import it.unibo.risikoop.view.Implementations.SwingView;

public class MapChoserScene extends JPanel {
    private final Controller controller;
    private final JPanel mapPreview = new JPanel();
    private Graph selectedMap;

    public MapChoserScene(final Controller controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        /**
         * Adding button for the default map
         */
        JButton defaultButton = new JButton("Select default map");
        defaultButton.addActionListener(i -> selectedDefaultMap());
        add(defaultButton, BorderLayout.WEST);
        /**
         * Adding button for the custom map choser
         */
        JButton fileChoser = new JButton("Select file");
        fileChoser.addActionListener(i -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                System.out.println(fileChooser.getSelectedFile());
                selectedMap = MapExtractor.MapExtractorFromFile(fileChooser.getSelectedFile());
                beginPlay();
            }
        });
        add(fileChoser, BorderLayout.EAST);
        /**
         * Adding the selected map preview panel
         */
        add(mapPreview);
        JButton begiGameButton = new JButton("Begin to Play");
        begiGameButton.addActionListener(i -> {
            beginPlay();
        });
        add(begiGameButton, BorderLayout.SOUTH);
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

    private void beginPlay() {
        if (selectedMap == null) {
            JOptionPane.showMessageDialog(this, "No map selected");
        } else {
            this.controller.eventHandle(EventType.SET_MAP_EVENT, Optional.of(selectedMap));
            this.controller.eventHandle(EventType.BEGIN_PLAY, Optional.empty());
        }
    }

    private void selectedDefaultMap() {
        if (controller.retrieveFromModel(RetrieveType.RETRIEVE_DEFAULT_MAP).get() instanceof Graph canonMap) {
            selectedMap = canonMap;
        }
    }

}
