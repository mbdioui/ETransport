package utils;

import android.content.Context;

import com.alertdialogpro.AlertDialogPro;

import tn.android.etransport.etransport.R;

/**
 * Created by mohamed salah on 06/11/2016.
 */

public class AlertDialogCustom {
    public static void show(Context context,String string)
    {
        AlertDialogPro.Builder alertDialogBuilder = new AlertDialogPro.Builder(context);
        alertDialogBuilder.setMessage(string);
        alertDialogBuilder.setTitle("informations");
        alertDialogBuilder.setIcon(R.drawable.ic_info_black_24dp);
        alertDialogBuilder.setPositiveButton("ok",null);
        alertDialogBuilder.create();
        alertDialogBuilder.show();
    }

}
