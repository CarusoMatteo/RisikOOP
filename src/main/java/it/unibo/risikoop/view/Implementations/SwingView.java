package it.unibo.risikoop.view.Implementations;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import it.unibo.risikoop.controller.Interfaces.Controller;
import it.unibo.risikoop.view.Interfaces.RisikoView;

public class SwingView implements RisikoView {
    private static final int MIN_WIDTH = 1000;
    private static final int MIN_HEIGHT = 600;
    private static final float RIDIM = 1.5f;
    private final Controller controller;
    private final JFrame frame = new JFrame();

    public SwingView(Controller controller) {
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'start'");
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

    private void changeScene(JPanel panel) {

    }

}
