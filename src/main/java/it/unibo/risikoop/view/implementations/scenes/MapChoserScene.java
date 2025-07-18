package it.unibo.risikoop.view.implementations.scenes;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import it.unibo.risikoop.controller.interfaces.Controller;
import it.unibo.risikoop.view.implementations.SwingView;

/**
 * second scene.
 */
public final class MapChoserScene extends JPanel {
    private static final long serialVersionUID = 1L;
    private final transient Controller controller;
    private final MapPreviewPanel mapPreview;
    private boolean firstSelectionMade;

    /**
     * constructor.
     * 
     * @param controller
     */
    public MapChoserScene(final Controller controller) {
        this.controller = controller;
        this.mapPreview = new MapPreviewPanel(controller);
        setLayout(new BorderLayout());
        /*
         * Adding button for the default map.
         */
        final JButton defaultButton = new JButton("Select default map");
        defaultButton.addActionListener(i -> selectedDefaultMap());
        add(defaultButton, BorderLayout.WEST);
        /*
         * Adding button for the custom map choser.
         */
        final JButton fileChoser = new JButton("Select file");
        fileChoser.addActionListener(i -> {
            mapSelection();
        });
        add(fileChoser, BorderLayout.EAST);
        /*
         * Adding the selected map preview panel.
         */
        add(mapPreview, BorderLayout.CENTER);
        final JButton begiGameButton = new JButton("Begin to Play");
        begiGameButton.addActionListener(i -> {
            if (firstSelectionMade) {
                controller.beginToPlay();
            } else {
                JOptionPane.showMessageDialog(this.getParent(),
                        "   Prima seleziona una mappa",
                        "Error message", JOptionPane.ERROR_MESSAGE);
            }

        });
        add(begiGameButton, BorderLayout.SOUTH);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final int width = getWidth();
                final int newSize = Math.max(2, width / 50); // Logica semplice
                for (final var comp : getComponents()) {
                    SwingView.setFontRecursively(comp, newSize);

                }
            }
        });
    }

    private void mapSelection() {
        final JFileChooser fileChooser = new JFileChooser();
        final int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            firstSelectionMade = controller.getDataAddingController().loadWorldFromFile(fileChooser.getSelectedFile());
            if (firstSelectionMade) {
                mapPreview.updatePreview();
                JOptionPane.showMessageDialog(this.getParent(),
                        "Mappa selezionata correttamente, puoi passare alla prossima fase di gioco"
                                + " oppure selezionare un'altra mappa");
            } else {
                JOptionPane.showMessageDialog(this.getParent(),
                        "Mappa selezionata incorrettamente, formato file probabilmente sbagliato",
                        "Error message", JOptionPane.ERROR_MESSAGE);

            }
        }

    }

    private void selectedDefaultMap() {
        controller.getDataAddingController().setDefaultMap();
        mapPreview.updatePreview();
        firstSelectionMade = true;
    }

}
