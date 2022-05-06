package FindI;

import java.util.Vector;

public class Peak {
    private Vector<Double> x;
    private Vector<Double> y;
    int beginPosition, endPosition, maxPoint;

    public Peak(int beginning, int end, int max, Vector<Double> x, Vector<Double> y) {
        beginPosition = beginning;
        endPosition = end;
        maxPoint = max;
        this.x = x;
        this.y = y;
    }
}
