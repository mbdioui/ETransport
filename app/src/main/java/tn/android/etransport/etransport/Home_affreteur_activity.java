package tn.android.etransport.etransport;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import utils.UserInfos;

/**
 * Created by mohamed salah on 01/11/2016.
 */

public class Home_affreteur_activity extends AppCompatActivity implements View.OnClickListener
        , NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ImageButton btn_naviguation_view;
    private TextView header_nav_mail;
    private TextView header_nav_username;
    private ImageButton profileBTN;
    private View action_bar_layout;
    private ActionBar actionbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.affreteur_layout);

        action_bar_layout = setup_tab();

        drawer =(DrawerLayout) findViewById(R.id.drawer_layout_affreteur);

        btn_naviguation_view =(ImageButton) action_bar_layout.findViewById(R.id.btn_naviguation_view);
        btn_naviguation_view.setOnClickListener(this);

        navigationView =(NavigationView) findViewById(R.id.navig_View);
        navigationView.setNavigationItemSelectedListener(Home_affreteur_activity.this);

        View header=navigationView.getHeaderView(0);
        header_nav_mail=(TextView) header.findViewById(R.id.Mail_user_head);
        header_nav_username=(TextView) header.findViewById(R.id.User_name_head);
        setupheader_nav_view();

        Home_affreteur_fragment f = new Home_affreteur_fragment();
        FragmentTransaction FT = getFragmentManager().beginTransaction();
        FT.replace(R.id.fragment_affreteur, f);
        FT.setTransition(FragmentTransaction.TRANSIT_NONE);
        FT.addToBackStack(null);
        FT.commit();
        utils.ActionBar.changetextview(action_bar_layout,"Acceuil");
    }

    private View setup_tab() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        actionbar= getSupportActionBar();
        actionbar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_layout = inflater.inflate(R.layout.custom_action_bar, null);
        actionbar.setCustomView(action_bar_layout, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        return action_bar_layout;
    }


    private void setupheader_nav_view()
    {
        header_nav_mail.setText(UserInfos.getConnecteduser().getMail().toString());
        header_nav_username.setText(UserInfos.getConnecteduser().getF_name().toString()+" "+UserInfos.getConnecteduser().getL_name().toString());
    }



    @Override
    public void onClick(View v) {
        int K=v.getId();
        if (v.getId()==R.id.btn_naviguation_view)
        {
            drawer.openDrawer(navigationView);
            drawer.bringToFront();
        }
    }


    private void exit() {
        this.finish();
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
       int id = item.getItemId();
        switch (id)
                {
                case R.id.nav_reset_password:
                Intent forgetintent=new Intent(getApplication(),Forgetpass_activity.class);
                startActivity(forgetintent);
                exit();
                    break;
                case R.id.nav_disconnect:

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home_affreteur_activity.this,R.style.dialog_theme);
                alertDialog.setTitle("Déconnexion ...");// Le titre
                alertDialog.setMessage(getResources().getString(R.string.deconnexion));// Le message
                alertDialog.setIcon(R.drawable.deconnexion);// L'icone

                alertDialog.setPositiveButton("OUI",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent connexIntent = new Intent(Home_affreteur_activity.this
                                        ,LoginActivity.class);
                                startActivity(connexIntent);
                                exit();
                            }
                        });
                alertDialog.setNegativeButton("NON",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
                    break;
                }
            return true;
        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=getIntent();
        startActivity(intent);
    }

}
