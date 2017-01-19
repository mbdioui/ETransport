package tn.android.etransport.etransport;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alertdialogpro.AlertDialogPro;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import utils.UserInfos;

/**
 * Created by mohamed salah on 01/11/2016.
 */

public class Home_affreteur_activity extends AppCompatActivity
{

    private View action_bar_layout;
    private AccountHeader headerResult = null;
    private ActionBar actionbar;

    public String getType() {
        return type;
    }

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.affreteur_layout);
        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Réinitialiser le mot de passe")
                .withIcon(R.drawable.ic_autorenew_white_24dp).withSelectable(false);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2)
                .withName("Se déconnecter").withIcon(R.drawable.deconnexion).withSelectable(false);
        //profile
        final IProfile profile = new ProfileDrawerItem()
                .withName(UserInfos.getConnecteduser().getF_name()+" "+UserInfos.getConnecteduser().getL_name())
                .withEmail(UserInfos.getConnecteduser().getMail())
                .withIcon(R.drawable.ic_account_circle_white_36dp)
                .withIdentifier(UserInfos.getConnecteduser().getId());
        //create the drawer and remember the `Drawer` result object
        action_bar_layout=setup_tab();
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withSelectionListEnabled(false)
                .withHeaderBackground(R.drawable.bg3)
                .addProfiles(
                        profile
                )
                .withSavedInstance(savedInstanceState)
                .build();

        final Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withSelectedItem(-1)
                .withTranslucentStatusBar(true)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1
                        ,item2
                )
                .withOnDrawerItemClickListener(clickitem)
                .build();

        if (this.getClass()==Home_affreteur_activity.class)
        {
            Home_affreteur_fragment f = new Home_affreteur_fragment();
            FragmentTransaction FT = getFragmentManager().beginTransaction();
            FT.replace(R.id.fragment_affreteur, f);
            FT.setTransition(FragmentTransaction.TRANSIT_NONE);
            FT.addToBackStack(null);
            FT.commit();
        }
        utils.ActionBar.changetextview(action_bar_layout,"Acceuil");

    }


    private void exit() {
        this.finish();
    }


    Drawer.OnDrawerItemClickListener clickitem= new Drawer.OnDrawerItemClickListener() {
        @Override
        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
            if (position ==1)
            {
                Intent forgetintent=new Intent(getApplication(),Forgetpass_activity.class);
                startActivity(forgetintent);
                exit();
            }
            else
            {
                AlertDialogPro.Builder alertDialog = new AlertDialogPro.Builder(Home_affreteur_activity.this);
                alertDialog.setTitle("Déconnexion ...")
                        .setMessage(getResources().getString(R.string.deconnexion))
                        .setIcon(R.drawable.deconnexion)
                        .setPositiveButton("OUI",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent connexIntent = new Intent(Home_affreteur_activity.this
                                                ,LoginActivity.class);
                                        startActivity(connexIntent);
                                        UserInfos.setConnecteduser(null);
                                        exit();
                                    }
                                })
                        .setNegativeButton("NON",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                        .show();
            }

            return true;
        }
    };

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.fragment_affreteur);
        if(currentFragment instanceof profile_update_fragment)
        {  Intent intent =getIntent();
           startActivity(intent);
           this.finish();
        }
        else
        {
            moveTaskToBack(true);
        }
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
}
