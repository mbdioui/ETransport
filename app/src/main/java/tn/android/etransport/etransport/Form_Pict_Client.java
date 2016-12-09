package tn.android.etransport.etransport;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.ImageButton;


public class Form_Pict_Client extends android.support.v4.app.Fragment {


    private ImageButton Camera_BTN;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_form_pict_client,container,false);
        return v;
    }

}
