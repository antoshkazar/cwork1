package FindI;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class findI {
    private Vector<Double> x;
    private Vector<Double> y;
    public List<Peak> peaks = new ArrayList<>();
    Vector<Double> okextremumsX = new Vector<>();
    Vector<Double> okextremumsY = new Vector<>();

    public Vector<Double> getOkextremumsX() {
        return okextremumsX;
    }

    public Vector<Double> getOkextremumsY() {
        return okextremumsY;
    }

    public findI(Vector<Double> x, Vector<Double> y) {
        this.x = x;
        this.y = y;
        findPeaks2();
        //findPeaks();
    }

    void findPeaks2() {
        int possible_peaks = 0;
        Vector<Double> extremumsX = new Vector<>();
        Vector<Double> extremumsY = new Vector<>();
        boolean trendDown;
        try {
            trendDown = x.get(0) >= x.get(1);
            for (int i = 1; i < x.size() - 1; i++) {
                if (x.get(i + 1) < x.get(i) && !trendDown) {
                    trendDown = true;
                    extremumsX.add(x.get(i));
                    extremumsY.add(y.get(i));
                }
                if (x.get(i + 1) > x.get(i) && trendDown) {
                    trendDown = false;
                    extremumsX.add(x.get(i));
                    extremumsY.add(y.get(i));
                }
            }
            System.out.println(extremumsX.size());
            okextremumsX.add(x.get(0));
            okextremumsY.add(y.get(0));
            for (int i = 0; i < extremumsX.size() - 1; i++) {
                if (((1 - Math.abs(extremumsX.get(i) / extremumsX.get(i + 1))) * 100 >= Params.PERCENT)) {
                    okextremumsY.add(extremumsY.get(i + 1));
                    okextremumsX.add(extremumsX.get(i + 1));
                }
            }
            System.out.println(okextremumsX.size());
            for (Double extremum : okextremumsX) {
                System.out.println(extremum);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void findPeaks() {
        int possible_peaks = 0;
        try {
            int oldFreq = Params.FREQUENCY;
            double depth = 0;
            boolean peak_started = false;
            for (int i = 0; i < x.size() - Params.FREQUENCY; i += Params.FREQUENCY) {        //TODO Может не 70?
                if ((1 - Math.abs(x.get(i) / x.get(i + Params.FREQUENCY - 1))) * 100 < Params.PERCENT) {
                    peak_started = false;
                    if (depth != 0) {
                        System.out.println("bottom " + i + ' ' + y.get(i));
                        Params.FREQUENCY = oldFreq;
                        depth = 0;
                    }
                } else {
                    //System.out.println("percent " + (1 - Math.abs(x.get(i) / x.get(i + Params.FREQUENCY - 1))) * 100);
                    if (!peak_started) {
                        peak_started = true;
                        System.out.println(i);
                        System.out.println(y.get(i));
                        Params.FREQUENCY = Params.FREQUENCY / 2;
                        possible_peaks++;
                    }
                    depth += x.get(i + 69) - x.get(i);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(possible_peaks);
    }

}
