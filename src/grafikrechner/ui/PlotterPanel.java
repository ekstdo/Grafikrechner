package grafikrechner.ui;

import grafikrechner.util.PosParameters;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PlotterPanel extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener {
    double[] trafoMatrix = {20.0, 0.0, 50.0, 0.0, -20.0, 50};
    PosParameters previousMousePos = new PosParameters(-1, -1);
    public PlotterPanel(MainWindow plotter){
        setBackground(new Color(210, 210, 210));
        setPreferredSize(new Dimension(100, 100));

        addMouseMotionListener(this);
        addMouseListener(this);
        addMouseWheelListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        int textOffset = 15;
        super.paintComponent(g);

        double getNearestPower = Math.pow(10, 2 - (int) Math.log10(trafoMatrix[0]));
        double getNearestOffsetX = -((int) (trafoMatrix[2] / trafoMatrix[0] / getNearestPower * 2)) * getNearestPower / 2;
        double getNearestOffsetY = -((int) (trafoMatrix[5] / trafoMatrix[4] / getNearestPower * 2)) * getNearestPower / 2;

        g.setColor(Color.black);
        g.drawLine((int) (-trafoMatrix[2] * 50), (int) trafoMatrix[5], (int) (+ trafoMatrix[2] * 50), (int) trafoMatrix[5]);
        g.drawLine((int) trafoMatrix[2], (int) (-trafoMatrix[5] * 50), (int) trafoMatrix[2], (int) (+ trafoMatrix[5] * 50));

        for (int i = -10; i < 10; i++){
            double xcoord = getNearestOffsetX + i * getNearestPower / 2;
            if (xcoord == 0) continue;
            double[] result = PosParameters.transform(xcoord, 0.0, trafoMatrix);
            g.drawString("" + xcoord, (int) result[0], (int) result[1] + textOffset);
        }

        for (int i = -20; i < 20; i++){
            double ycoord = getNearestOffsetY + i * getNearestPower / 2;
            double[] result = PosParameters.transform(0.0, ycoord, trafoMatrix);
            g.drawString("" + ycoord, (int) result[0],  (int) result[1]+ textOffset );
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
}

