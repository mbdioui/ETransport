package tn.android.etransport.etransport;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import utils.Pager;

import static tn.android.etransport.etransport.R.id.tablayout;

/**
 * Created by mohamed salah on 08/11/2016.
 */

public class Add_Transport_affreteur_fragment extends Fragment implements TabLayout.OnTabSelectedListener {
    private Context activitycontext;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentActivity myContext;
    private AlertDialog spotdialog;

    public Context getActivitycontext() {
        return activitycontext;
    }

    public void setActivitycontext(Context activitycontext) {
        this.activitycontext = activitycontext;
    }
    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.transport_affreteur_layout, container, false);
        //Initializing the tablayout
        tabLayout = (TabLayout) v.findViewById(tablayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab(),0);
        tabLayout.addTab(tabLayout.newTab(),1);
        tabLayout.addTab(tabLayout.newTab(),2);
        tabLayout.addTab(tabLayout.newTab(),3);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);

        //Creating our pager adapter
        Pager adapter = new Pager(myContext.getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);
        tabLayout.setupWithViewPager(viewPager);

        return v;
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
