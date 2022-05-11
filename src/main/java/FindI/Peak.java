package FindI;

import java.util.Vector;

public class Peak {
    private Vector<Double> x;
    private Vector<Double> y;
    public boolean maxPoint;
    public int indexBeginning;
    public int indexEnd;
    public double minOrMaxElem;

    public Peak(int indexEnd, int indexBeginning, double minOrMaxElem, boolean trendDown, Vector<Double> x, Vector<Double> y) {
        //beginPosition = beginning;
        this.indexBeginning = indexBeginning;
        this.indexEnd = indexEnd;
        maxPoint = trendDown;
        this.minOrMaxElem = minOrMaxElem;
        this.x = x;
        this.y = y;
    }

    private void findMidPosition() {

    }

    private void findDepthTillLevel() {

    }
}
