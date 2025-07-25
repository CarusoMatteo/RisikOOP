package it.unibo.risikoop.view.implementations;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import it.unibo.risikoop.controller.interfaces.Controller;
import it.unibo.risikoop.view.implementations.scenes.MapChoserScene;
import it.unibo.risikoop.view.implementations.scenes.PlayerAddingView;
import it.unibo.risikoop.view.implementations.scenes.mapscene.GameOverPanel;
import it.unibo.risikoop.view.implementations.scenes.mapscene.MapSceneImpl;
import it.unibo.risikoop.view.interfaces.MapScene;
import it.unibo.risikoop.view.interfaces.RisikoView;

/**
 * swing view class.
 */
public final class SwingView implements RisikoView {
    private static final int MIN_WIDTH = 1000;
    private static final int MIN_HEIGHT = 600;
    private static final float RIDIM = 1.5f;
    private final Controller controller;
    private final JFrame frame = new JFrame();
    private Optional<MapScene> mapScene = Optional.empty();
    private JPanel lastPanel;

    /**
     * constructor.
     * 
     * @param controller
     */
    public SwingView(final Controller controller) {
        this.controller = controller;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((int) (screenSize.getWidth() / RIDIM), (int) (screenSize.getHeight() / RIDIM));
        frame.setTitle("Risiko");
        frame.setLocationByPlatform(true);
        frame.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        frame.setVisible(true);
    }

    /**
     * 
     * @param comp
     * @param fontSize
     */
    public static void setFontRecursively(final Component comp, final int fontSize) {
        final Font currentFont = comp.getFont();
        if (currentFont != null) {
            comp.setFont(new Font(currentFont.getName(), currentFont.getStyle(), fontSize));
        }

        if (comp instanceof final Container container) {
            for (final Component child : container.getComponents()) {
                setFontRecursively(child, fontSize);
            }
        }
    }

    @Override
    public void start() {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((int) (screenSize.getWidth() / 3), (int) (screenSize.getHeight() / 4));
        changePanel(frame, new PlayerAddingView(controller));
    }

    @Override
    public void chooseMap() {
        changePanel(frame, new MapChoserScene(controller));
    }

    @Override
    public void beginPlay() {
        final MapSceneImpl mapSceneImpl = new MapSceneImpl(this.controller);
        mapScene = Optional.of(mapSceneImpl);
        changePanel(frame, mapSceneImpl);
    }

    @Override
    public void gameOver() {
        changePanel(frame, new GameOverPanel(controller));
    }

    private JPanel changePanel(
            final Container parent,
            final JPanel newPanel) {
        if (lastPanel != null) {
            parent.remove(lastPanel);
        }
        parent.add(newPanel);
        lastPanel = newPanel;
        newPanel.setVisible(true);
        this.frame.revalidate();
        this.frame.repaint();
        this.frame.setVisible(true);
        return newPanel;
    }

    @Override
    public void showErrorMessage(final String s) {
        JOptionPane.showMessageDialog(frame, s);
    }

    @Override
    public Optional<MapScene> getMapScene() {
        return mapScene;
    }

}
