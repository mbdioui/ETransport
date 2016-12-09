package utils;

import android.app.AlertDialog;
import android.content.Context;

import tn.android.etransport.etransport.R;

/**
 * Created by mohamed salah on 06/11/2016.
 */

public class AlertDialogCustom {
    public static void show(Context context,String string)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context,R.style.dialog_theme);
        alertDialogBuilder.setMessage(string);
        alertDialogBuilder.setTitle("informations");
        alertDialogBuilder.setIcon(R.drawable.ic_info_black_24dp);
        alertDialogBuilder.setPositiveButton("ok",null);
        alertDialogBuilder.create();
        alertDialogBuilder.show();
    }

}
