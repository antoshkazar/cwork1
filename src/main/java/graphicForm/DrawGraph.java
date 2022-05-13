package graphicForm;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Vector;

import FindI.Params;
import FindI.Peak;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class DrawGraph {
    Vector<Double> x;
    Vector<Double> y;
    String filename;
    Vector<Double> okextremumsX;
    Vector<Double> okextremumsY;

    public DrawGraph(Vector<Double> x, Vector<Double> y, String filename, Vector<Double> okextremumsX, Vector<Double> okextremumsY) {
        this.x = x;
        this.y = y;
        this.filename = filename;
        this.okextremumsX = okextremumsX;
        this.okextremumsY = okextremumsY;
    }

    /**
     * Создаем датасет из исходных данных и списка пиков.
     * @param peaks список пиков
     * @return датасет
     */
    public XYDataset createDataset(List<Peak> peaks) {
        try {
            XYSeries xySeries = new XYSeries("ИК-спектр");
            for (int i = 0; i < x.size(); i++) {
                xySeries.add(y.get(i), x.get(i));
            }
            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(xySeries);
            for (int i = 0; i < peaks.size(); i++) {
                XYSeries xySeries1 = new XYSeries("Фон пика " + i);
                xySeries1.add(y.get(peaks.get(i).indexBeginning), x.get(peaks.get(i).indexBeginning));
                xySeries1.add(y.get(peaks.get(i).indexEnd), x.get(peaks.get(i).indexEnd));

                dataset.addSeries(xySeries1);
            }
            return dataset;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Создаем итоговый график
     * @param dataset датасет
     * @return график
     */
    public JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart;
        try {
            chart = ChartFactory.createXYLineChart(
                    filename,
                    "v, см^-1",
                    "I,отн.ед.",
                    dataset
            );
            chart.setBackgroundPaint(Params.FOREGROUND);
            XYPlot plot = (XYPlot) chart.getPlot();
            plot.setBackgroundPaint(Params.FOREGROUND);
            plot.setDomainGridlinePaint(Params.BACKGROUND);
            plot.setRangeGridlinePaint(Params.BACKGROUND);
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