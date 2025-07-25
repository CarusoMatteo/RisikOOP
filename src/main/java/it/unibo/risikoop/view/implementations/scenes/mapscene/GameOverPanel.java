package it.unibo.risikoop.view.implementations.scenes.mapscene;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.unibo.risikoop.controller.interfaces.Controller;

/**
 * A class that is for the jpanel of when showing the winner.
 */
public final class GameOverPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    /**
     * the constructor.
     * 
     * @param controller the controller for the application
     */
    public GameOverPanel(final Controller controller) {
        setLayout(new BorderLayout());
        add(new JLabel("Winner: " + controller.getDataRetrieveController().getCurrentPlayerName()),
                BorderLayout.CENTER);
        add(new WinnerButtonsPanel(controller), BorderLayout.PAGE_END);
    }

    private final class WinnerButtonsPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        private final JButton playAgainButton = new JButton("Play Again");
        private final JButton closeButton = new JButton("Close");

        private WinnerButtonsPanel(final Controller controller) {
            setLayout(new BorderLayout());
            add(playAgainButton, BorderLayout.EAST);
            add(closeButton, BorderLayout.WEST);
            playAgainButton.addActionListener(i -> controller.start());
            closeButton.addActionListener(new ActionListener() {
                @Override
                @SuppressWarnings("DM_EXIT")
                public void actionPerformed(final ActionEvent i) {
                    System.exit(0);
                }
            });
        }
    }
}
