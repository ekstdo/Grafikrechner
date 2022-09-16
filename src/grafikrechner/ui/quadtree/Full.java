package grafikrechner.ui.quadtree;

import grafikrechner.parser.ast.FunctionalAST;
import grafikrechner.ui.PlotterPanel;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class Full extends Quadtree {
    @Override
    public Quadtree reduce(FunctionalAST f) {
        return this;
    }

    @Override
    public boolean isFull() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void draw(FunctionalAST f, PlotterPanel plotterPanel, Graphics g) {
    }

    @Override
    public List<Leaf> toList() {
        return new LinkedList<>();
    }

    @Override
    public boolean isLeaf() {
        return false;
    }
}
