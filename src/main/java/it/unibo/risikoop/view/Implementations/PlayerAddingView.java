package it.unibo.risikoop.view.Implementations;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.TextField;
import java.util.List;
import java.util.Optional;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import it.unibo.risikoop.controller.Interfaces.Controller;
import it.unibo.risikoop.controller.utilities.EventType;
import it.unibo.risikoop.model.interfaces.Player;
import it.unibo.risikoop.view.Interfaces.Scene;

public class PlayerAddingView extends JPanel implements Scene {
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
            controller.eventHandle(EventType.ADD_PLAYER_EVENT,
                    Optional.of(
                            List.of(text.getText(), col.getRed(), col.getGreen(), col.getBlue())));
        });
        JButton finishButton = new JButton("End");
        finishButton.addActionListener(i -> controller.eventHandle(EventType.SELECT_MAP_BEGIN, Optional.empty()));
        add(finishButton, BorderLayout.SOUTH);
    }

    @Override
    public void updatePlayerList(List<Player> newPlayerList) {
        newPlayerList.forEach(i -> System.out.println(i.getName() + " " + i.getTotalUnits()));
        model.removeAllElements();
        newPlayerList.forEach(i -> model.addElement(i.getName()));
    }
}
