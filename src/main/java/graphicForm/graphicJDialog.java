package graphicForm;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

public class graphicJDialog extends JDialog {
    Vector<Double> x = new Vector<>();
    Vector<Double> y = new Vector<>();

    public graphicJDialog(Vector<Double> x, Vector<Double> y) {
        //this.setSize(500, 500);
        this.setResizable(false);
        this.setLocation(600, 600);
        setTitle("Graph");
        // this.setVisible(true);
        this.x = x;
        this.y = y;
    }
/*
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        //g2D.drawImage(image, 0, 0, null);
        g2D.setPaint(Color.blue);
        g2D.setStroke(new BasicStroke(5));
        drawDiagram(g, x, y, 1, 500, Color.blue);
    }*/
/*
    public class ScatterPlotExample extends JFrame {
        private static final long serialVersionUID = 6294689542092367723L;

        public ScatterPlotExample(String title) {
            super(title);

            // Create dataset
            XYDataset dataset = createDataset();

            // Create chart
            JFreeChart chart = ChartFactory.createScatterPlot(
                    "Boys VS Girls weight comparison chart",
                    "X-Axis", "Y-Axis", dataset);


            //Changes background color
            XYPlot plot = (XYPlot) chart.getPlot();
            plot.setBackgroundPaint(new Color(255, 228, 196));


            // Create Panel
            ChartPanel panel = new ChartPanel(chart);
            setContentPane(panel);
        }

        private XYDataset createDataset() {
            XYSeriesCollection dataset = new XYSeriesCollection();

            //Boys (Age,weight) series
            XYSeries series1 = new XYSeries("Boys");
            series1.add(1, 72.9);
            series1.add(2, 81.6);
            series1.add(3, 88.9);
            series1.add(4, 96);
            series1.add(5, 102.1);
            series1.add(6, 108.5);
            series1.add(7, 113.9);
            series1.add(8, 119.3);
            series1.add(9, 123.8);
            series1.add(10, 124.4);

            dataset.addSeries(series1);

            //Girls (Age,weight) series
            XYSeries series2 = new XYSeries("Girls");
            series2.add(1, 72.5);
            series2.add(2, 80.1);
            series2.add(3, 87.2);
            series2.add(4, 94.5);
            series2.add(5, 101.4);
            series2.add(6, 107.4);
            series2.add(7, 112.8);
            series2.add(8, 118.2);
            series2.add(9, 122.9);
            series2.add(10, 123.4);

            dataset.addSeries(series2);

            return dataset;
        }
    }*/


    public void drawDiagram(Graphics g, Vector<Double> x, Vector<Double> y, double scaling, int height, Color color) {
        double lastX = 0, lastY = 0;

        g.setColor(color);
        var g2D = (Graphics2D) g;
        for (int i = 0; i < 10; i++) {
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2D.draw(new Line2D.Double(10.5, 10.5, 20.5, 20.5));
            //Rectangle2D.Double rect = new Rectangle2D.Double(lastX * scaling, lastY * height, 10, 10);
            //g2D.setColor(Color.BLUE);
            //g2D.draw(rect);
            // g.drawLine((int) (lastX * scaling), height - (int) (lastY * scaling),
            //   height - (int) (x.get(i) * scaling), (int) (y.get(i) * scaling));
            lastX = x.get(i) * 1000000;
            lastY = y.get(i) * 1000000;
        }
    }
}