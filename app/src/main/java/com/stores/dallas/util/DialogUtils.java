package com.stores.dallas.util;

import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * Created by MobileBillionare on 1/10/16.
 */
public class DialogUtils {

    public static final void createErrorDialog(String title, String message,Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();


    }
}
