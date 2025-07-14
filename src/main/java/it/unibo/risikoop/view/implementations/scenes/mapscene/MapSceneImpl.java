package it.unibo.risikoop.view.implementations.scenes.mapscene;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import it.unibo.risikoop.controller.interfaces.Controller;
import it.unibo.risikoop.view.interfaces.MapScene;

/**
 * Scene that displays the regular game loop.
 * Shows the Map, the Current Player, their Cards and available Action Buttons.
 */
public final class MapSceneImpl extends JPanel implements MapScene {
    private static final double BIG_PANEL_PROPORTION = 0.8;
    private static final double SMALL_PANEL_PROPORTION = 0.2;
    private static final long serialVersionUID = 1L;

    // private final Controller controller;
    private final CurrentPlayerJPanel currentPlayerPanel;
    private final JPanel mapPanel;
    private final JPanel cardPanel;
    private final JPanel actionPanel;

    /**
     * Constructor for the MapScene.
     * Scene that displays the regular game loop.
     * Shows the Map, the Current Player, their Cards and available Action Buttons.
     * 
     * @param controller The controller to retrieve graph data.
     */
    public MapSceneImpl(final Controller controller) {
        // this.controller = controller;

        this.currentPlayerPanel = new CurrentPlayerJPanel(
                "Giocatore 1",
                new it.unibo.risikoop.model.implementations.Color(0, 255, 255));
        /*
         * this.currentPlayerPanel = new CurrentPlayerJPanel(
         * controller.getDataRetrieveController().getCurrentPlayerName(),
         * controller.getDataRetrieveController().getCurrentPlayerColor());
         */
        this.mapPanel = new MapJPanel(controller);
        this.cardPanel = new CardJpanel();
        this.actionPanel = new ActionJPanel();

        setDebugPanelColors();
        setLayout(new GridBagLayout());
        setGridBagConstraints();
    }

    // TODO Remove when actual panels are implemented.
    private void setDebugPanelColors() {
        this.cardPanel.setBackground(Color.GREEN);
        this.actionPanel.setBackground(Color.ORANGE);
    }

    private void setGridBagConstraints() {
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        final JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.BLACK);
        final JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.DARK_GRAY);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = SMALL_PANEL_PROPORTION;
        gbc.weighty = 1;
        add(leftPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = BIG_PANEL_PROPORTION;
        gbc.weighty = 1;
        add(rightPanel, gbc);

        populateLeftPanel(leftPanel);
        populateRightPanel(rightPanel);
    }

    private void populateLeftPanel(final JPanel leftPanel) {
        leftPanel.setLayout(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        // currentPlayerPanel
        gbc.gridy = 0;
        gbc.weightx = SMALL_PANEL_PROPORTION;
        gbc.weighty = SMALL_PANEL_PROPORTION;
        leftPanel.add(this.currentPlayerPanel, gbc);

        // cardPanel
        gbc.gridy = 1;
        gbc.weightx = BIG_PANEL_PROPORTION;
        gbc.weighty = BIG_PANEL_PROPORTION;
        leftPanel.add(this.cardPanel, gbc);
    }

    private void populateRightPanel(final JPanel rightPanel) {
        rightPanel.setLayout(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        // mapPanel
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = BIG_PANEL_PROPORTION;
        rightPanel.add(this.mapPanel, gbc);

        // actionPanel
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = SMALL_PANEL_PROPORTION;
        rightPanel.add(this.actionPanel, gbc);
    }

    @Override
    public void updateCurrentPlayer(String playerName, it.unibo.risikoop.model.implementations.Color playerColor) {
        currentPlayerPanel.updateCurrentPlayer(
                playerName,
                new Color(playerColor.r(), playerColor.g(), playerColor.b()));
    }
}
