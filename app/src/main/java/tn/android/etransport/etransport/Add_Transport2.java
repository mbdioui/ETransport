package tn.android.etransport.etransport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class Add_Transport2 extends Activity {
    private DatePicker simpleDatePicker;
    private Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add__transport2);
        SetUp();
        Intent intent = getIntent();
        Bundle B =intent.getExtras();
        if( B != null)
        {
            LatLng deslatlnt = (LatLng) B.get("Destination_Place");
            LatLng startlatlnt = (LatLng) B.get("Start_place");

        }
    }
    private void SetUp()
    {
        // initiate the date picker and a button
        simpleDatePicker = (DatePicker) findViewById(R.id.dates_transport);
        submit = (Button) findViewById(R.id.BTN_transport_done);
        // perform click event on submit button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the values for day of month , month and year from a date picker
                String day = "Day = " + simpleDatePicker.getDayOfMonth();
                String month = "Month = " + (simpleDatePicker.getMonth() + 1);
                String year = "Year = " + simpleDatePicker.getYear();
                // display the values by using a toast
                Toast.makeText(getApplicationContext(), day + "\n" + month + "\n" + year, Toast.LENGTH_LONG).show();
            }
        });
    }
}
