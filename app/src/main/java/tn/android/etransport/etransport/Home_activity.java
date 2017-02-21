package tn.android.etransport.etransport;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alertdialogpro.AlertDialogPro;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;

import tn.android.etransport.etransport.FireBaseNotification.RegistrationIntentService;
import utils.Links;
import utils.PermissionUtils;
import utils.UserInfos;

/**
 * Created by mohamed salah on 01/11/2016.
 */

public class Home_activity extends AppCompatActivity {

    private View action_bar_layout;
    private AccountHeader headerResult = null;
    private ActionBar actionbar;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private final int PhoneState =111;


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
        //
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName("Trajets en cours"
        ).withIcon(android.R.drawable.ic_dialog_info).withSelectable(false);
        //profile
        IProfile profile = new ProfileDrawerItem()
                .withName(UserInfos.getConnecteduser().getF_name() + " " + UserInfos.getConnecteduser().getL_name())
                .withEmail(UserInfos.getConnecteduser().getMail())
                .withIcon(R.drawable.ic_account_circle_white_36dp)
                .withIdentifier(UserInfos.getConnecteduser().getId());

        if (!UserInfos.getConnecteduser().getUser_picture().equals("null")) {
            DrawerImageLoader.init(new AbstractDrawerImageLoader() {
                @Override
                public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                    Glide.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
                }

                @Override
                public void cancel(ImageView imageView) {
                    Glide.clear(imageView);
                }

                @Override
                public Drawable placeholder(Context ctx, String tag) {
                    //define different placeholders for different imageView targets
                    //default tags are accessible via the DrawerImageLoader.Tags
                    //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
                    if (DrawerImageLoader.Tags.PROFILE.name().equals(tag)) {
                        return DrawerUIUtils.getPlaceHolder(ctx);
                    } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name().equals(tag)) {
                        return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(com.mikepenz.materialdrawer.R.color.primary).sizeDp(56);
                    } else if ("customUrlItem".equals(tag)) {
                        return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(R.color.md_red_500).sizeDp(56);
                    }

                    //we use the default one for
                    //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()

                    return super.placeholder(ctx, tag);
                }
            });
            profile.withIcon(Links.getProfilePictures() + UserInfos.getConnecteduser().getUser_picture());
        }        //create the drawer and remember the `Drawer` result object
        action_bar_layout = setup_tab();
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
                        , item2
                        , new DividerDrawerItem()
                        , item3
                )
                .withOnDrawerItemClickListener(clickitem)
                .build();

        if (this.getClass() == Home_activity.class) {
            Home_fragment f = new Home_fragment();
            FragmentTransaction FT = getFragmentManager().beginTransaction();
            FT.replace(R.id.fragment_affreteur, f);
            FT.setTransition(FragmentTransaction.TRANSIT_NONE);
            FT.addToBackStack(null);
            FT.commit();
        }
        utils.ActionBar.changetextview(action_bar_layout, "Acceuil");
        if (checkPlayServices()) {
            if(Build.VERSION.SDK_INT< Build.VERSION_CODES.M)
            { sendtoken();}
            else
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.READ_PHONE_STATE}
                        , PhoneState);
            else
                sendtoken();

        }

    }

    private void sendtoken()
    {// Start IntentService to register this application with GCM.
        Intent intent = new Intent(getApplication(), RegistrationIntentService.class);
        startService(intent);
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
            else if (position == 2)
            {
                AlertDialogPro.Builder alertDialog = new AlertDialogPro.Builder(Home_activity.this);
                alertDialog.setTitle("Déconnexion ...")
                        .setMessage(getResources().getString(R.string.deconnexion))
                        .setIcon(R.drawable.deconnexion)
                        .setPositiveButton("OUI",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent connexIntent = new Intent(Home_activity.this
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
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M ) {
            if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.READ_PHONE_STATE)
                    && requestCode == PhoneState)
                sendtoken();

        }
    }

}
