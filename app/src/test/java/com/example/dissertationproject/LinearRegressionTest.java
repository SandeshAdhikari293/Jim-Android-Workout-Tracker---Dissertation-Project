package com.example.dissertationproject;

import static junit.framework.TestCase.assertEquals;

import com.example.dissertationproject.ui.stats.LinearRegression;

import org.junit.Test;

import java.util.ArrayList;

public class LinearRegressionTest {
    /**
     * Normal indicates positive, non-extreme values
     */
    ArrayList<Long> xNormal = new ArrayList<>();
    ArrayList<Double> yNormal = new ArrayList<>();


    ArrayList<Long> xExtreme = new ArrayList<>();
    ArrayList<Double> yExtreme = new ArrayList<>();

    public LinearRegressionTest(){

        //Populate normal dataset for regression model
        xNormal.add(1l);
        xNormal.add(2l);
        xNormal.add(3l);
        xNormal.add(4l);
        xNormal.add(5l);

        yNormal.add(2d);
        yNormal.add(5d);
        yNormal.add(6d);
        yNormal.add(10d);
        yNormal.add(9d);

        //Populate extreme dataset for regression model
        xExtreme.add(1290l);
        xExtreme.add(312l);
        xExtreme.add(5432l);
        xExtreme.add(321l);
        xExtreme.add(454l);

        yExtreme.add(-23d);
        yExtreme.add(12439d);
        yExtreme.add(23d);
        yExtreme.add(132d);
        yExtreme.add(43214d);
    }



    /**
     *
     * Testing normal values
     *
     */

    /**
     * Test the gradient is correct for normal values
     */
    @Test
    public void testNormalDatasetGradient(){
        LinearRegression linearRegression = new LinearRegression();

        linearRegression.fit(xNormal, yNormal);
        assertEquals(1.9, linearRegression.getGradient());
    }

    /**
     * Test the intercept is correct for normal values
     */
    @Test
    public void testNormalDatasetIntercept(){
        LinearRegression linearRegression = new LinearRegression();

        linearRegression.fit(xNormal, yNormal);
        assertEquals(0.7, linearRegression.getIntercept(), 0.01);
    }

    /**
     * Test a prediction for the linear regression model on normal data values
     */
    @Test
    public void testNormalDatasetPrediction1(){
        LinearRegression linearRegression = new LinearRegression();

        linearRegression.fit(xNormal, yNormal);
        assertEquals(2.6, linearRegression.predict(1), 0.01);
    }

    /**
     * Test a prediction for the linear regression model on normal data values
     */
    @Test
    public void testNormalDatasetPrediction2(){
        LinearRegression linearRegression = new LinearRegression();

        linearRegression.fit(xNormal, yNormal);
        assertEquals(38.7, linearRegression.predict(20), 0.01);
    }

    /**
     * Test a prediction for the linear regression model on normal data values
     * Testing extreme data
     */
    @Test
    public void testNormalDatasetPrediction3(){
        LinearRegression linearRegression = new LinearRegression();

        linearRegression.fit(xNormal, yNormal);
        assertEquals(95000.7, linearRegression.predict(50000), 0.01);
    }

    /**
     * Test a prediction for the linear regression model on normal data values
     * Testing negative values
     */
    @Test
    public void testNormalDatasetPrediction4(){
        LinearRegression linearRegression = new LinearRegression();

        linearRegression.fit(xNormal, yNormal);
        assertEquals(-960.7, linearRegression.predict(-506), 0.01);
    }


    /**
     *
     * Testing extreme values
     *
     */

    /**
     * Test the gradient is correct for extreme values
     */
    @Test
    public void testExtremeDatasetGradient(){
        LinearRegression linearRegression = new LinearRegression();

        linearRegression.fit(xExtreme, yExtreme);
        assertEquals(-3.27573, linearRegression.getGradient(), 0.01);
    }

    /**
     * Test the intercept is correct for extreme values
     */
    @Test
    public void testExtremeDatasetIntercept(){
        LinearRegression linearRegression = new LinearRegression();

        linearRegression.fit(xExtreme, yExtreme);
        assertEquals(16270.40754, linearRegression.getIntercept(), 0.1);
    }

    /**
     * Test a prediction for the linear regression model on extreme data values
     */
    @Test
    public void testExtremeDatasetPrediction1(){
        LinearRegression linearRegression = new LinearRegression();

        linearRegression.fit(xExtreme, yExtreme);
        assertEquals(16267.13181, linearRegression.predict(1), 100);
    }

    /**
     * Test a prediction for the linear regression model on extreme data values
     */
    @Test
    public void testExtremeDatasetPrediction2(){
        LinearRegression linearRegression = new LinearRegression();

        linearRegression.fit(xExtreme, yExtreme);
        assertEquals(16204.89294, linearRegression.predict(20), 100);
    }

    /**
     * Test a prediction for the linear regression model on extreme data values
     * Testing extreme data
     */
    @Test
    public void testExtremeDatasetPrediction3(){
        LinearRegression linearRegression = new LinearRegression();

        linearRegression.fit(xExtreme, yExtreme);
        assertEquals(-147516.0925, linearRegression.predict(50000), 100);
    }

    /**
     * Test a prediction for the linear regression model on extreme data values
     * Testing negative values
     */
    @Test
    public void testExtremeDatasetPrediction4(){
        LinearRegression linearRegression = new LinearRegression();

        linearRegression.fit(xExtreme, yExtreme);
        assertEquals(17927.92692, linearRegression.predict(-506), 100);
    }
}
