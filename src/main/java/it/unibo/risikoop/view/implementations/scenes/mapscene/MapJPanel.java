package it.unibo.risikoop.view.implementations.scenes.mapscene;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.graphstream.graph.Graph;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.ViewPanel;

/**
 * Panel to dislpay the Map Ghraph in the MapScene.
 */
public final class MapJPanel extends JPanel {
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

    /**
     * Constructor for MapJPanel.
     * 
     * @param graph The graph representing the actual map.
     */
    public MapJPanel(final Graph graph) {
        setLayout(new BorderLayout());

        graph.nodes().forEach(
                node -> node.setAttribute("ui.label", node.getId() + " - " + getUnits(node.getId()) + " units"));

        graph.setAttribute("ui.stylesheet", COMMON_STYLE_SHEET);
        final SwingViewer graphViewer = new SwingViewer(graph, SwingViewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        graphViewer.enableAutoLayout();
        final ViewPanel graphView = (ViewPanel) graphViewer.addDefaultView(false);

        this.add(graphView, BorderLayout.CENTER);
    }

    /**
     * Gets the number of units in a territory based on its name.
     * 
     * @param territoryName The name of the territory.
     * @return A string representing the number of units in the territory.
     */
    private String getUnits(final String territoryName) {
        // TODO Implement logic to get the number of units.
        return String.valueOf(territoryName.length());
    }

}
