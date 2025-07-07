package it.unibo.risikoop.view.implementations.Scenes;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import it.unibo.risikoop.controller.interfaces.Controller;
import it.unibo.risikoop.view.implementations.SwingView;

public class MapChoserScene extends JPanel {
    private final Controller controller;
    private final JPanel mapPreview = new JPanel();

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
            mapSelection();
        });
        add(fileChoser, BorderLayout.EAST);
        /**
         * Adding the selected map preview panel
         */
        add(mapPreview);
        JButton begiGameButton = new JButton("Begin to Play");
        begiGameButton.addActionListener(i -> {
            controller.beginToPlay();
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

    private void mapSelection() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            System.out.println(fileChooser.getSelectedFile());

            if (controller.getDataAddingController().setWorldFromFile(fileChooser.getSelectedFile())) {
                JOptionPane.showMessageDialog(this.getParent(),
                        "Mappa selezionata correttamente, puoi passare alla prossima fase di gioco oppure selezionare un'altra mappa");
            } else {
                JOptionPane.showMessageDialog(this.getParent(),
                        "Mappa selezionata incorrettamente, formato file probabilmente sbagliato");

            }
        }

    }

    private void selectedDefaultMap() {
        controller.getDataAddingController().setDefaultMap();
    }

}
