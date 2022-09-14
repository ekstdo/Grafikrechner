package grafikrechner.ui;

import grafikrechner.util.PosParameters;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.function.Function;

public class MainWindow extends JFrame {
    PlotterPanel plotterPanel;
    TermPanel termPanel;

    ArrayList<Function<PosParameters, Double>> functions;


    public MainWindow(){
        plotterPanel = new PlotterPanel(this);
        termPanel = new TermPanel(this);
        functions = new ArrayList<>();

        setLayout(new GridLayout(0, 2));

        add(plotterPanel);
        add(termPanel);

        pack();
        setVisible(true);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }


}
