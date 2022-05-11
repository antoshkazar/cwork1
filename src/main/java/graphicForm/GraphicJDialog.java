package graphicForm;

import FindI.FindIntensity;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ui.RectangleInsets;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class GraphicJDialog extends JDialog {
    Vector<Double> x;
    Vector<Double> y;
    String filename;

    public GraphicJDialog(Vector<Double> x, Vector<Double> y, String filename) {
        this.x = x;
        this.y = y;
        this.filename = filename;
        this.setLocation(100, 100);
        setTitle("Graph");
        graphic();
    }

    public void graphic() {
        try {
            FindIntensity f = new FindIntensity(x, y);
            DrawGraph d = new DrawGraph(x, y, filename, f.getOkExtremumsX(), f.getOkExtremumsY());
            var dataset = d.createDataset(f.getPeaks());
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
            panel.setPreferredSize(new Dimension(900, 700));
            add(panel);
            this.pack();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}