package tn.android.etransport.etransport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

public class Add_Transport2 extends Activity {
    private DatePicker Date_go;
    private DatePicker Date_arrival;
    private TimePicker Time_go;
    private TimePicker Time_arrival;
    private Button submit;
    private String destVille;
    private String startCountry;
    private String destCountry;
    private String startVille;
    private String destposition;
    private String startposition;
    private String Time_go_String="";
    private String Time_arrival_String="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add__transport2);
        SetUp();
        Intent intent = getIntent();
        Bundle B =intent.getExtras();
        if( B != null)
        {
            destVille= (String) B.get("Destination_Place");
            startVille = (String) B.get("Start_place");
            destCountry = (String) B.get("Destination_Country");
            startCountry = (String) B.get("Start_Country");
            startposition = (String)  B.get("Start_position");
            destposition = (String)  B.get("Dest_position");
        }
        //close button
        ImageButton close_btn=(ImageButton) findViewById(R.id.close_BTN2);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = Add_Transport2.this;
                Intent intent= new Intent(activity,MainClient.class);
                startActivity(intent);
                activity.finish();
            }
        });
    }
    private void SetUp()
    {
        // initiate the date picker and a button
        Date_go = (DatePicker) findViewById(R.id.dates_transport_go);
        submit = (Button) findViewById(R.id.BTN_transport_done);
        Date_arrival = (DatePicker) findViewById(R.id.dates_transport_arrival);
        Time_go = (TimePicker) findViewById(R.id.times_go);
        Time_arrival= (TimePicker) findViewById(R.id.times_arrive);
        Time_go.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Time_go_String = String.valueOf(hourOfDay).toString() + ":" + String.valueOf(minute).toString();
            }
        } );
        Time_arrival.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Time_arrival_String= String.valueOf(hourOfDay).toString() + ":" + String.valueOf(minute).toString();
            }
        } );
        // perform click event on submit button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the values for day of month , month and year from a date picker
                if(Time_go_String.equals(""))
                        Time_go_String= String.valueOf(Time_go.getHour())+ ":" +String.valueOf(Time_go.getMinute());
                Time_go_String= Date_go.getYear()+"-"+(Date_go.getMonth()+1)+"-"+Date_go.getDayOfMonth()+" "+Time_go_String;
                // display the values by using a toast
                Toast.makeText(getApplicationContext(),Time_go_String, Toast.LENGTH_LONG).show();
                clearall();
                //InsertTransportTask user_insert_task = new InsertTransportTask(Add_Transport2.this,getApplicationContext());
                //pass context in parameter
//                user_insert_task.execute(Links.getRootFolder()+"inserttransport.php", sprenom, sname, smail
//                        , smdp, phone, status);
//                KeyboardUtil.hideKeyboard(Add_Transport2.this);
            }
        });
    }

    private void clearall()
    {
        Time_arrival_String="";
        Time_go_String="";
    }


}
