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
    private ClickingTerritoryMovementState internalViewState = ClickingTerritoryMovementState.SELECTING_ATTACKER;
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

        add(movmentPanel());
        add(stateLabel);
        add(changeStateButton);
        changeStateButton.addActionListener(i -> {
            changeStateButtonBehavior();
        });
        changeStateButtonBehavior();
    }

    private void switchInternalState() {
        switch (internalViewState) {
            case SELECTING_DEFENDER -> {
                internalViewState = ClickingTerritoryMovementState.SELECTING_UNITS;
            }
            case SELECTING_ATTACKER -> {
                internalViewState = ClickingTerritoryMovementState.SELECTING_DEFENDER;
            }
            case SELECTING_UNITS -> {
                internalViewState = ClickingTerritoryMovementState.SELECTING_ATTACKER;
            }

            default -> {
            }
        }
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
        gbc.insets = new Insets(5, 0, 5, 5);
        panel.add(performeActionButton, gbc);
        performeActionButton.addActionListener(i -> {
            performeActionButtonBehavior();
        });
        return panel;
    }

    private void performeActionButtonBehavior() {
        if (inMovementBasedState()) {
            switchInternalState();
            if (internalViewState == ClickingTerritoryMovementState.SELECTING_UNITS)
                try {
                    Integer units = Integer.valueOf(unitsTextField.getText());
                    controller.getGamePhaseController().setUnitsToUse(units);

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Errore, inserire un numero di cifre",
                            "Errore valore non numerico", JOptionPane.ERROR_MESSAGE);
                    return;
                }
        }
        controller.getGamePhaseController().performAction();
        updateStateLabel();
        this.setButtons();
        if (controller.getGamePhaseController().getCurrentPhase().isComplete()) {
            changeStateButtonBehavior();
        }

    }

    private boolean inMovementBasedState() {
        return inAttackState() || inMovementState();
    }

    private boolean inAttackState() {
        return controller.getGamePhaseController().getStateDescription().equals("Fase di gestione attacchi");
    }

    private boolean inMovementState() {
        return controller.getGamePhaseController().getStateDescription().equals("Fase di gestione spostamenti");
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

    public void clickTerritory(final String territoryName) {
        if (inMovementBasedState()) {
            if (internalViewState == ClickingTerritoryMovementState.SELECTING_ATTACKER) {
                if (controller.isOwned(territoryName, controller.getDataRetrieveController().getCurrentPlayerName())) {
                    srcTerritoryLabel.setText(territoryName);
                }
            } else if (internalViewState == ClickingTerritoryMovementState.SELECTING_DEFENDER) {
                if (!controller.isOwned(territoryName, controller.getDataRetrieveController().getCurrentPlayerName())) {
                    dstTerritoryLabel.setText(territoryName);
                }

            }
        }
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
                !controller.getGamePhaseController().getStateDescription()
                        .equals("Fase di gestione combo")
                        || !(controller.getGamePhaseController().getCurrentPhase().isComplete()));
    }

    /**
     * A enum that is used for updating the
     */
    private enum ClickingTerritoryMovementState {
        SELECTING_DEFENDER,
        SELECTING_ATTACKER,
        SELECTING_UNITS;
    }
}