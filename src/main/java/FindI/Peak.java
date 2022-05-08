package FindI;

import java.util.Vector;

public class Peak {
    private Vector<Double> x;
    private Vector<Double> y;
    int beginPosition, endPosition, maxPoint, indexBeginning, indexEnd;

    public Peak(int beginning, int indexBeginning, int max, Vector<Double> x, Vector<Double> y) {
        beginPosition = beginning;
        this.indexBeginning = indexBeginning;
        maxPoint = max;
        this.x = x;
        this.y = y;
    }
    private void findEndPosition(){
        for(int i = indexBeginning; i < x.size(); i++){
            while (x.get(i) != maxPoint){
                continue;
            }
        }
    }
}
