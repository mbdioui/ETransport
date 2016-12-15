package tn.android.etransport.etransport;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.github.clans.fab.FloatingActionButton;

import dmax.dialog.SpotsDialog;

public class Home_affreteur_fragment extends Fragment implements View.OnClickListener {
    private ImageButton profile_btn;
    private View action_bar_layout;
    private ActionBar actionbar;
    private FloatingActionButton addButton;
    private FloatingActionButton researchButton;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.acceuil_affreteur, container, false);
        profile_btn = (ImageButton) v.findViewById(R.id.profile_button);
        profile_btn.setOnClickListener(this);
        addButton = (FloatingActionButton) v.findViewById(R.id.fabAdd);
        researchButton = (FloatingActionButton) v.findViewById(R.id.fabResearch);
        addButton.setOnClickListener(this);
        researchButton.setOnClickListener(this);
        action_bar_layout = (View) getActivity().findViewById(R.id.my_toolbar);
        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.profile_button) {
            profile_update_fragment f = new profile_update_fragment();
            f.setActivitycontext(getActivity().getApplicationContext());
            FragmentTransaction FT = getFragmentManager().beginTransaction();
            FT.replace(R.id.fragment_affreteur, f);
            FT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            FT.addToBackStack(null);
            FT.commit();

            utils.ActionBar.changetextview(action_bar_layout, "mise à jour du profil");

        } else if (v.getId() == R.id.fabAdd) {

//            Add_Transport_affreteur_fragment f = new Add_Transport_affreteur_fragment();
//            f.setActivitycontext(getActivity().getApplicationContext());
//            FragmentTransaction FT = getFragmentManager().beginTransaction();
//            FT.replace(R.id.fragment_affreteur, f);
//            FT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//            FT.addToBackStack(null);
//            FT.commit();
//            utils.ActionBar.changetextview(action_bar_layout,"Ajout d'un trajet");

            AddTransportAffreteurTask task= new AddTransportAffreteurTask(this.getActivity());
//            task.setCntx(getContext());
            task.execute();

        }
        else if (v.getId()==R.id.fabResearch)
        {
            getActivity().finish();
            Intent researchintent= new Intent(getActivity(),CardView_activity.class);
            startActivity(researchintent);

        }
    }


    public class AddTransportAffreteurTask extends AsyncTask<String, String, String> {

        private AlertDialog progdialog;
        private Activity activityparent;

        public AddTransportAffreteurTask(Activity act) {
            super();
            progdialog = new SpotsDialog(act,R.style.CustomSpotDialog);
            activityparent = act;
        }

        @Override
        protected String doInBackground(String... params) {
            Intent i = new Intent(getActivity(), Add_Transport_affreteur.class);
            startActivity(i);
            return "";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progdialog.show();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progdialog.dismiss();
        }


    }

}