package tn.android.etransport.etransport;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.FloatingActionButton;
import com.rey.material.widget.Spinner;

import utils.Links;
import utils.Pager;
import utils.UserInfos;

/**
 * Created by mohamed salah on 08/11/2016.
 */

public class Add_Transport_affreteur extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {
    private Context activitycontext;
    private Pager adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String date_depart_min;
    private String date_depart_max;
    private String date_arrive_min;
    private String date_arrive_max;
    private String date_depart;
    private String date_arrive;
    private AutoCompleteTextView startposition;
    private AutoCompleteTextView destposition;
    private FragmentActivity myContext;
    private int[] tabIcons = {
         R.drawable.ic_query_white,
         R.drawable.ic_room_white,
         R.drawable.ic_info_white,
         R.drawable.ic_image_white
    };
    private int pageSelected;
    private String startpositiontext;
    private String destpositiontext;

    public Context getActivitycontext() {
        return activitycontext;
    }

    public void setActivitycontext(Context activitycontext) {
        this.activitycontext = activitycontext;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transport_affreteur_layout);

        ShowManager();}


    private void ShowManager() {
        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab(),0);
        tabLayout.addTab(tabLayout.newTab(),1);
        tabLayout.addTab(tabLayout.newTab(),2);
        tabLayout.addTab(tabLayout.newTab(),3);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        //Creating our pager adapter
        adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());
        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
        ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                pageSelected = position;
                if (pageSelected==3) {
                //empty code
                }
            }
        };
        FloatingActionButton submitbutton = (FloatingActionButton) findViewById(R.id.submit_transport_btn);
        submitbutton.setOnClickListener(Add_Transport_affreteur.this);
        viewPager.setOnPageChangeListener(pageChangeListener);
        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }


//        ActionBar.changetextview(tabLayout,"Ajouter un trajet");
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);

    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(this,Home_affreteur_activity.class);
        startActivity(intent);
        Add_Transport_affreteur.this.finish();
    }

    @Override
    public void onClick(View v) {
        Boolean fieldsok=false;
        //non obligatory field
        MaterialEditText descriEditText= (MaterialEditText) findViewById(R.id.description_transport_affreteur);
        String string = descriEditText.getText().toString();

        if(((android.widget.Switch) findViewById(R.id.switch_date)).isChecked())
        {
            date_depart_min= ((EditText) findViewById(R.id.date_depart_min)).getText().toString();
            date_depart_max= ((EditText) findViewById(R.id.date_depart_max)).getText().toString();
            date_arrive_min= ((EditText) findViewById(R.id.date_arrive_min)).getText().toString();
            date_arrive_max= ((EditText) findViewById(R.id.date_arrive_max)).getText().toString();
        }
        else
        {
            date_depart=((EditText) findViewById(R.id.date_depart)).getText().toString();
            date_arrive=((EditText) findViewById(R.id.date_arrive)).getText().toString();
        }
        startposition = (AutoCompleteTextView) findViewById(R.id.start_place_autocomplete);
        destposition = (AutoCompleteTextView) findViewById(R.id.dest_place_autocomplete);
        startpositiontext = startposition.getText().toString();
        destpositiontext = destposition.getText().toString();
        Pager myadapter= ((Pager)viewPager.getAdapter());
        Form_Paths_Client positionfragment =(Form_Paths_Client) myadapter.getFragment(1);
        View positionfragmentview=positionfragment.getView();
        Marker startmarker= positionfragment.getStartmarker();
        Marker endmarker= positionfragment.getDestinationmarker();

        Spinner meansSpinner = (Spinner)findViewById(R.id.spinner_means);
        String means = meansSpinner.getSelectedItem().toString();
        Spinner typegoodsSpinner = (Spinner)findViewById(R.id.spinner_goods);
        String typegood = typegoodsSpinner.getSelectedItem().toString();
        if (((android.widget.Switch) findViewById(R.id.switch_date)).isChecked()==false)
        {
            if (date_depart.equals("") || date_arrive.equals(""))
            {
                viewPager.setCurrentItem(0);
                Toast.makeText(this,"Veuillez choisir les dates",Toast.LENGTH_LONG).show();
            }
            else if (startpositiontext.equals("") || destpositiontext.equals("")) {
                viewPager.setCurrentItem(1);
                Toast.makeText(this, "Veuillez choisir les adresses de départ et de destination", Toast.LENGTH_LONG).show();
            } else if (means.equals("---")) {
                viewPager.setCurrentItem(2);
                Toast.makeText(this, "Veuillez choisir votre moyen de transport", Toast.LENGTH_LONG).show();
            } else if (typegood.equals("---")) {
                viewPager.setCurrentItem(2);
                Toast.makeText(this, "Veuillez choisir votre type de marchandise", Toast.LENGTH_LONG).show();
            } else {
                fieldsok = true;
            }
        }
        else if(((android.widget.Switch) findViewById(R.id.switch_date)).isChecked()) {
            if ((date_depart_min.equals("") || date_depart_max.equals("") || date_arrive_min.equals("") || date_arrive_max.equals(""))) {
                viewPager.setCurrentItem(0);
                Toast.makeText(this, "Veuillez choisir les dates", Toast.LENGTH_LONG).show();
                date_depart="";
                date_arrive="";
            } else if (startpositiontext.equals("") || destpositiontext.equals("")) {
                viewPager.setCurrentItem(1);
                Toast.makeText(this, "Veuillez choisir les adresses de départ et de destination", Toast.LENGTH_LONG).show();
            } else if (means.equals("---")) {
                viewPager.setCurrentItem(2);
                Toast.makeText(this, "Veuillez choisir votre moyen de transport", Toast.LENGTH_LONG).show();
            } else if (typegood.equals("---")) {
                viewPager.setCurrentItem(2);
                Toast.makeText(this, "Veuillez choisir votre type de marchandise", Toast.LENGTH_LONG).show();
            } else {
                fieldsok = true;
            }
        }
        if(fieldsok)
        {
            InsertTransportTask task = new InsertTransportTask(Add_Transport_affreteur.this, getApplicationContext());
            task.setParentactivity(Add_Transport_affreteur.this);
            task.execute(Links.getRootFolder() + "inserttransport.php", String.valueOf(UserInfos.getConnecteduser().getId())
                    , date_depart_min, date_depart_max, date_arrive_min, date_arrive_max, string, startpositiontext,
                    destposition.getText().toString(), String.valueOf(startmarker.getPosition().latitude)
                    , String.valueOf(startmarker.getPosition().longitude), String.valueOf(endmarker.getPosition().latitude),
                    String.valueOf(endmarker.getPosition().longitude), date_depart, date_arrive, "1",means,typegood);
        }
    }
}
