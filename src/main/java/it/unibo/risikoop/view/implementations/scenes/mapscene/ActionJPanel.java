package it.unibo.risikoop.view.implementations.scenes.mapscene;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.unibo.risikoop.controller.interfaces.Controller;

/**
 * Panel for the Action Buttons in the MapScene.
 */
// commit before merge
public final class ActionJPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final JLabel srcTerritoryLabel = new JLabel("Prova src");
    private final JLabel srcTerritoryDesc = new JLabel("Territorio partenza");
    private final JLabel dstTerritoryLabel = new JLabel("Prova dst");
    private final JLabel dstTerritoryDesc = new JLabel("Territorio destinazione");
    private final JButton performeActionButton = new JButton("Esegui azione");
    private final JButton changeStateButton = new JButton("Cambia Stato");
    private final JLabel stateLabel;
    private final JTextField unitsTextField = new JTextField();
    private final JPanel statePanel = labelButton();
    private final Controller controller;

    /**
     * constructor.
     * 
     * @param controller the game controller
     */
    public ActionJPanel(final Controller controller) {
        this.controller = controller;
        this.stateLabel = new JLabel(controller.getGamePhaseController().getStateDescription() + " "
                + controller.getGamePhaseController().getInnerStatePhaseDescription());
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(statePanel());
        add(stateLabel);
        add(changeStateButton);
        changeStateButton.addActionListener(i -> {
            this.changeState();
            this.stateLabel.setText(controller.getGamePhaseController().getStateDescription() + " "
                    + controller.getGamePhaseController().getInnerStatePhaseDescription());
        });
    }

    private JPanel statePanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        panel.add(statePanel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 1;
        gbc.insets = new Insets(5, 0, 5, 5);
        panel.add(performeActionButton, gbc);
        performeActionButton.addActionListener(i -> {
            controller.getGamePhaseController().performAction();
            this.stateLabel.setText(controller.getGamePhaseController().getStateDescription() + " "
                    + controller.getGamePhaseController().getInnerStatePhaseDescription());
        });
        return panel;
    }

    private JPanel labelButton() {
        final JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        final GridBagConstraints labelGbc = new GridBagConstraints();
        labelGbc.fill = GridBagConstraints.BOTH;
        labelGbc.insets = new Insets(2, 2, 2, 2);

        labelGbc.gridx = 0;
        labelGbc.gridy = 0;
        labelGbc.weightx = 0;
        labelGbc.weighty = 1;
        panel.add(srcTerritoryDesc, labelGbc);

        labelGbc.gridx = 1;
        labelGbc.gridy = 0;
        labelGbc.weightx = 1;
        labelGbc.weighty = 1;
        panel.add(srcTerritoryLabel, labelGbc);

        labelGbc.gridx = 0;
        labelGbc.gridy = 1;
        labelGbc.weightx = 0;
        labelGbc.weighty = 1;
        panel.add(dstTerritoryDesc, labelGbc);

        labelGbc.gridx = 1;
        labelGbc.gridy = 1;
        labelGbc.weightx = 1;
        labelGbc.weighty = 1;
        panel.add(dstTerritoryLabel, labelGbc);

        labelGbc.gridx = 0;
        labelGbc.gridy = 2;
        labelGbc.weightx = 0;
        labelGbc.weighty = 1;
        panel.add(new JLabel("Number of units to use"), labelGbc);

        labelGbc.gridx = 1;
        labelGbc.gridy = 2;
        labelGbc.weightx = 1;
        labelGbc.weighty = 1;
        panel.add(unitsTextField, labelGbc);

        return panel;
    }

    /**
     * change the displayed source territory.
     * 
     * @param srcTerritoryName the new text
     */
    public void updateSrcTerritory(String srcTerritoryName) {
        this.srcTerritoryLabel.setText(srcTerritoryName);
    }

    /**
     * change the displayed destination territory.
     * 
     * @param srcTerritoryName the new text
     */
    public void updateDstTerritory(String dstTerritoryName) {
        this.dstTerritoryLabel.setText(dstTerritoryName);
    }

    /**
     * change the displayed phase related text.
     * 
     * @param srcTerritoryKindString
     * @param dstterritoryKindString
     * @param changeStateButonString
     */
    public void updatePhaseRelatedText(String srcTerritoryKindString, String dstterritoryKindString,
            String changeStateButonString) {
        this.srcTerritoryDesc.setText(srcTerritoryKindString);
        this.dstTerritoryDesc.setText(dstterritoryKindString);
        this.changeStateButton.setText(changeStateButonString);
    }

    /**
     * set if the action panel should be enabled.
     * 
     * @param toEnable
     */
    public void enableActionPanel(boolean toEnable) {
        statePanel.setVisible(toEnable);
    }

    private void changeState() {
        controller.getGamePhaseController().nextPhase();
    }
}