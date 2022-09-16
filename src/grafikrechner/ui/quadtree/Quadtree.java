package grafikrechner.ui.quadtree;

import grafikrechner.parser.ast.FunctionalAST;
import grafikrechner.ui.PlotterPanel;

import java.awt.*;
import java.util.List;

public abstract class Quadtree {
    public static Quadtree build(double steps, double xmin, double ymin, double xmax, double ymax) {
        if (steps <= 0) {
            return new Leaf(xmin, ymin, xmax, ymax);
        } else {
            double xmid = (xmin + xmax) / 2.0;
            double ymid = (ymin + ymax) / 2.0;
            return new Tree(
                    build(steps - 1, xmin, ymin, xmid, ymid),
                    build(steps - 1, xmid, ymin, xmax, ymid),
                    build(steps - 1, xmid, ymid, xmax, ymax),
                    build(steps - 1, xmin, ymid, xmid, ymax)
            );
        }
    }

    public abstract Quadtree reduce(FunctionalAST fun);

    public abstract boolean isFull();
    public abstract boolean isEmpty();
    public abstract boolean isLeaf();

    public abstract void draw(FunctionalAST f, PlotterPanel plotterPanel, Graphics g);

    public abstract List<Leaf> toList();
}
