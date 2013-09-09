package com.anywherelabs.util.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogUtils {

    // Show Ok Dialog
    public static void showOkDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
               .setCancelable(false)
               .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       dialog.cancel();
                   }
               });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public static void showOkCancelDialog(Context context, String message, final Runnable okCallback, 
            final Runnable cancelCallback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
               .setCancelable(false)
               .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       okCallback.run();
                   }
               })
               .setNegativeButton("Cancel",  new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       cancelCallback.run();
                   }
               });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
