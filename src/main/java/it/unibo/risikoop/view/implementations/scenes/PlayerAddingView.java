package it.unibo.risikoop.view.implementations.scenes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import it.unibo.risikoop.controller.interfaces.Controller;
import it.unibo.risikoop.view.implementations.SwingView;

/**
 * first scene.
 */
public final class PlayerAddingView extends JPanel {
    private static final long serialVersionUID = 1L;
    private final transient Controller controller;
    private final JPanel playerListPanel = new JPanel();
    private final JPanel inputPanel = new JPanel();
    private final DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(
            new String[] {});
    private final JList<String> playerList = new JList<>(model);

    /**
     * constructor.
     * 
     * @param controller
     */
    public PlayerAddingView(final Controller controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        inputPanel.setLayout(new GridBagLayout());
        /*
         * 
         */
        playerListPanel.add(playerList);
        add(inputPanel, BorderLayout.CENTER);
        add(playerListPanel, BorderLayout.AFTER_LINE_ENDS);
        /**
         * 
         */
        final GridBagConstraints c = new GridBagConstraints();
        // natural height, maximum width
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;

        c.gridx = 0;
        c.gridy = 0;
        inputPanel.add(new Label("Inserisci il nome del giocatore"), c);

        final TextField text = new TextField("Giocatore 1", 1);
        c.gridx = 0;
        c.gridy = 1;
        inputPanel.add(text, c);

        c.gridx = 0;
        c.gridy = 2;
        inputPanel.add(new JLabel("selezione il suo colore"), c);

        c.gridx = 0;
        c.gridy = 3;
        final var tcc = new JColorChooser(new Color(255, 0, 0));
        inputPanel.add(tcc, c);

        final JButton button = new JButton("Add Player");
        c.gridx = 0;
        c.gridy = 4;
        inputPanel.add(button, c);

        button.addActionListener(i -> {
            final var col = tcc.getColor();
            if (this.controller.getDataAddingController().addPlayer(text.getText(), col.getRed(),
                    col.getGreen(), col.getBlue())) {
                JOptionPane.showMessageDialog(this.getParent(), "Giocatore aggiunto correttamente");
            } else {
                JOptionPane.showMessageDialog(this.getParent(),
                        "Errore nell'inserimento, nome giocatore opppure colore già presenti");
            }

            updatePlayerListMine();
        });
        final JButton finishButton = new JButton("End");
        finishButton.addActionListener(i -> {
            if (controller.getDataRetrieveController().getPlayerList().size() >= 2) {
                controller.beginMapSelection();
            } else {
                JOptionPane.showMessageDialog(this, "Have to add at least two characters");
            }
        });
        add(finishButton, BorderLayout.SOUTH);
        /**
         * Making the fonts dynamic
         */
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final int width = getWidth();
                final int newSize = Math.max(2, width / 50); // Logica semplice
                SwingView.setFontRecursively(tcc, newSize);
                for (final var comp : getComponents()) {
                    SwingView.setFontRecursively(comp, newSize);

                }
            }
        });
    }

    private void updatePlayerListMine() {
        model.removeAllElements();
        controller.getDataRetrieveController().getPlayerList().forEach(i -> model.addElement(i.getName()));
    }

}
