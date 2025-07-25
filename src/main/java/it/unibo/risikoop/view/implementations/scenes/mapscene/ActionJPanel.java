package it.unibo.risikoop.view.implementations.scenes.mapscene;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.unibo.risikoop.controller.interfaces.Controller;
import it.unibo.risikoop.controller.interfaces.GamePhaseController;
import it.unibo.risikoop.model.interfaces.gamephase.InternalState;

/**
 * Panel for the Action Buttons in the MapScene.
 */
// commit before merge
public final class ActionJPanel extends JPanel {
    private static final int INSECT_COSTANT = 5;
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
    private final transient Controller controller;

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

        add(movmentPanel());
        add(stateLabel);
        add(changeStateButton);
        changeStateButton.addActionListener(i -> {
            changeStateButtonBehavior();
        });
        changeStateButtonBehavior();

    }

    private void changeStateButtonBehavior() {
        this.changeState();
        updateStateLabel();
        this.setButtons();
        statePanel.setVisible(
                inMovementBasedState());
    }

    private JPanel movmentPanel() {
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
        gbc.insets = new Insets(INSECT_COSTANT, 0, INSECT_COSTANT, INSECT_COSTANT);
        panel.add(performeActionButton, gbc);
        performeActionButton.addActionListener(i -> {
            performeActionButtonBehavior();
        });
        return panel;
    }

    private void performeActionButtonBehavior() {
        if (inMovementBasedState()
                && controller.getGamePhaseController().getInternalState().isPresent()
                && controller.getGamePhaseController().getInternalState()
                        .get() == InternalState.SELECT_UNITS_QUANTITY) {
            try {
                final Integer units = Integer.valueOf(unitsTextField.getText());
                controller.getGamePhaseController().setUnitsToUse(units);
            } catch (final NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Errore, inserire un numero di cifre",
                        "Errore valore non numerico", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        controller.getGamePhaseController().performAction();
        updateStateLabel();
        this.setButtons();
        if (controller.getGamePhaseController().getCurrentPhase().isComplete()) {
            if (!inAttackState()) {
                changeStateButtonBehavior();
            }
            controller.getGamePhaseController().showAttackResults().ifPresent(m -> {
                JOptionPane.showMessageDialog(this, m);
            });
        }

    }

    private boolean inMovementBasedState() {
        return inAttackState() || inMovementState();
    }

    private boolean inAttackState() {
        return controller.getGamePhaseController().getPhaseKey() == GamePhaseController.PhaseKey.ATTACK;
    }

    private boolean inMovementState() {
        return controller.getGamePhaseController().getPhaseKey() == GamePhaseController.PhaseKey.MOVEMENT;
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
     * it is called whenever you click on some territory.
     * 
     * @param territoryName
     */
    public void clickTerritory(final String territoryName) {
        controller.getGamePhaseController()
                .getInternalState()
                .ifPresent(i -> {
                    switch (i) {
                        case SELECT_SRC -> this.srcTerritoryLabel.setText(territoryName);
                        case SELECT_DST -> this.dstTerritoryLabel.setText(territoryName);
                        default -> throw new IllegalStateException("Unexpected value: " + i);
                    }
                });
    }

    /**
     * update the text inside the statae label.
     * 
     */
    public void updateStateLabel() {
        this.stateLabel.setText(controller.getGamePhaseController().getStateDescription() + " "
                + controller.getGamePhaseController().getInnerStatePhaseDescription());
    }

    private void changeState() {
        controller.getGamePhaseController().nextPhase();
    }

    private void setButtons() {
        changeStateButton.setVisible(
                controller.getGamePhaseController().getCurrentPhase().isComplete());
        performeActionButton.setVisible(
                controller.getGamePhaseController().getPhaseKey() != GamePhaseController.PhaseKey.COMBO
                        || !controller.getGamePhaseController().getCurrentPhase().isComplete());
    }
}
