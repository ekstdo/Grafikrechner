package grafikrechner.ui;

import grafikrechner.util.PosParameters;

import javax.swing.*;
import java.awt.*;

public class PlotterPanel extends JPanel  {
    double[] trafoMatrix = {1.0, 0.0, 50.0, 0.0, 1.0, 50};
    public PlotterPanel(MainWindow plotter){
        setBackground(new Color(210, 210, 210));
        setPreferredSize(new Dimension(100, 100));


    }
}

