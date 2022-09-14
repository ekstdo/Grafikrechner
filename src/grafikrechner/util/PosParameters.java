package grafikrechner.util;

public class PosParameters {
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
}
