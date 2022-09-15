package grafikrechner.ui;

import grafikrechner.parser.Term;
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

    @Override
    protected void paintComponent(Graphics g) {
        int textOffset = 15;
        double steps = getWidth();
        super.paintComponent(g);

        double[] domain = new double[]{0, 0, getWidth(), getHeight()};
        domain = PosParameters.inverseTransform(domain, trafoMatrix);

        double nearestPower = Math.pow(10, 2 - (int) Math.log10(trafoMatrix[0]));
        double nearestOffsetX = -((int) (trafoMatrix[2] / trafoMatrix[0] / nearestPower * 2)) * nearestPower / 2;
        double nearestOffsetY = -((int) (trafoMatrix[5] / trafoMatrix[4] / nearestPower * 2)) * nearestPower / 2;

        g.setColor(Color.black);
        g.drawLine((int) (-trafoMatrix[2] * 50), (int) trafoMatrix[5], (int) (+ trafoMatrix[2] * 50), (int) trafoMatrix[5]);
        g.drawLine((int) trafoMatrix[2], (int) (-trafoMatrix[5] * 50), (int) trafoMatrix[2], (int) (+ trafoMatrix[5] * 50));

        for (int i = -10; i < 10; i++){
            double xcoord = nearestOffsetX + i * nearestPower / 2;
            if (xcoord == 0) continue;
            double[] result = PosParameters.transform(xcoord, 0.0, trafoMatrix);
            g.drawString("" + xcoord, (int) result[0], (int) result[1] + textOffset);
        }

        for (int i = -20; i < 20; i++){
            double ycoord = nearestOffsetY + i * nearestPower / 2;
            double[] result = PosParameters.transform(0.0, ycoord, trafoMatrix);
            g.drawString("" + ycoord, (int) result[0],  (int) result[1]+ textOffset );
        }

        plotFunctions(g, domain, steps);
    }

    private void plotFunctions(Graphics g, double[] domain, double steps) {
        double stepSize = (domain[2] - domain[0]) / steps;
        for (Term function : plotter.functions) {
            if (function == null) continue;
            if (!function.is_implicit()){
                PosParameters input = new PosParameters(domain[0] - 1,  0);
                PosParameters previous = new PosParameters(input.x, function.fun.apply(input));
                double[] preTransformed = previous.transform(trafoMatrix);
                previous.x = preTransformed[0];
                previous.y = preTransformed[1];

                for (double x = domain[0]; x <= domain[2]; x += stepSize){
                    input.x = x;
                    double val = function.fun.apply(input);
                    double[] transformed = PosParameters.transform(x, val, trafoMatrix);

                    if (Double.isNaN(val)) continue;
                    if (Double.isNaN(previous.y)) {
                        previous.x = transformed[0];
                        previous.y = transformed[1];
                        continue;
                    }

                    g.drawLine((int) previous.x, (int) previous.y, (int) transformed[0], (int) transformed[1]);
                    previous.x = transformed[0];
                    previous.y = transformed[1];
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
        trafoMatrix[0] = Math.max(Double.MIN_NORMAL, Math.exp(Math.log(trafoMatrix[0]) + factor * amount));
        trafoMatrix[4] = Math.min(-Double.MIN_NORMAL, -Math.exp(Math.log(-trafoMatrix[4]) + factor * amount));
        repaint();
    }

    public void initialCallback() {
        trafoMatrix[2] = getWidth() / 2;
        trafoMatrix[5] = getHeight() / 2;
        System.out.println(getWidth());
    }
}

