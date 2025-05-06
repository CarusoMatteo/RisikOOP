package it.unibo.risikoop.view.Implementations;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.TextField;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;

import it.unibo.risikoop.controller.Interfaces.Controller;
import it.unibo.risikoop.controller.utilities.EventType;

public class PlayerAddingView extends JPanel {
    private final Controller controller;

    public PlayerAddingView(final Controller controller) {
        this.controller = controller;
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        // natural height, maximum width
        c.fill = GridBagConstraints.HORIZONTAL;

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        add(new Label("Inserisci il giocatore"), c);

        TextField text = new TextField("Giocatore 1", 1);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        add(text, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        var tcc = new JColorChooser(new Color(255, 0, 0));
        add(tcc, c);

        JButton button = new JButton("Button 1");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        add(button, c);

        button.addActionListener(i -> {
            var col = tcc.getColor();
            controller.eventHandle(EventType.ADD_PLAYER_EVENT,
                    Optional.of(
                            List.of(text.getText(), col.getRed(), col.getGreen(), col.getBlue())));
        });
    }
}
