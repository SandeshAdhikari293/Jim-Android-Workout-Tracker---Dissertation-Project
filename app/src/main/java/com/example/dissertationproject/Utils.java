/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject;

import android.app.AlertDialog;
import android.content.Context;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;


public class Utils {

    /**
     * Display an error dialog to the user
     * @param context   the application context
     * @param title     the title of the error message
     * @param msg       the content of the message
     * @param button    the name of the button to continue
     */
    public static void errorDialog(Context context, String title, String msg, String button){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);


        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(button, (dialog, id) -> dialog.dismiss());

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    /**
     * Displays a confirmation button to the user
     * @param context       the application context
     * @param title         the title of the dialog
     * @param msg           the content of the dialog
     * @param negativeBtn   the label of the reject button
     * @param positiveBtn   the label of the accept button
     * @param negativeRun   the code to be executed if the negative button is pressed
     * @param positiveRun   the code to be executed if the positive button is pressed
     */
    public static void confirmDialog(Context context, String title, String msg, String negativeBtn, String positiveBtn, Runnable negativeRun, Runnable positiveRun){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);


        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(positiveBtn, (dialog, id) -> {
                    //run the code
                    positiveRun.run();
                    dialog.dismiss();
                }).setNegativeButton(negativeBtn, (dialog, i) -> {
                    negativeRun.run();
                    dialog.dismiss();
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    /**
     * Determine how similar two strings are
     * @param x
     * @param y
     * @return
     */
    public static double findSimilarity(String x, String y) {
        double maxLength = Double.max(x.length(), y.length());
        if (maxLength > 0) {
            return (maxLength - StringUtils.getLevenshteinDistance(x, y)) / maxLength;
        }
        return 1.0;
    }

    /**
     * Method that returns the time in one week in milliseconds
     * @return  millisecond time in 1 week
     */
    public static long timeInOneWeek(){
        return System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(7, TimeUnit.DAYS);
    }

    /**
     * Method that returns the time in one month in milliseconds
     * @return  millisecond time in 1 month
     */
    public static long timeInOneMonth(){
        return System.currentTimeMillis() + (TimeUnit.MILLISECONDS.convert(28, TimeUnit.DAYS));
    }

    /**
     * Method that returns the time in six months in milliseconds
     * @return  millisecond time in 6 months
     */
    public static long timeInSixMonths(){
        return System.currentTimeMillis() + (TimeUnit.MILLISECONDS.convert(7, TimeUnit.DAYS) * 6);
    }

    /**
     * Method that returns the time one week prior
     * @return  millisecond time 1 week ago
     */
    public static long timeLastOneWeek(){
        return System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(7, TimeUnit.DAYS);
    }

    /**
     * Method that returns the time one month prior
     * @return  millisecond time 1 month ago
     */
    public static long timeLastOneMonth(){
        return System.currentTimeMillis() - (TimeUnit.MILLISECONDS.convert(28, TimeUnit.DAYS));
    }

    /**
     * Method that returns the time six months prior
     * @return  millisecond time 6 months ago
     */
    public static long timeLastSixMonths(){
        return System.currentTimeMillis() - (TimeUnit.MILLISECONDS.convert(28, TimeUnit.DAYS) * 6);
    }
    
}
