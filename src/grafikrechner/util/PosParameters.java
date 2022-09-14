package grafikrechner.util;

public class PosParameters {
    double x;
    double y;
    double rCached;
    double phiCached;

    PosParameters(double x, double y){
        this.x = x;
        this.y = y;
        double[] converted = convert(x, y);
        this.rCached = converted[0];
        this.phiCached = converted[1];
    }

    public static double[] convert(double x, double y){
        double[] returning = new double[2];
        returning[0] = Math.sqrt(x * x + y * y); // r
        returning[1] = Math.atan2(y, x); // phi
        return returning;
    }
}
