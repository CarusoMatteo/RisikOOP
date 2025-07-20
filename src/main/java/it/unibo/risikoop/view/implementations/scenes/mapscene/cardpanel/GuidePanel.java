package it.unibo.risikoop.view.implementations.scenes.mapscene.cardpanel;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import it.unibo.risikoop.controller.interfaces.Controller;
import it.unibo.risikoop.model.interfaces.Continent;

/**
 * A class that is displayed for showing all the continents and its territories
 * and all the avalaible combos.
 */
public final class GuidePanel extends JPanel {

    public GuidePanel(final Controller controller) {
        controller.getDataRetrieveController().getContinents().forEach(c -> {
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
            add(new JLabel(c.getName()));
            add(new JScrollPane(new ContinentDisplaying(c)));
        });
    }

    private class ContinentDisplaying extends JPanel {
        private ContinentDisplaying(final Continent continent) {
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
            add(new JLabel("Continent rewards units: " + continent.getUnitReward()));
            continent.getTerritories().forEach(t -> {
                add(new JLabel("Territory: " + t.getName()));
            });
        }
    }
}
