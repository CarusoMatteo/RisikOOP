package it.unibo.risikoop.view.implementations.scenes.mapscene;

import java.awt.BorderLayout;
import java.util.EnumSet;

import javax.swing.JPanel;

import org.graphstream.graph.Graph;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;
import org.graphstream.ui.view.util.InteractiveElement;

import it.unibo.risikoop.controller.interfaces.Controller;

/**
 * Panel to dislpay the Map Ghraph in the MapScene.
 */
public final class MapJPanel extends JPanel implements ViewerListener {
    private static final long serialVersionUID = 1L;
    private static final String COMMON_STYLE_SHEET = """
            node {
                fill-color: orange;
                size: 30px;
                text-size: 15px;
                text-color: black;
                text-alignment: under;
                size-mode: dyn-size;
            }
            """;

    private final transient Controller controller;
    private final Graph graph;
    private final SwingViewer viewer;
    private final View view;
    private final ViewPanel panel;
    private Boolean loop = true;

    /**
     * Constructor for MapJPanel.
     * 
     * @param controller The controller to retrieve graph data and unit count.
     */
    public MapJPanel(final Controller controller) {
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

    private void sleep(final int millis) {
        try {
            Thread.sleep(millis);
        } catch (final InterruptedException e) {
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
        System.out.println("node=" + id);
        view.allGraphicElementsIn(EnumSet.of(InteractiveElement.NODE), -Double.MAX_VALUE, -Double.MAX_VALUE,
                Double.MAX_VALUE, Double.MAX_VALUE).forEach(i -> {
                    System.out.println(i.getSelectorType());
                    System.out.println(i.getId());
                    System.out.println(i.getX() + " - " + i.getY());
                });
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
}
