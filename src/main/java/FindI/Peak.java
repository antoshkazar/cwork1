package FindI;

import java.util.Vector;

public class Peak {
    private Vector<Double> x;
    private Vector<Double> y;
    public boolean trendDown;
    public int indexBeginning;
    public int indexEnd, indexExtremum;
    public double extremum, depth;

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

    // точка пересечения

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
            }  // c(x) + c(y)*q
            double fn = (x3 - x1) + (y3 - y1) * q;   // b(x) + b(y)*q
            n = fn / sn;
        } else {
            if ((y3 - y4) == 0) {
                return false;
            }  // b(y)
            n = (y3 - y1) / (y3 - y4);   // c(y)/b(y)
        }
        double fx = x3 + (x4 - x3) * n;  // x3 + (-b(x))*n
        double fy = y3 + (y4 - y3) * n;  // y3 +(-b(y))*n
        depth = Math.abs(fy - y3);
        return true;
    }

}
