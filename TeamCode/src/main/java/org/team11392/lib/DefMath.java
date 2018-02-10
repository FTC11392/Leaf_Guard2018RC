package org.team11392.lib;

/**
 * Created by lu_ha on 2/9/2018.
 */

public class DefMath {
    public double nthRoot(int n, double A) {
        double x0 = 1;
        boolean accurate = false;
        while (!accurate) {
            double x1 = (1 / (double)n) * ((n - 1) * x0 + A / Math.pow(x0, n - 1));
            accurate = accurate(x0, x1);
            x0 = x1;
        }
        return x0;
    }


    private static boolean accurate(double x0, double x1) {
        return Math.abs(x1-x0) < 0.000001;
    }
}
