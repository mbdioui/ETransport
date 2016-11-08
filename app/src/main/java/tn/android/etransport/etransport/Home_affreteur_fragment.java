package tn.android.etransport.etransport;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class Home_affreteur_fragment extends Fragment implements View.OnClickListener {
    private ImageButton profile_btn;
    private View action_bar_layout;
    private ActionBar actionbar;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.acceuil_affreteur,container,false);
        profile_btn=(ImageButton) v.findViewById(R.id.profile_button);
        profile_btn.setOnClickListener(this);
        return v;
    }
    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.profile_button)
        {
            profile_update_fragment f = new profile_update_fragment();
            f.setActivitycontext(getActivity().getApplicationContext());
            FragmentTransaction FT = getFragmentManager().beginTransaction();
            FT.replace(R.id.fragment_affreteur, f);
            FT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            FT.addToBackStack(null);
            FT.commit();

            action_bar_layout = (View) getActivity().findViewById(R.id.my_toolbar);
            utils.ActionBar.changetextview(action_bar_layout,"mise à jour du profil ");

            //TODO issue action bar
//            actionbar =  getActivity().getActionBar();
//            actionbar.setDisplayHomeAsUpEnabled(true);

        }
    }


}
