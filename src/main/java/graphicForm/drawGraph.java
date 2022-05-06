package graphicForm;

import java.awt.*;

public class drawGraph {
    public void drawDiagram(Graphics g, double[] x, double[] y, double scaling, int height, Color color) {
        int lastX = 0, lastY = 0;

        g.setColor(color);

        for(int i = 0; i < x.length; i++) {
            g.drawLine((int)(lastX * scaling), height - (int)(lastY * scaling),
                    height - (int)(x[i] * scaling), (int)(y[i] * scaling));

            lastX = (int)x[i];
            lastY = (int)y[i];
        }
    }
}
