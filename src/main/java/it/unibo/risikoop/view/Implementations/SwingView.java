package it.unibo.risikoop.view.Implementations;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import it.unibo.risikoop.controller.Interfaces.Controller;
import it.unibo.risikoop.view.Interfaces.RisikoView;

public class SwingView implements RisikoView {
    private static final int MIN_WIDTH = 1000;
    private static final int MIN_HEIGHT = 600;
    private static final float RIDIM = 1.5f;
    private final Controller controller;
    private final JFrame frame = new JFrame();
    private final PlayerAddingView playerAddingView;

    public SwingView(Controller controller) {
        playerAddingView = new PlayerAddingView(controller);
        this.controller = controller;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((int) (screenSize.getWidth() / RIDIM), (int) (screenSize.getHeight() / RIDIM));
        frame.setTitle("Risiko");
        frame.setLocationByPlatform(true);
        frame.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        frame.setVisible(true);
    }

    @Override
    public void start() {
        changePanel(frame, null, playerAddingView);
    }

    @Override
    public void choose_map() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'chose_map'");
    }

    @Override
    public void begin_play() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'begin_play'");
    }

    @Override
    public void game_over() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'game_over'");
    }

    private JPanel changePanel(
            final Container parent,
            final JPanel oldPanel,
            final JPanel newPanel) {
        if (oldPanel != null) {
            parent.remove(oldPanel);
        }
        parent.add(newPanel);
        newPanel.setVisible(true);
        this.frame.revalidate();
        this.frame.repaint();
        this.frame.setVisible(true);
        return newPanel;
    }

    @Override
    public void show_player_add_failed() {
        JOptionPane.showMessageDialog(playerAddingView, "The charaters name or color has already been used");
    }

}
