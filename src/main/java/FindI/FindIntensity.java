package FindI;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class FindIntensity {
    private final Vector<Double> x;
    private final Vector<Double> y;
    // Список пиков.
    public List<Peak> peaks = new ArrayList<>();
    Vector<Double> okExtremumsX = new Vector<>();
    Vector<Double> okExtremumsY = new Vector<>();

    public Vector<Double> getOkExtremumsX() {
        return okExtremumsX;
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
        Vector<Double> extremumsX = new Vector<>();
        Vector<Double> extremumsY = new Vector<>();
        Vector<Integer> positions = new Vector<>();
        // Сначала находим все экстремумы.
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
            okExtremumsX.clear();
            okExtremumsY.clear();
            okExtremumsX.add(x.get(0));
            okExtremumsY.add(y.get(0));
            // Затем находим только те экстремумы, которые не являются шумом, и из позиции.
            Vector<Integer> okPositions = new Vector<>();
            okPositions.add(0);
            for (int i = 0; i < extremumsX.size() - 1; i++) {
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
            // Затем для каждого пика находим минимум/максимум, в зависимости от типа пика.
            for (int i = 0; i < okExtremumsX.size() - 1; i++) {
                double minElem = Double.MAX_VALUE, maxElem = Double.MIN_VALUE;
                int posMin = 0, posMax = 0;
                trendDown = x.get(okPositions.get(i)) >= x.get(okPositions.get(i) + 1);
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
                // Добавляем пики в список.
                if (trendDown) {
                    System.out.println(okPositions.get(i) + " min " + minElem + " pos " + posMin);
                    if (okExtremumsX.get(i) - minElem > 0.005 && okExtremumsY.get(i + 1) - okExtremumsY.get(i) < 1000) {
                        positions.add(okPositions.get(i));
                        peaks.add(new Peak(okPositions.get(i + 1), okPositions.get(i), posMin, minElem, true, x, y));
                    }
                } else {
                    if (maxElem - okExtremumsX.get(i) > 0.005 && okExtremumsY.get(i + 1) - okExtremumsY.get(i) < 1000) {
                        positions.add(okPositions.get(i));
                        peaks.add(new Peak(okPositions.get(i + 1), okPositions.get(i), posMax, maxElem, false, x, y));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
