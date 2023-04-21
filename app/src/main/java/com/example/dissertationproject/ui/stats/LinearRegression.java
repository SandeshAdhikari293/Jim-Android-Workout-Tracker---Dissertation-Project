/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.ui.stats;

import java.util.ArrayList;

public class LinearRegression {

    private double gradient;
    private double intercept;

    /**
     * Trains the machine learning algorithm to fit the current data
     * @param x the x dataset
     * @param y the y dataset
     */
    public void fit(ArrayList<Long> x, ArrayList<Double> y) {
        double xMean = meanLong(x);
        double yMean = meanDouble(y);
        double numerator = 0.0;
        double denominator = 0.0;

        //Fits the data using error function
        for (int i = 0; i < x.size(); i++) {
            numerator += (x.get(i) - xMean) * (y.get(i) - yMean);
            denominator += Math.pow((x.get(i) - xMean), 2);
        }

        gradient = numerator / denominator;
        intercept = yMean - gradient * xMean;
    }

    /**
     * Calculates the mean of an array list
     * @param data  array list data set
     * @return      the mean value of the dataset as a double
     */
    private static double meanDouble(ArrayList<Double> data) {
        if(data.size() == 0) return 0; //avoid a divide by 0 error

        double sum = 0.0;
        for (double d : data) {
            sum += d;
        }
        return sum / data.size();
    }
    /**
     * Calculates the mean of an array list
     * @param data  array list data set
     * @return      the mean value of the dataset as a long
     */
    private static long meanLong(ArrayList<Long> data) {
        if(data.size() == 0) return 0; //avoid a divide by 0 error

        long sum = (long) 0.0;
        for (long d : data) {
            sum += d;
        }
        return sum / data.size();
    }

    /**
     * Use the model to predict the value of a new, unseen data point
     * @param x the new data point
     * @return  the predicted y value for the data
     */
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