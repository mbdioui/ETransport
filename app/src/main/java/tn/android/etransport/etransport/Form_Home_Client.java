package tn.android.etransport.etransport;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.roughike.bottombar.BottomBar;

import utils.UserInfos;

public class Form_Home_Client extends Fragment {
    private FloatingActionButton Add_transport_BTN;
    private BottomBar BB;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_form_home_client,container,false);
        Add_transport_BTN = (FloatingActionButton) v.findViewById(R.id.button_transport);
        if(UserInfos.getConnecteduser()==null) {
            Add_transport_BTN.setEnabled(false);
            Add_transport_BTN.setBackgroundTintList(getResources().getColorStateList(R.color.Gray));
        }
        Add_transport_BTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(Form_Home_Client.this.getActivity(),Add_Transport1.class);
                startActivity(i);
                Form_Home_Client.this.getActivity().finish();
            }
        });
        BB= (BottomBar) v.findViewById(R.id.Bottbar);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NestedScrollView myScrollView =(NestedScrollView) view.findViewById(R.id.ScrollViewNested);
        myScrollView.setSmoothScrollingEnabled(true);
        myScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                 if(scrollY >=00 && scrollY< 70)
                 {
                     if (!Add_transport_BTN.isShown())
                     {
                         Add_transport_BTN.clearAnimation();
                         Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);
                         Add_transport_BTN.startAnimation(animation);
                         Add_transport_BTN.setVisibility(View.VISIBLE);
                     }
                 }
                 else
                 {
                     if (Add_transport_BTN.isShown()) {
                         Add_transport_BTN.clearAnimation();
                         Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.out_animation);
                         Add_transport_BTN.startAnimation(animation);
                         Add_transport_BTN.setVisibility(View.INVISIBLE);
                     }
                 }
            }
        });
    }




}
