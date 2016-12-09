package utils;

/**
 * Created by mohamed salah on 12/10/2016.
 */

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import tn.android.etransport.etransport.R;

public class DatePickerFragment extends android.support.v4.app.DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(getActivity(),this,year,month,day);

        // Create a TextView programmatically.
        TextView tv = new TextView(getActivity());

        // Create a TextView programmatically
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, // Width of TextView
                RelativeLayout.LayoutParams.WRAP_CONTENT); // Height of TextView
        tv.setLayoutParams(lp);
        tv.setPadding(10, 10, 10, 10);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
        tv.setText("Sélectionner une date");
        tv.setTextColor(getResources().getColor(R.color.Gray));
        tv.setBackgroundColor(getResources().getColor(R.color.White));

        // Set the newly created TextView as a custom tile of DatePickerDialog
        dpd.setCustomTitle(tv);
        return  dpd;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {

        }
}
