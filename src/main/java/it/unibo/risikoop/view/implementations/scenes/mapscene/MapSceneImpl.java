package it.unibo.risikoop.view.implementations.scenes.mapscene;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JPanel;

import it.unibo.risikoop.controller.interfaces.Controller;
import it.unibo.risikoop.controller.interfaces.DataRetrieveController;
import it.unibo.risikoop.model.interfaces.ObjectiveCard;
import it.unibo.risikoop.model.interfaces.cards.GameCard;
import it.unibo.risikoop.view.implementations.scenes.mapscene.cardpanel.CardJpanel;
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
    private final MapJPanel mapPanel;
    private final CardJpanel cardPanel;
    private final ActionJPanel actionPanel;

    /**
     * Constructor for the MapScene.
     * Scene that displays the regular game loop.
     * Shows the Map, the Current Player, their Cards and available Action Buttons.
     * 
     * @param controller The controller to retrieve graph data and player
     *                   information.
     */
    public MapSceneImpl(final Controller controller) {
        // this.controller = controller;
        final DataRetrieveController data = controller.getDataRetrieveController();

        this.currentPlayerPanel = new CurrentPlayerJPanel(
                data.getCurrentPlayerName(),
                data.getCurrentPlayerColor());

        this.cardPanel = new CardJpanel(
                data.getCurrentPlayerObjectiveCard(),
                data.getCurrentPlayerGameCards(),
                controller);
        // this.cardPanel = getDebugCardPanel(controller);

        this.actionPanel = new ActionJPanel(controller);
        this.mapPanel = new MapJPanel(actionPanel, controller);

        setLayout(new GridBagLayout());
        setGridBagConstraints();
    }

    @Override
    public void updateCurrentPlayer(
            final String playerName,
            final it.unibo.risikoop.model.implementations.Color playerColor,
            final ObjectiveCard objectiveCard,
            final List<GameCard> cards) {

        currentPlayerPanel.updateCurrentPlayer(
                playerName,
                new Color(playerColor.r(), playerColor.g(), playerColor.b()));
        cardPanel.updateCurrentPlayerCards(objectiveCard, cards);
    }
    /*
     * private CardJpanel getDebugCardPanel(final Controller controller) {
     * return new CardJpanel(
     * new ObjectiveCard() {
     * 
     * @Override
     * public boolean isAchieved() {
     * return false;
     * }
     * 
     * @Override
     * public String getDescription() {
     * return "Conquer 24 territories.";
     * }
     * },
     * List.of(new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(new
     * GameManagerImpl(), "Iceland")),
     * new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(new
     * GameManagerImpl(), "Quebec")),
     * new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(new
     * GameManagerImpl(), "Scandinavia")),
     * new WildCardImpl(),
     * new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(new
     * GameManagerImpl(), "Congo")),
     * new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(new
     * GameManagerImpl(), "East Africa")),
     * new WildCardImpl(),
     * new TerritoryCardImpl(UnitType.KNIGHT, new TerritoryImpl(new
     * GameManagerImpl(), "Kamchatka")),
     * new TerritoryCardImpl(UnitType.CANNON, new TerritoryImpl(new
     * GameManagerImpl(), "Siberia")),
     * new WildCardImpl(),
     * new WildCardImpl(),
     * new TerritoryCardImpl(UnitType.JACK, new TerritoryImpl(new GameManagerImpl(),
     * "Argentina")),
     * new WildCardImpl(),
     * new WildCardImpl(),
     * new WildCardImpl()),
     * controller);
     * }
     */

    private void setGridBagConstraints() {
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        final JPanel leftPanel = new JPanel();
        final JPanel rightPanel = new JPanel();

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
        gbc.weightx = 1;
        gbc.weighty = SMALL_PANEL_PROPORTION;
        leftPanel.add(this.currentPlayerPanel, gbc);

        // cardPanel
        gbc.gridy = 1;
        gbc.weightx = 1;
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

    /**
     * change the displayed source territory.
     * 
     * @param srcTerritoryName the new text
     */
    @Override
    public void updateSrcTerritory(String srcTerritoryName) {
        actionPanel.updateSrcTerritory(srcTerritoryName);
    }

    /**
     * change the displayed destination territory.
     * 
     * @param srcTerritoryName the new text
     */
    @Override
    public void updateDstTerritory(String dstTerritoryName) {
        actionPanel.updateDstTerritory(dstTerritoryName);
    }

    /**
     * change the displayed phase related text.
     * 
     * @param srcTerritoryKindString
     * @param dstterritoryKindString
     * @param changeStateButonString
     */
    @Override
    public void updatePhaseRelatedText(String srcTerritoryKindString, String dstterritoryKindString,
            String changeStateButonString) {
        actionPanel.updatePhaseRelatedText(srcTerritoryKindString, dstterritoryKindString, changeStateButonString);
    }

    @Override
    public void changeTerritoryUnits(String territoryName, int units) {
        mapPanel.changeUnitsOfTerritory(territoryName, units);
    }

    @Override
    public void enableAction(boolean toEnable) {
        actionPanel.enableActionPanel(toEnable);
    }
}
