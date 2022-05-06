package FindI;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class findIMax {
    private Vector<Double> x;
    private Vector<Double> y;
    public List<Peak> peaks = new ArrayList<>();

    public findIMax(Vector<Double> x, Vector<Double> y) {
        this.x = x;
        this.y = y;
        findPeaks();
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
                        System.out.println("bottom " + y.get(i));
                        Params.FREQUENCY = oldFreq;
                        depth = 0;
                    }
                } else {
                    if (!peak_started) {
                        peak_started = true;
                        System.out.println(y.get(i));
                        Params.FREQUENCY = Params.FREQUENCY - 10;
                        possible_peaks++;
                    }
                    depth += x.get(i + 69) - x.get(i);
                }
            }

        } catch (Exception e) {
            System.out.println(e.getStackTrace().toString());
        }
        System.out.println(possible_peaks);
    }

}
