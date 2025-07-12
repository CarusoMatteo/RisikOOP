package it.unibo.risikoop.view.implementations.scenes.mapscene;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel to display the Current Player in the MapScene.
 */
public final class CurrentPlayerJPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int FONT_SIZE = 20;
    private static final double RED_FACTOR = 0.299;
    private static final double GREEN_FACTOR = 0.587;
    private static final double BLUE_FACTOR = 0.114;
    private static final int LUMINANCE_THRESHOLD = 128;

    private JLabel playerNameLabel;

    public CurrentPlayerJPanel(final String firstPlayerName,
            final it.unibo.risikoop.model.implementations.Color firstPlayerColor) {
        this.setLayout(new GridLayout(1, 1));
        createLabel(firstPlayerName);
        updateCurrentPlayer(firstPlayerName, new Color(
                firstPlayerColor.r(),
                firstPlayerColor.g(),
                firstPlayerColor.b()));
    }

    private void createLabel(final String firstPlayerName) {
        this.playerNameLabel = new JLabel(firstPlayerName);
        this.add(this.playerNameLabel);
        this.playerNameLabel.setHorizontalAlignment(JLabel.CENTER);
        this.playerNameLabel.setVerticalAlignment(JLabel.CENTER);
    }

    public void updateCurrentPlayer(final String playerName, final Color playerColor) {
        this.setBackground(playerColor);
        this.playerNameLabel.setText("<html>" + playerName + "</html>");
        this.playerNameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE));
        this.playerNameLabel.setForeground(getContrastingColor(playerColor));

        this.playerNameLabel.setHorizontalAlignment(JLabel.CENTER);
        this.playerNameLabel.setVerticalAlignment(JLabel.CENTER);

    }

    /**
     * Returns black or white depending on the brightness of the background color.
     * <p>
     * This uses a standard formula for perceived brightness.
     * <p>
     * Source:
     * https://en.wikipedia.org/wiki/Relative_luminance#Relative_luminance_and_%22gamma_encoded%22_colorspaces
     * 
     * @param bg The background color.
     * @return A contrasting color (black or white).
     */
    private Color getContrastingColor(final Color bg) {
        return (RED_FACTOR * bg.getRed()
                + GREEN_FACTOR * bg.getGreen()
                + BLUE_FACTOR * bg.getBlue()) > LUMINANCE_THRESHOLD ? Color.BLACK : Color.WHITE;
    }
}
