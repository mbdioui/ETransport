package tn.android.etransport.etransport;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rey.material.widget.Switch;
import com.rey.material.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class Form_Time_Client extends Fragment implements View.OnClickListener {
    private TextView Datefixe;
    private TextView Dateflexible;
    private static Switch switchdate;
    private SimpleDateFormat dateFormatter;


    private EditText Datego;
    private EditText Datearrive;
    final private int DateGomaxID=1;
    final private int DatearriveminID=10;
    final private int DateGominID=11;
    final private int DatearrivemaxID=110;
    final private int DategoID=111;
    final private int DatearriveID=1110;
    private int day;
    private int year;
    private int mounth;
    private Date currentdate;
    private EditText date_max_arrive;
    private EditText date_min_arrive;
    private EditText date_max_go;
    private EditText date_min_go;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_form_time_client, container, false);
        editview(v);
        return v;
    }
    public static boolean checkswitch()
    {
        if (switchdate.isChecked())
            return true;
        return false;

    }
    private void editview(View v)
    {
        switchdate =(Switch) v.findViewById(R.id.switch_date);
        Datefixe= (TextView) v.findViewById(R.id.textview_fixe);
        Dateflexible= (TextView) v.findViewById(R.id.textview_flexible);
        final RelativeLayout flexiblelayout= (RelativeLayout) v.findViewById(R.id.flexible_Date_layout);
        final RelativeLayout fixlayout= (RelativeLayout) v.findViewById(R.id.fix_Date_layout);
        switchdate.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if (checked)
                {
                    Datefixe.setTextColor(getResources().getColor(R.color.blue_gray));
                    Dateflexible.setTextColor(getResources().getColor(R.color.Green));
                    flexiblelayout.setVisibility(View.VISIBLE);
                    fixlayout.setVisibility(View.INVISIBLE);
                }
                else
                {
                    Datefixe.setTextColor(getResources().getColor(R.color.Green));
                    Dateflexible.setTextColor(getResources().getColor(R.color.blue_gray));
                    flexiblelayout.setVisibility(View.INVISIBLE);
                    fixlayout.setVisibility(View.VISIBLE);
                }
            }
        });
        //FIXED DATES SELECTING
        Datego = (EditText) v.findViewById(R.id.date_depart);
        Datearrive =(EditText) v.findViewById(R.id.date_arrive);
        Datego.setOnClickListener(this);
        Datearrive.setOnClickListener(this);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
        Calendar c = Calendar.getInstance();
        year= c.get(Calendar.YEAR);
        mounth=c.get(Calendar.MONTH);
        day=c.get(Calendar.DAY_OF_MONTH);
        currentdate = new Date(System.currentTimeMillis());
        //FLEXIBLE DATES SELECTING
        date_min_go =(EditText) v.findViewById(R.id.date_depart_min);
        date_max_go =(EditText) v.findViewById(R.id.date_depart_max);
        date_min_arrive =(EditText) v.findViewById(R.id.date_arrive_min);
        date_max_arrive = (EditText) v.findViewById(R.id.date_arrive_max);
        date_min_go.setOnClickListener(this);
        date_max_go.setOnClickListener(this);
        date_min_arrive.setOnClickListener(this);
        date_max_arrive.setOnClickListener(this);
    }

    private void showDateDialog(int id) {
        switch (id)
        {   case DategoID:
                DateDialog(Datego);
            break;
            case DatearriveID:
                DateDialog(Datearrive);
            break;
            case DateGominID:
                DateDialog(date_min_go);
                break;
            case DateGomaxID:
                DateDialog(date_max_go);
                break;
            case DatearriveminID:
                DateDialog(date_min_arrive);
                break;
            case DatearrivemaxID:
                DateDialog(date_max_arrive);
                break;
        }
    }

    private void DateDialog(final EditText v)
    {
        DatePickerDialog datearrive = new DatePickerDialog(getActivity(), R.style.datepicker,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        if(newDate.getTime().before(currentdate))
                            Toast.makeText(getActivity(),"la date entrée doit etre supérieur à la date courante",Toast.LENGTH_LONG).show();
                        else
                            v.setText(dateFormatter.format(newDate.getTime()));
                    }

                },year,mounth,day);
        datearrive.show();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date_depart:
            showDateDialog(DategoID);
            break;
            case R.id.date_arrive:
                showDateDialog(DatearriveID);
            break;
            case R.id.date_depart_min:
                showDateDialog(DateGominID);
            break;
            case R.id.date_depart_max:
                showDateDialog(DateGomaxID);
            break;
            case R.id.date_arrive_min:
                showDateDialog(DatearriveminID);
            break;
            case R.id.date_arrive_max:
                showDateDialog(DatearrivemaxID);
            break;
        }
    }
}
