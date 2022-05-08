package graphicForm;

import java.awt.*;
import java.util.Vector;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class drawGraph {
    Vector<Double> x;
    Vector<Double> y;
    String filename;

    public drawGraph(Vector<Double> x, Vector<Double> y, String filename) {
        this.x = x;
        this.y = y;
        this.filename = filename;
    }

    public XYDataset createDataset() {
        try {
            XYSeries xySeries = new XYSeries("ИК-спектр");
            for (int i = 0; i < x.size(); i++) {
                xySeries.add(y.get(i), x.get(i));
            }
            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(xySeries);
            return dataset;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart;
        try {
            chart = ChartFactory.createXYLineChart(
                    filename,
                    "v, см^-1",
                    "I,отн.ед.",
                    dataset
            );
            chart.setBackgroundPaint(Color.white);
            XYPlot plot = (XYPlot) chart.getPlot();
            plot.setBackgroundPaint(Color.WHITE);
            plot.setDomainGridlinePaint(Color.gray);
            plot.setRangeGridlinePaint(Color.GRAY);
            plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
            plot.setDomainCrosshairVisible(true);
            plot.setRangeCrosshairVisible(true);
            return chart;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}