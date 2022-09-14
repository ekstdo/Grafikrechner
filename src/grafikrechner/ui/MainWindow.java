package grafikrechner.ui;

import grafikrechner.parser.ast.FunctionalAST;
import grafikrechner.util.PosParameters;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.function.Function;

public class MainWindow extends JFrame {
    PlotterPanel plotterPanel;
    TermPanel termPanel;

    ArrayList<FunctionalAST> functions;


    public MainWindow(){
        // change the font size
        UIManager.put("Label.font", new FontUIResource(new Font("Dialog", Font.PLAIN, 20)));
        UIManager.put("Button.font", new FontUIResource(new Font("Dialog", Font.BOLD, 20)));
        UIManager.put("TextField.font", new FontUIResource(new Font("Dialog", Font.PLAIN, 20)));

        plotterPanel = new PlotterPanel(this);
        termPanel = new TermPanel(this);
        functions = new ArrayList<>();

        setLayout(new GridLayout(0, 2));

        add(plotterPanel);
        add(new JScrollPane(termPanel));

        pack();
        setVisible(true);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }


}
