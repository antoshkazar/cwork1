package FindI;

import java.util.Vector;

public class Peak {
    private Vector<Double> x;
    private Vector<Double> y;
    public boolean trendDown;
    public int indexBeginning;
    public int indexEnd, indexExtremum;
    public double extremum, depth, square;

    public Peak(int indexEnd, int indexBeginning, int indexExtremum,
                double extremum, boolean trendDown, Vector<Double> x, Vector<Double> y) {
        //beginPosition = beginning;
        this.indexBeginning = indexBeginning;
        this.indexExtremum = indexExtremum;
        this.indexEnd = indexEnd;
        this.trendDown = trendDown;
        this.extremum = extremum;
        this.x = x;
        this.y = y;
        findDepth();
    }

    boolean findDepth() {
        double x1 = y.get(indexBeginning);
        double y1 = x.get(indexBeginning);
        double x2 = y.get(indexEnd);
        double y2 = x.get(indexEnd);
        double x3 = y.get(indexExtremum);
        double y3 = x.get(indexExtremum);
        double x4 = y.get(indexExtremum);
        double y4 = x.get(indexExtremum) + 0.1;
        double n;
        if (y2 - y1 != 0) {  // a(y)
            double q = (x2 - x1) / (y1 - y2);
            double sn = (x3 - x4) + (y3 - y4) * q;
            if (sn == 0) {
                return false;
            }
            double fn = (x3 - x1) + (y3 - y1) * q;
            n = fn / sn;
        } else {
            if ((y3 - y4) == 0) {
                return false;
            }
            n = (y3 - y1) / (y3 - y4);
        }
        double fx = x3 + (x4 - x3) * n;
        double fy = y3 + (y4 - y3) * n;
        depth = Math.abs(fy - y3);
        square = 0.5 * ((x.get(indexEnd) - x.get(indexBeginning)) * (y.get(indexExtremum) -
                y.get(indexBeginning)) - (y.get(indexEnd) - y.get(indexBeginning)) * (
                x.get(indexExtremum) - x.get(indexBeginning)));
        return true;
    }

}
