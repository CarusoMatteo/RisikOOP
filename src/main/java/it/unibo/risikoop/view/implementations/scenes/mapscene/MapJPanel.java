package it.unibo.risikoop.view.implementations.scenes.mapscene;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EnumSet;

import javax.swing.JPanel;
import org.graphstream.graph.Graph;
import org.graphstream.ui.graphicGraph.GraphicElement;
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

        this.panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                GraphicElement element = view.findGraphicElementAt(
                        EnumSet.of(InteractiveElement.NODE),
                        me.getX(),
                        me.getY());
                if (element != null) {
                    System.out.println("element clicked!!!");
                } else {
                    System.out.println("element is null");
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

    private void attachPipe() {
        ViewerPipe pipe = viewer.newViewerPipe();
        pipe.addViewerListener(this);
        pipe.addSink(graph);

        new Thread(() -> {
            while (loop) {
                pipe.pump();
                sleep(100);
            }
        }).start();
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
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
    public void viewClosed(String viewName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'viewClosed'");
    }

    @Override
    public void buttonPushed(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buttonPushed'");
    }

    @Override
    public void buttonReleased(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buttonReleased'");
    }

    @Override
    public void mouseOver(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseOver'");
    }

    @Override
    public void mouseLeft(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseLeft'");
    }
}
