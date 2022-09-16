package grafikrechner.ui.quadtree;

import grafikrechner.parser.ast.FunctionalAST;
import grafikrechner.ui.PlotterPanel;
import grafikrechner.util.PosParameters;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class Leaf extends Quadtree {
    public double xmin;
    public double ymin;
    public double xmax;
    public double ymax;
    int cachedSides;
    public double cachedTLVal; // top left value
    public double cachedTRVal;
    public double cachedBRVal;
    public double cachedBLVal;

    public boolean cached = false;

    Leaf(double xmin, double ymin, double xmax, double ymax){
        this.xmin = xmin;
        this.ymin = ymin;
        this.xmax = xmax;
        this.ymax = ymax;
        this.cachedSides = 0;
    }

    public void cacheValues(FunctionalAST f){
        if (cached) return;
        PosParameters input = new PosParameters(xmin, ymin);
        cachedTLVal = f.apply(input);
        input.x = xmax;
        cachedTRVal = f.apply(input);
        input.y = ymax;
        cachedBRVal = f.apply(input);
        input.x = xmin;
        cachedBLVal = f.apply(input);
        cached = true;
    }
    @Override
    public Quadtree reduce(FunctionalAST f) {
        cacheValues(f);
        if (cachedTLVal < 0 && cachedTRVal < 0 && cachedBLVal < 0 && cachedBRVal < 0){
            return new Empty();
        }

        if (cachedTLVal > 0 && cachedTRVal > 0 && cachedBLVal > 0 && cachedBRVal > 0){
            return new Full();
        }

        if (cachedTLVal * cachedTRVal < 0) cachedSides |= 0b0001;
        if (cachedBRVal * cachedTRVal < 0) cachedSides |= 0b0010;
        if (cachedBRVal * cachedBLVal < 0) cachedSides |= 0b0100;
        if (cachedTLVal * cachedBLVal < 0) cachedSides |= 0b1000;

        return this;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void draw(FunctionalAST f, PlotterPanel plotterPanel, Graphics g) {
        if (cachedSides == 0) return;
        double[] pos = zeroLine(f);

        g.setColor(new Color((int) (Math.random() * 255)));
        plotterPanel.drawLineTransformed(g, pos[0], pos[1], pos[2], pos[3]);
    }
    // y^2 + x^2 = 3


    @Override
    public List<Leaf> toList() {
        LinkedList<Leaf> returning = new LinkedList<>() ;
        returning.add(this);
        return returning;
    }

    static final int NUMBER_OF_STEPS = 10;

    public double[] zeroLine(FunctionalAST f) {
        PosParameters p1 = null;
        PosParameters p2 = null;
        if (cachedSides == 0) return null;
        if ((cachedSides & 0b0001) == 1) p1 = zeroPos(f, new PosParameters(xmin, ymin), new PosParameters(xmax, ymin)); // Top
        if ((cachedSides & 0b0010) == 0b0010) {
            PosParameters val = zeroPos(f, new PosParameters(xmax, ymin), new PosParameters(xmax, ymax)); // Right
            if (p1 == null) p1 = val;
            else p2 = val;
        }
        if ((cachedSides & 0b0100) == 0b0100) {
            PosParameters val = zeroPos(f, new PosParameters(xmin, ymax), new PosParameters(xmax, ymax)); // Bottom
            if (p1 == null) p1 = val;
            else p2 = val;
        }
        if ((cachedSides & 0b1000) == 0b1000) {
            PosParameters val = zeroPos(f, new PosParameters(xmin, ymin), new PosParameters(xmin, ymax)); // Left
            if (p1 == null) p1 = val;
            else p2 = val;
        }
        return new double[]{ p1.x, p1.y, p2.x, p2.y };
    }

    public static PosParameters zeroPos(FunctionalAST f, PosParameters p1, PosParameters p2){
        double val1 = f.apply(p1);
        double val2 = f.apply(p2);
        if (val1 >= 0 && val2 >= 0)
            throw new IllegalArgumentException("invalid positions");
        if (val1 >= 0)
            return zeroPos(f, p2, p1);

        PosParameters midPoint = p1;

        for (int i = 0; i < NUMBER_OF_STEPS; i++) {
            midPoint = new PosParameters((p1.x + p2.x) / 2.0, (p1.y + p2.y) / 2.0);
            double midValue = f.apply(midPoint);
            if (midValue >= 0) {
                p2 = midPoint;
            } else {
                p1 = midPoint;
            }
        }

        return midPoint;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }
}
