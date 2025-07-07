package it.unibo.risikoop.view.implementations.Scenes;

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

public class PlayerAddingView extends JPanel {
    private final Controller controller;
    private final JPanel playerListPanel = new JPanel();
    private final JPanel inputPanel = new JPanel();
    private final DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(
            new String[] {});
    private final JList<String> playerList = new JList<>(model);

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
        GridBagConstraints c = new GridBagConstraints();
        // natural height, maximum width
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;

        c.gridx = 0;
        c.gridy = 0;
        inputPanel.add(new Label("Inserisci il nome del giocatore"), c);

        TextField text = new TextField("Giocatore 1", 1);
        c.gridx = 0;
        c.gridy = 1;
        inputPanel.add(text, c);

        c.gridx = 0;
        c.gridy = 2;
        inputPanel.add(new JLabel("selezione il suo colore"), c);

        c.gridx = 0;
        c.gridy = 3;
        var tcc = new JColorChooser(new Color(255, 0, 0));
        inputPanel.add(tcc, c);

        JButton button = new JButton("Add Player");
        c.gridx = 0;
        c.gridy = 4;
        inputPanel.add(button, c);

        button.addActionListener(i -> {
            var col = tcc.getColor();
            if (this.controller.getDataAddingController().addPlayer(text.getText(), col.getRed(),
                    col.getGreen(), col.getBlue())) {
                JOptionPane.showMessageDialog(this.getParent(), "Giocatore aggiunto correttamente");
            } else {
                JOptionPane.showMessageDialog(this.getParent(),
                        "Errore nell'inserimento, nome giocatore opppure colore già presenti");
            }

            updatePlayerListMine();
        });
        JButton finishButton = new JButton("End");
        finishButton.addActionListener(i -> controller.beginMapSelection());
        add(finishButton, BorderLayout.SOUTH);
        /**
         * Making the fonts dynamic
         */
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                int newSize = Math.max(2, width / 50); // Logica semplice
                SwingView.setFontRecursively(tcc, newSize);
                for (var comp : getComponents()) {
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
