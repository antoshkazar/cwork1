package FindI;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class FindIntensity {
    private final Vector<Double> x;
    private final Vector<Double> y;
    public List<Peak> peaks = new ArrayList<>();
    Vector<Double> okExtremumsX = new Vector<>();
    Vector<Double> okExtremumsY = new Vector<>();

    public Vector<Double> getOkExtremumsX() {
        return okExtremumsX;
    }

    public List<Peak> getPeaks() {
        return peaks;
    }

    public Vector<Double> getOkExtremumsY() {
        return okExtremumsY;
    }

    public FindIntensity(Vector<Double> x, Vector<Double> y) {
        this.x = x;
        this.y = y;
        findPeaks2();
        //findPeaks();
    }

    void findPeaks2() {
        int possible_peaks = 0;
        Vector<Double> extremumsX = new Vector<>();
        Vector<Double> extremumsY = new Vector<>();
        Vector<Integer> positions = new Vector<>();
        boolean trendDown;
        try {
            trendDown = x.get(0) < x.get(1);
            for (int i = 0; i < x.size() - 1; i++) {
                if (x.get(i + 1) < x.get(i) && !trendDown) {
                    trendDown = true;
                    extremumsX.add(x.get(i));
                    extremumsY.add(y.get(i));
                    positions.add(i);
                }
                if (x.get(i + 1) > x.get(i) && trendDown) {
                    trendDown = false;
                    extremumsX.add(x.get(i));
                    extremumsY.add(y.get(i));
                    positions.add(i);
                }
            }
            // System.out.println(extremumsX.size());
            okExtremumsX.clear();
            okExtremumsY.clear();
            okExtremumsX.add(x.get(0));
            okExtremumsY.add(y.get(0));
            Vector<Integer> okPositions = new Vector<>();
            okPositions.add(0);
            for (int i = 0; i < extremumsX.size() - 1; i++) {
                //&& (
                //                        extremumsY.get(i + 1) - extremumsY.get(i) > 35 && i + 1 != extremumsY.size())
                if (((1 - Math.abs(extremumsX.get(i) / extremumsX.get(i + 1))) * 100 >= Params.PERCENT)) {
                    okExtremumsY.add(extremumsY.get(i + 1));
                    okExtremumsX.add(extremumsX.get(i + 1));
                    okPositions.add(positions.get(i + 1));
                }
            }
            extremumsX.clear();
            extremumsY.clear();
            positions.clear();
            positions.add(0);
            for (int i = 0; i < okExtremumsX.size() - 1; i++) {
                double minElem = Double.MAX_VALUE, maxElem = Double.MIN_VALUE;
                int posMin = 0, posMax = 0;
                trendDown = x.get(okPositions.get(i)) >= x.get(okPositions.get(i) + 1);
                //System.out.println( x.get(okPositions.get(i)).toString() + " " +   x.get((okPositions.get(i) +1)));
               // System.out.println(trendDown);
                for (int j = okPositions.get(i) + 1; j < okPositions.get(i + 1); j++) {
                    if (trendDown) {
                        if (x.get(j) < minElem) {
                            minElem = x.get(j);
                            posMin = j;
                        }
                    } else {
                        if (x.get(j) > maxElem) {
                            maxElem = x.get(j);
                            posMax = j;
                        }
                    }
                }
                if (trendDown) {
                    System.out.println(okPositions.get(i) + " min " + minElem + " pos " + posMin);
                    if (okExtremumsX.get(i) - minElem > 0.005 && okExtremumsY.get(i + 1) - okExtremumsY.get(i) < 1000) {
                        // System.out.println("min " + y.get(positions.get(i)));
                        positions.add(okPositions.get(i));
                        peaks.add(new Peak(okPositions.get(i + 1), okPositions.get(i), posMin, minElem, true, x, y));
                    }
                } else {
                    // System.out.println(okExtremumsX.get(i) + " max " + maxElem);
                    if (maxElem - okExtremumsX.get(i) > 0.005 && okExtremumsY.get(i + 1) - okExtremumsY.get(i) < 1000) {
                        positions.add(okPositions.get(i));
                        peaks.add(new Peak(okPositions.get(i + 1), okPositions.get(i), posMax, maxElem, false, x, y));
                    }
                }
            }
            // System.out.println(positions.size());
            for (var peak : peaks) {
                System.out.println(y.get(peak.indexBeginning) + " " + peak.depth);
                // System.out.println(y.get(peak.indexBeginning) + " " + y.get(peak.indexEnd));
            }
            /*
            System.out.println(okPositions.size());
            System.out.println(okExtremumsX.size());
            for (Double extremum : okExtremumsY) {
                System.out.println(extremum);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
