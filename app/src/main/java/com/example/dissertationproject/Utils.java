package com.example.dissertationproject;

import android.app.AlertDialog;
import android.content.Context;
import org.apache.commons.lang3.StringUtils;


public class Utils {
    final static long millisecondsPerDay = 1000L * 60 * 60 * 24;

    public static void errorDialog(Context context, String title, String msg, String button){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);


        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(button, (dialog, id) -> {
                    dialog.dismiss();
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

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

    public static double findSimilarity(String x, String y) {

        double maxLength = Double.max(x.length(), y.length());
        if (maxLength > 0) {
            // optionally ignore case if needed
            return (maxLength - StringUtils.getLevenshteinDistance(x, y)) / maxLength;
        }
        return 1.0;
    }

    public static long timeInOneWeek(){
        return System.currentTimeMillis() + (millisecondsPerDay * 7);
    }

    public static long timeInOneMonth(){
        return System.currentTimeMillis() + (millisecondsPerDay * 28);
    }

    public static long timeInSixMonths(){
        return System.currentTimeMillis() + (millisecondsPerDay * (28 * 6));
    }
    
}
