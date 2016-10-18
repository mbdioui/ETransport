package tn.android.etransport.etransport;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Form_Home_Client extends Fragment  {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_form_home_client,container,false);
        FloatingActionButton Add_transport_BTN = (FloatingActionButton) v.findViewById(R.id.button_transport);
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

        return v;
    }

}
