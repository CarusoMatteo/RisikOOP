package it.unibo.risikoop.view.implementations.scenes.mapscene;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.risikoop.controller.interfaces.Controller;
import it.unibo.risikoop.model.implementations.Color;

/**
 * Panel to dislpay the Map Ghraph in the MapScene.
 */
public final class MapJPanel extends JPanel implements ViewerListener {
    private static final long serialVersionUID = 1L;
    private static final String COMMON_STYLE_SHEET = """
            node {
                size: 30px;
                text-size: 15px;
                text-color: black;
                text-alignment: under;
                size-mode: dyn-size;
            }
            """;

    private final transient Controller controller;
    private final ActionJPanel ap;
    private final transient Graph graph;
    private final transient SwingViewer viewer;
    private final View view;
    private final ViewPanel panel;
    private Boolean loop = true;

    /**
     * Constructor for MapJPanel.
     * 
     * @param actonPanel The ActionPanel that will be used when we click a
     *                   territory for updating the state labels
     * @param controller The controller to retrieve graph data and unit count.
     */
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public MapJPanel(final ActionJPanel actonPanel, final Controller controller) {
        this.ap = actonPanel;
        this.controller = controller;
        setLayout(new BorderLayout());

        this.graph = this.controller.getDataRetrieveController().getActualMap();
        this.graph.nodes().forEach(
                node -> node.setAttribute("ui.label", node.getId() + " - " + getUnits(node.getId()) + " units"));

        graph.setAttribute("ui.stylesheet", COMMON_STYLE_SHEET);
        this.viewer = new SwingViewer(graph, SwingViewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        this.viewer.enableAutoLayout();
        this.view = viewer.addDefaultView(false);
        this.panel = (ViewPanel) this.view;

        this.add(panel, BorderLayout.CENTER);

        attachPipe();
        assignNewNodesColor();

    }

    /**
     * method to change the color of each node based on the territory's owner.
     */
    public void assignNewNodesColor() {
        for (final Node n : graph.nodes().toList()) {
            final Color territoryColor = controller.getDataRetrieveController().getTerritoryFromName(n.getId()).get()
                    .getOwner().getColor();
            final String cssString = "fill-color: rgb(" + territoryColor.r() + "," + territoryColor.g() + ","
                    + territoryColor.b() + ");";
            n.setAttribute("ui.style", cssString);
        }
    }

    private void attachPipe() {
        final ViewerPipe pipe = viewer.newViewerPipe();
        pipe.addViewerListener(this);
        pipe.addSink(graph);

        new Thread(() -> {
            while (loop) {
                pipe.pump();
                sleep(100);
            }
        }).start();
    }

    // Thread.sleep is used to control the loop timing,
    // we have no logger in this project.
    @SuppressWarnings({ "PMD.EmptyCatchBlock", "checkstyle:EmptyCatchBlock" })
    private void sleep(final int millis) {
        try {
            Thread.sleep(millis);
        } catch (final InterruptedException e) {
            // Thread.sleep is used to control the loop timing,
            // we have no logger in this project.
        }
    }

    /**
     * Gets the number of units in a territory based on its name.
     * 
     * @param territoryName The name of the territory.
     * @return A string representing the number of units in the territory.
     */
    private String getUnits(final String territoryName) {
        return String.valueOf(controller
                .getDataRetrieveController()
                .getTerritoryUnitsFromName(territoryName)
                .orElse(-1));
    }

    @Override
    public void viewClosed(final String viewName) {
        loop = false;
    }

    @Override
    public void buttonPushed(final String id) {
        if (controller.getGamePhaseController()
                .selectTerritory(controller.getDataRetrieveController().getTerritoryFromName(id).get())) {
            ap.clickTerritory(id);
            ap.updateStateLabel();
            assignNewNodesColor();
        }

    }

    @Override
    public void buttonReleased(final String id) {

    }

    @Override
    public void mouseOver(final String id) {

    }

    @Override
    public void mouseLeft(final String id) {
    }

    /**
     * change the displayed units for the specific territory.
     * 
     * @param territoryName
     * @param units
     */
    public void changeUnitsOfTerritory(final String territoryName, final int units) {
        this.graph.nodes().filter(i -> i.getId().equals(territoryName)).forEach(i -> i.setAttribute("ui.label",
                i.getId() + " - " + getUnits(i.getId()) + " units"));
    }
}
