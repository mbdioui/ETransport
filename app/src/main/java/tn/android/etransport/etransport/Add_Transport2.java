package tn.android.etransport.etransport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import tasks.InsertTransportTask;
import utils.KeyboardUtil;
import utils.Links;

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
    private EditText Transport_text;
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
//        Time_go.setHour(System.c);
        Time_arrival= (TimePicker) findViewById(R.id.times_arrive);
        Transport_text =(EditText) findViewById(R.id.description_transport);
        // perform click event on submit button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if the time go were changed
                Time_go_String= String.valueOf(Time_go.getHour())+ ":" +String.valueOf(Time_go.getMinute());
                Time_go_String= Date_go.getYear()+"-"+(Date_go.getMonth()+1)+"-"+Date_go.getDayOfMonth()+" "+Time_go_String;

                Time_arrival_String= String.valueOf(Time_arrival.getHour())+ ":" +String.valueOf(Time_arrival.getMinute());
                Time_arrival_String= Date_arrival.getYear()+"-"+(Date_arrival.getMonth()+1)+"-"+Date_arrival.getDayOfMonth()+" "+Time_arrival_String;
                // display the values by using a toast
//                Toast.makeText(getApplicationContext(),Time_arrival_String, Toast.LENGTH_LONG).show();
                InsertTransportTask user_transport_task = new InsertTransportTask(Add_Transport2.this,getApplicationContext());
//                user_transport_task.execute(Links.getRootFolder()+"inserttransport.php", String.valueOf(UserInfos.getConnecteduser().getId()),
//                        Time_go_String,Time_arrival_String,Transport_text.getTransport_text().toString(),
//                        startVille,destVille,startCountry,destCountry,startposition,destposition);
                user_transport_task.setParentactivity(Add_Transport2.this);
                user_transport_task.execute(Links.getRootFolder()+"inserttransport.php", "7",
                        Time_go_String,Time_arrival_String,Transport_text.getText().toString(),
                        startVille,destVille,startCountry,destCountry,startposition,destposition);
                KeyboardUtil.hideKeyboard(Add_Transport2.this);
                clearall();
            }
        });
    }

    private void clearall()
    {
        Time_arrival_String="";
        Time_go_String="";
        Transport_text.setText("");
    }


}
