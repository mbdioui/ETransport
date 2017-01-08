package tn.android.etransport.etransport;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;

import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


public class Form_Pict_Client extends android.support.v4.app.Fragment implements View.OnClickListener {


    private BootstrapButton galeriebtn;
    private final int numberOfImagesToSelect = 3;
    private final int Galeriepermission= 5656;
    private HashMap<String,String> savedImages;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
         if (requestCode == Galeriepermission && resultCode == RESULT_OK && data != null)
        {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            Toast.makeText(getContext(),images.get(0).path,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_form_pict_client,container,false);
        confilay(v);
        return v;
    }

    private void confilay(View v) {
        galeriebtn =(BootstrapButton) v.findViewById(R.id.button_galerie);
        galeriebtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.button_galerie)
        {
            Intent intent = new Intent(getActivity(), AlbumSelectActivity.class);
            //set limit on number of images that can be selected, default is 10
            intent.putExtra(Constants.INTENT_EXTRA_LIMIT, numberOfImagesToSelect);
            startActivityForResult(intent,Galeriepermission);

        }
    }
}
