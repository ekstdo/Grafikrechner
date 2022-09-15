package grafikrechner.util;

import java.awt.*;

public class PosParameters extends Point {
    public double x;
    public double y;
    public double rCached;
    public double phiCached;

    public PosParameters(double x, double y){
        this.x = x;
        this.y = y;
    }

    public static double[] convert(double x, double y){
        double[] returning = new double[2];
        returning[0] = Math.sqrt(x * x + y * y); // r
        returning[1] = Math.atan2(y, x); // phi
        return returning;
    }

    public void convert(){
        double[] converted = convert(x, y);
        this.rCached = converted[0];
        this.phiCached = converted[1];
    }

    public double[] transform(double[] trafomatrix) {
        double[] result = new double[2];
        result[0] = trafomatrix[0] * x + trafomatrix[1] * y + trafomatrix[2];
        result[1] = trafomatrix[3] * x + trafomatrix[4] * y + trafomatrix[5];
        return result;
    }

    public static double[] transform(double[] coords, double[] trafomatrix) {
        double[] result = new double[coords.length];
        for (int i = 0; i < coords.length; i += 2) {
            result[i] = trafomatrix[0] * coords[i] + trafomatrix[1] * coords[i + 1] + trafomatrix[2];
            result[i + 1] = trafomatrix[3] * coords[i] + trafomatrix[4] * coords[i + 1] + trafomatrix[5];
        }
        return result;
    }

    public static double[] inverseTransform(double[] coords, double[] trafomatrix) {
        double[] result = new double[coords.length];
        for (int i = 0; i < coords.length; i += 2) {
            double[] temp = inverseTransform(coords[i], coords[i + 1], trafomatrix);
            result[i] = temp[0];
            result[i + 1] = temp[1];
        }
        return result;
    }

    public static double[] transform(double x, double y, double[] trafomatrix) {
        double[] result = new double[2];
        result[0] = trafomatrix[0] * x + trafomatrix[1] * y + trafomatrix[2];
        result[1] = trafomatrix[3] * x + trafomatrix[4] * y + trafomatrix[5];
        return result;
    }

    public static double[] inverseTransform(double x, double y, double[] trafomatrix) {
        double[] result = new double[2];
        double determinant = trafomatrix[0] * trafomatrix[4] - trafomatrix[1] * trafomatrix[3];
        x -= trafomatrix[2];
        y -= trafomatrix[5];
        result[0] = 1/determinant * (trafomatrix[4] * x - trafomatrix[1] * y);
        result[1] = 1/determinant * (-trafomatrix[3] * x + trafomatrix[0] * y);
        return result;
    }
}
