package com.example.dissertationproject.ui.stats;

import java.util.ArrayList;

public class LinearRegression {

    private double gradient;
    private double intercept;

    public void fit(ArrayList<Long> x, ArrayList<Double> y) {
        double x_mean = meanLong(x);
        double y_mean = meanDouble(y);
        double numerator = 0.0;
        double denominator = 0.0;

        for (int i = 0; i < x.size(); i++) {
            numerator += (x.get(i) - x_mean) * (y.get(i) - y_mean);
            denominator += Math.pow((x.get(i) - x_mean), 2);
        }

        gradient = numerator / denominator;
        intercept = y_mean - gradient * x_mean;
    }

    private static double meanDouble(ArrayList<Double> data) {
        if(data.size() == 0) return 0;

        double sum = 0.0;
        for (double d : data) {
            sum += d;
        }
        return sum / data.size();
    }
    private static long meanLong(ArrayList<Long> data) {
        if(data.size() == 0) return 0;

        long sum = (long) 0.0;
        for (long d : data) {
            sum += d;
        }
        return sum / data.size();
    }

    public double predict(long x) {
        return gradient * x + intercept;
    }

    public double getGradient() {
        return gradient;
    }

    public double getIntercept() {
        return intercept;
    }
}