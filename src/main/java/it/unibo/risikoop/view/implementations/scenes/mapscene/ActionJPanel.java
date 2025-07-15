package it.unibo.risikoop.view.implementations.scenes.mapscene;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.unibo.risikoop.controller.interfaces.Controller;

/**
 * Panel for the Action Buttons in the MapScene.
 */
public final class ActionJPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final JLabel srcTerritoryLabel = new JLabel("Prova src");
    private final JLabel srcTerritoryDesc = new JLabel("Territorio partenza");
    private final JLabel dstTerritoryLabel = new JLabel("Prova dst");
    private final JLabel dstTerritoryDesc = new JLabel("Territorio destinazione");
    private final JButton performeActionButton = new JButton("Esegui azione");
    private final JButton changeStateButton = new JButton("Cambia Stato");

    /**
     * constructor.
     * 
     * @param controller the game controller
     */
    public ActionJPanel(final Controller controller) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(statePanel());
        add(changeStateButton);
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
        panel.add(labelButton(), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 1;
        gbc.insets = new Insets(5, 0, 5, 5);
        panel.add(performeActionButton, gbc);
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

        return panel;
    }

    /**
     * Update all the text inside this panel.
     * 
     * @param srcTerritory       the actual source territory string that has been
     *                           selected
     * @param srcTerritoryLabel  the string that says the kind of source territory
     * @param dstTerritory       the actual destination territory string that has
     *                           been
     *                           selected
     * @param dstTerritoryLabel  the string that says the kind of destination
     *                           territory
     * @param performeButtonText the button action string
     * @param changeStateString  the string for the changestate button
     */
    public void updateAllText(String srcTerritory, String srcTerritoryLabel, String dstTerritory,
            String dstTerritoryLabel, String performeButtonText, String changeStateString) {
        this.srcTerritoryLabel.setText(srcTerritory);
        this.srcTerritoryDesc.setText(srcTerritoryLabel);
        this.dstTerritoryLabel.setText(dstTerritoryLabel);
        this.dstTerritoryDesc.setText(dstTerritoryLabel);
        this.performeActionButton.setText(performeButtonText);
        this.changeStateButton.setText(changeStateString);
    }
}