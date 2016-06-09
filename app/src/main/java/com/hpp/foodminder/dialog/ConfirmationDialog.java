package com.hpp.foodminder.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Adeel on 1/13/2016.
 */
public class ConfirmationDialog {

    public static Dialog createDialog(Context context, int messageId,
                                      DialogInterface.OnClickListener onClick) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure you want to delete conversation?")
                .setPositiveButton("Yes", onClick)
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Do Nothing
                            }
                        });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
