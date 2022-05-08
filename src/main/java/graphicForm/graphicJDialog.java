package graphicForm;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ui.RectangleInsets;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class graphicJDialog extends JDialog {
    Vector<Double> x;
    Vector<Double> y;
    String filename;

    public graphicJDialog(Vector<Double> x, Vector<Double> y, String filename) {
        this.x = x;
        this.y = y;
        this.filename = filename;
        this.setLocation(100, 100);
        setTitle("Graph");
        try {
            drawGraph d = new drawGraph(x, y, filename);
            var dataset = d.createDataset();
            if (dataset == null) {
                JOptionPane.showMessageDialog(this, "Не удалось построить график!");
                this.dispose();
                return;
            }
            JFreeChart chart = d.createChart(dataset);
            if (chart == null) {
                JOptionPane.showMessageDialog(this, "Не удалось построить график!");
                this.dispose();
                return;
            }
            chart.setPadding(new RectangleInsets(4, 8, 2, 2));
            ChartPanel panel = new ChartPanel(chart);
            panel.setFillZoomRectangle(true);
            panel.setMouseWheelEnabled(true);
            panel.setPreferredSize(new Dimension(800, 600));
            add(panel);
            this.pack();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}