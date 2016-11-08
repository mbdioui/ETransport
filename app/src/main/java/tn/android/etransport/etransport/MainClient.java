package tn.android.etransport.etransport;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import utils.UserInfos;

public class MainClient extends Activity  {
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private NavigationView nav;
    private DrawerLayout mDrawerLayout;
    private Boolean connected=false;
    private FloatingActionButton AddtransportBTN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_client);
        nav = (NavigationView) findViewById(R.id.naviguationView);
        if(UserInfos.IsConnected) {
            // TODO change naviguation view
            nav.getMenu().clear();
            nav.inflateMenu(R.menu.drawer_view_connected);
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mDrawerList = (ListView)findViewById(R.id.navList);
//        addDrawerItems();
        final BottomBar BB =(BottomBar) findViewById(R.id.Bottbar);
        BB.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch(tabId){
                case R.id.tab_action_settings :
                    Form_Settings_Client newFragment = new Form_Settings_Client();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.framefragment, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                        break;
                case R.id.tab_home_client:
                    Form_Home_Client f = new Form_Home_Client();
                    FragmentTransaction FT = getFragmentManager().beginTransaction();
                    FT.replace(R.id.framefragment, f);
                    FT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    FT.addToBackStack(null);
                    FT.commit();
                    break;
                case R.id.tab_Paths :
                    Form_Paths_Client fp = new Form_Paths_Client();
                    FragmentTransaction FT2 = getFragmentManager().beginTransaction();
                    FT2.replace(R.id.framefragment, fp);
                    FT2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    FT2.addToBackStack(null);
                    FT2.commit();
                    break;
                }
            }
        });
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {
                    case  R.id.nav_connexion:
                    Intent intent = new Intent(MainClient.this,LoginActivity.class);
                    startActivity(intent);
                    MainClient.this.finish();
                        break;
                    case  R.id.nav_user_inscription:
                    Intent intent2 = new Intent(MainClient.this,InsertUserActivity.class);
                    startActivity(intent2);
                    MainClient.this.finish();
                    break;
                    case R.id.nav_disconnect :
                    UserInfos.setConnecteduser(null);
                    UserInfos.IsConnected=false;
                    Intent currentintent = getIntent();
                    finish();
                    startActivity(currentintent);
                    break;
                    case R.id.nav_reset_password:
                    Intent intent_reset_password = new Intent(MainClient.this,Forgetpass_activity.class);
                    startActivity(intent_reset_password);
                    MainClient.this.finish();
                    break;
            }

                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    //    private void addDrawerItems() {
//        String[] osArray = { "S'authentifier", "S'inscrire", "Réinitialiser le mot de passe"};
//        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
//        mDrawerList.setAdapter(mAdapter);
//    }
}
