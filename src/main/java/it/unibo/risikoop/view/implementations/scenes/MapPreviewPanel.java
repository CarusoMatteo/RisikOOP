package it.unibo.risikoop.view.implementations.scenes;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.ViewPanel;

import it.unibo.risikoop.controller.interfaces.Controller;

/**
 * shows the preview of the selected map before starting to play.
 */
public final class MapPreviewPanel extends JPanel {
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
    private JPanel panel;

    /**
     * 
     * @param controller the application controller
     */
    public MapPreviewPanel(final Controller controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        setVisible(true);
        add(new JLabel("preview"),BorderLayout.NORTH);
    }

    /**
     * called every time we select a new map.
     */
    public void updatePreview() {
        if (panel != null) {
            this.remove(panel);
        }
        var graph = this.controller.getDataRetrieveController().getActualMap();
        graph.nodes().forEach(
                node -> node.setAttribute("ui.label", node.getId()));

        graph.setAttribute("ui.stylesheet", COMMON_STYLE_SHEET);
        var viewer = new SwingViewer(graph, SwingViewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        viewer.enableAutoLayout();
        var view = viewer.addDefaultView(false);
        this.panel = (ViewPanel) view;
        add(this.panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
