package com.example.dissertationproject;

import android.app.AlertDialog;
import android.content.Context;

public class Utils {

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
}
