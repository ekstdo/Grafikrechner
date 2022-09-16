package grafikrechner.ui;

import grafikrechner.parser.Term;
import grafikrechner.ui.quadtree.Quadtree;
import grafikrechner.util.PosParameters;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PlotterPanel extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener {
    double[] trafoMatrix;
    PosParameters previousMousePos = new PosParameters(-1, -1);
    MainWindow plotter;
    public PlotterPanel(MainWindow plotter){
        setBackground(new Color(210, 210, 210));
        setPreferredSize(new Dimension(100, 100));

        addMouseMotionListener(this);
        addMouseListener(this);
        addMouseWheelListener(this);
        trafoMatrix  = new double[]{20.0, 0.0, 50.0, 0.0, -20.0, 50};


        this.plotter = plotter;
    }

    public void drawLineTransformed(Graphics g, double x1, double y1, double x2, double y2){
        double[] transformed = PosParameters.transform(new double[]{x1, y1, x2, y2}, trafoMatrix);
        g.drawLine((int) transformed[0], (int) transformed[1], (int) transformed[2], (int) transformed[3]);
    }

    public void drawRectTransformed(Graphics g, double x1, double y1, double x2, double y2){
        double[] transformed = PosParameters.transform(new double[]{x1, y1, x2, y2}, trafoMatrix);
        double xmin = Math.min(transformed[0], transformed[2]);
        double xmax = Math.max(transformed[0], transformed[2]);
        double ymin = Math.min(transformed[1], transformed[3]);
        double ymax = Math.max(transformed[1], transformed[3]);
        g.fillRect((int) xmin, (int) ymin, (int) (xmax - xmin), (int) (ymax - ymin));
    }

    @Override
    protected void paintComponent(Graphics g) {
        int textOffset = 15;
        int textOffset2 = 6;
        double steps = getWidth();
        super.paintComponent(g);

        double[] domain = new double[]{0, 0, getWidth(), 0, getWidth(), getHeight(), 0, getHeight()};
        domain = PosParameters.inverseTransform(domain, trafoMatrix);
        double[] bounds = getDomainBB(domain);

        double nearestPower = Math.pow(10, Math.floor(Math.log10(bounds[2] - bounds[0])  ));
        double lowestRoundedOffsetX = (Math.floor(bounds[0] / nearestPower * 2)) * nearestPower / 2;
        double lowestRoundedOffsetY = (Math.floor(bounds[1] / nearestPower * 2)) * nearestPower / 2;

        g.setColor(Color.black);
        drawLineTransformed(g, bounds[0], 0,bounds[2], 0);
        drawLineTransformed(g, 0, bounds[1], 0, bounds[3]);

        for (double xcoord = lowestRoundedOffsetX; xcoord <= bounds[2]; xcoord += nearestPower / 2){
            if (xcoord == 0) continue;

            g.setColor(Color.lightGray);
            for (double xsubcoord = xcoord; xsubcoord < xcoord + nearestPower; xsubcoord += nearestPower /10){
                drawLineTransformed(g, xsubcoord, bounds[1], xsubcoord, bounds[3]);
            }
            g.setColor(Color.white);
            drawLineTransformed(g, xcoord, bounds[1], xcoord,bounds[3]);
            g.setColor(Color.black);

            double[] result = PosParameters.transform(xcoord, 0.0, trafoMatrix);
            g.drawString("" + xcoord, (int) result[0] - textOffset, Math.max(Math.min((int) result[1] + textOffset, getHeight() - textOffset), textOffset));


        }

        for (double ycoord = lowestRoundedOffsetY; ycoord < bounds[3]; ycoord += nearestPower / 2){
            double[] result = PosParameters.transform(0.0, ycoord, trafoMatrix);

            g.setColor(Color.lightGray);
            for (double ysubcoord = ycoord; ysubcoord < ycoord + nearestPower; ysubcoord += nearestPower /10){
                drawLineTransformed(g, bounds[0], ysubcoord, bounds[2], ysubcoord);
            }
            g.setColor(Color.white);
            drawLineTransformed(g, bounds[0], ycoord,bounds[2], ycoord);
            g.setColor(Color.black);

            g.drawString("" + ycoord, Math.min(Math.max((int) result[0] + textOffset, textOffset), getWidth() - 2 * textOffset),  (int) result[1] + textOffset2);
        }

        drawLineTransformed(g, bounds[0], 0,bounds[2], 0);
        drawLineTransformed(g, 0, bounds[1], 0, bounds[3]);


        plotFunctions(g, bounds, steps);
    }

    static double[] getDomainBB(double[] domain){
        double minX = Math.min(Math.min(Math.min(domain[0], domain[2]), domain[4]), domain[6]);
        double maxX = Math.max(Math.max(Math.max(domain[0], domain[2]), domain[4]), domain[6]);
        double minY = Math.min(Math.min(Math.min(domain[1], domain[3]), domain[5]), domain[7]);
        double maxY = Math.max(Math.max(Math.max(domain[1], domain[3]), domain[5]), domain[7]);
        return new double[] {minX, minY, maxX, maxY};
    }

    private void plotFunctions(Graphics g, double[] bounds, double steps) {
        double stepSize = (bounds[2] - bounds[0]) / steps;
        for (Term function : plotter.functions) {
            if (function == null) continue;
            if (function.isImplicit()) {
                double lsteps = Math.log(steps) / Math.log(2) - 3; // implicit takes lot longer to render, so we make it less exact and maybe interpolate along the way
                Quadtree result = Quadtree.build(lsteps, bounds[0], bounds[1], bounds[2], bounds[3]);


                // long t1 = System.currentTimeMillis();
                result = result.reduce(function.fun);
                // long t2 = System.currentTimeMillis();
                result.draw(function.fun, this, g);
                // long t3 = System.currentTimeMillis();
                // System.out.println("calc time: " + (t2 - t1) + "  ms, draw time: " + (t3 - t2) + " ms");



            } else {
                PosParameters input = new PosParameters(bounds[0] - 1,  0);
                PosParameters previous = new PosParameters(input.x, function.fun.apply(input));

                for (double x = bounds[0]; x <= bounds[2]; x += stepSize){
                    input.x = x;
                    double val = function.fun.apply(input);

                    if (Double.isNaN(val)) continue;
                    if (Double.isNaN(previous.y)) {
                        previous.x = x;
                        previous.y = val;
                        continue;
                    }

                    drawLineTransformed(g, previous.x, previous.y, x, val);
                    previous.x = x;
                    previous.y = val;
                }
            }
        }

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)){
            trafoMatrix[2] += e.getX() - previousMousePos.x;
            trafoMatrix[5] += e.getY() - previousMousePos.y;
            previousMousePos.x = e.getX();
            previousMousePos.y = e.getY();
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        previousMousePos.x = e.getX();
        previousMousePos.y = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double amount = e.getPreciseWheelRotation();
        double factor = 0.1;

        double prevTrafoX = trafoMatrix[0];
        double prevTrafoY = trafoMatrix[4];
        trafoMatrix[0] = Math.max(Double.MIN_NORMAL, trafoMatrix[0] * Math.pow(2, factor * amount));
        trafoMatrix[4] = Math.min(-Double.MIN_NORMAL, trafoMatrix[4] * Math.pow(2, factor * amount));

        if (trafoMatrix[0] > 1.0E13){
            trafoMatrix[0] = prevTrafoX;
            trafoMatrix[4] = prevTrafoY;
            return;
        }
        if ((e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 64) {
            trafoMatrix[2] = (trafoMatrix[2] - e.getX()) * trafoMatrix[0] / prevTrafoX + e.getX();
            trafoMatrix[5] = (trafoMatrix[5] - e.getY()) * trafoMatrix[4] / prevTrafoY + e.getY();
        }

        repaint();
    }

    public void initialCallback() {
        trafoMatrix[2] = getWidth() / 2.0;
        trafoMatrix[5] = getHeight() / 2.0;
    }
}

