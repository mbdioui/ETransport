package tn.android.etransport.etransport;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Beans.Transport;
import utils.Links;

import static tn.android.etransport.etransport.R.id.slider;

public class Detail_transport_item extends Activity implements OnMapReadyCallback {

    private GoogleMap MapForm;
    private Transport transport;
    private Marker StartMarker;
    private Marker EndMarker;
    private MapFragment mapFragment;

    private AwesomeTextView datepublishView;
    private AwesomeTextView dategomin;
    private AwesomeTextView dategomax;
    private AwesomeTextView datearrivemax;

    private SliderLayout sliderShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transport_item_details);
        sliderShow = (SliderLayout) findViewById(slider);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.itemheaderdetail);
        mapFragment.getMapAsync(this);
        Bundle b=getIntent().getExtras();
        String st= b.get("item").toString();
        JSONObject obj= null;
        try {
            obj = new JSONObject(st);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        transport=new Transport(obj,2);
        sliderconfig();
        configlayout();
    }

    private void sliderconfig()  {
        DefaultSliderView SliderView = new DefaultSliderView(this);

        if(transport.getTransport_picture_1()==null&&transport.getTransport_picture_2()==null &&transport.getTransport_picture_3()==null)
        {
            SliderView.image("http://cloud.leportail.ci/default.png");
            sliderShow.addSlider(SliderView);
        }
        else
        {
            List<String> pictures= new ArrayList<>();
            if (transport.getTransport_picture_1()!=null)
                pictures.add(transport.getTransport_picture_1());
            if (transport.getTransport_picture_2()!=null)
                pictures.add(transport.getTransport_picture_2());
            if (transport.getTransport_picture_3()!=null)
                pictures.add(transport.getTransport_picture_3());
            for(int i=0;i<pictures.size();i++)
            {
                if (!pictures.get(i).equals(""))
                {
                    SliderView = new DefaultSliderView(this);
                    SliderView.image(Links.getPicFolder()+pictures.get(i));
                    sliderShow.addSlider(SliderView);
                }
            }
        }
        sliderShow.startAutoCycle();
    }

    private void configlayout()
    {
        datepublishView =(AwesomeTextView) findViewById(R.id.detail_datepublish);
        dategomin=(AwesomeTextView) findViewById(R.id.detail_dategomin);
        dategomax=(AwesomeTextView) findViewById(R.id.detail_dategomax);
        datearrivemax =(AwesomeTextView) findViewById(R.id.detail_datearrivemax);

        fillblanks();
    }

    private void fillblanks() {

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);

        if(transport.getTransport_date_add()!=null)
            datepublishView.setText(String.valueOf(dateFormatter.format(transport.getTransport_date_add())));
        if (transport.getTransport_date_go()!=null)
        {
            dategomin.setText(String.valueOf(dateFormatter.format(transport.getTransport_date_go())));
            dategomax.setText(String.valueOf(dateFormatter.format(transport.getTransport_date_go())));
        }
        else
        {
            dategomin.setText(String.valueOf(dateFormatter.format(transport.getTransport_date_go_min())));
            dategomax.setText(String.valueOf(dateFormatter.format(transport.getTransport_date_go_max())));
        }
        if(transport.getTransport_date_arrival_max()!=null)
            datearrivemax.setText(String.valueOf(dateFormatter.format(transport.getTransport_date_arrival_max())));
        else
            datearrivemax.setText(String.valueOf(dateFormatter.format(transport.getTransport_date_arrival())));
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        overridePendingTransition(R.anim.zoom_in, R.anim.out_animation);
        this.finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapForm = googleMap;
        MapForm.getUiSettings().setMapToolbarEnabled(false);
        if (transport.getStart_pos_lat()!=0 && transport.getStart_pos_lgt()!=0
                && transport.getEnd_pos_lat()!=0&& transport.getStart_pos_lgt()!=0) {
            LatLng startLatLng = new LatLng(transport.getStart_pos_lat(), transport.getStart_pos_lgt());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(startLatLng)
                    .title("position de départ")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            StartMarker = MapForm.addMarker(markerOptions);
            StartMarker.setVisible(true);
            LatLng endLatLng = new LatLng(transport.getEnd_pos_lat(), transport.getEnd_pos_lgt());
            MarkerOptions markerOptions2 = new MarkerOptions()
                    .position(endLatLng)
                    .title("Position d\'arrivée")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            EndMarker = MapForm.addMarker(markerOptions2);
            EndMarker.setVisible(true);
            MapForm.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    zoomout();
                }
            });

        }
        else
        {
            mapFragment.getView().setVisibility(View.GONE);
            mapFragment.getView().startAnimation(AnimationUtils.loadAnimation(this,R.anim.zoom_out));
        }
    }


    private void zoomout()
    {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        LatLng Latlng1 = new LatLng(StartMarker.getPosition().latitude, StartMarker.getPosition().longitude);
        LatLng Latlng2 = new LatLng(EndMarker.getPosition().latitude, EndMarker.getPosition().longitude);
        builder.include(Latlng1);
        builder.include(Latlng2);
        LatLngBounds bounds = builder.build();
        MapForm.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,70));
    }
    @Override
    protected void onStop() {
        sliderShow.stopAutoCycle();
        super.onStop();
    }
}
