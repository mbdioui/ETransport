package tn.android.etransport.etransport;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.google.android.gms.maps.MapFragment;
//import com.google.android.gms.maps.SupportMapFragment;

public class Form_Home_Client extends Fragment {
//    private SupportMapFragment mSupportMapFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_form_home_client,container,false);
//        MapFragment f = new MapFragment();
//        FragmentTransaction FT = getFragmentManager().beginTransaction();
//        FT.replace(R.id.MapHere, f);
//        FT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        FT.addToBackStack(null);
//        FT.commit();
        return v;
}
}
