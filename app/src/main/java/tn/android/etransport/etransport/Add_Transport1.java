package tn.android.etransport.etransport;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import utils.Connectivity;
import utils.GPSTracker;
import utils.KeyboardUtil;
import utils.PermissionUtils;

import static android.support.design.widget.Snackbar.make;


public class Add_Transport1 extends Activity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMapClickListener {

    private final int REQUEST_location = 100;
    private final LatLngBounds BOUNDS = new LatLngBounds(
            new LatLng(25.57152,-4.50047), new LatLng(51.56979, 7.27688));
    private GoogleMap MapForm;
    private UiSettings mapUIsettings;
    private Marker startmarker;
    private Marker destinationmarker;
    private AutoCompleteTextView Autocomplete_place_field;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private Geocoder geocod; // get ltd,lgt from adress
    private String startposition="";
    private ImageButton cameraBTN;
    private String destposition="";
    private String Start_Country;
    private String Destination_Country;
    private ImageButton BTN_Addtransport1;
    public String getDestposition() {
        return destposition;
    }

    public void setDestposition(String destposition) {
        this.destposition = destposition;
    }

    public String getStartposition() {
        return startposition;
    }

    public void setStartposition(String startposition) {
        this.startposition = startposition;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add__transport);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.MapHere);
        cameraBTN= (ImageButton) findViewById(R.id.camera_position_BTN);
        BTN_Addtransport1 = (ImageButton) findViewById(R.id.add_transport1_BTN);
        cameraBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(destinationmarker!=null)
                    zoomOutFit();
            }
        });
        cameraBTN.setEnabled(false);
        BTN_Addtransport1.setEnabled(false);
        Autocomplete_place_field = (AutoCompleteTextView) findViewById(R.id.Autocomplete_places);
        //check connectivity
        if (Connectivity.Checkinternet(this))
            mapFragment.getMapAsync(this);
        else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("vous devez etre lié à internet pour effectuer cette opération");
            alertDialogBuilder.setPositiveButton("continuez", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS),0);
                }
            });
            alertDialogBuilder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        //check GPS //TODO demand access to location settings
//        if(!Connectivity.checkGPS(this))
//            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),11);
        //close button
        ImageButton close_btn=(ImageButton) findViewById(R.id.close_BTN);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = Add_Transport1.this;
                Intent intent= new Intent(activity,MainClient.class);
                startActivity(intent);
                activity.finish();
            }
        });
        //Passing to the second form of Adding transport.
        BTN_Addtransport1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transportPage2 = new Intent(Add_Transport1.this, Add_Transport2.class);
                if(destinationmarker!=null && startmarker != null) {
                    transportPage2.putExtra("Destination_Place", destinationmarker.getTitle());
                    transportPage2.putExtra("Start_place", startmarker.getTitle());
                    if(!startposition.equals(""))transportPage2.putExtra("Start_position",startposition);
                    if(!destposition.equals(""))transportPage2.putExtra("Dest_position",destposition);
                    transportPage2.putExtra("Destination_Country",Destination_Country);
                    transportPage2.putExtra("Start_Country",Start_Country);
                }

                startActivity(transportPage2);
            }
        });

//        MapForm.
    }



    //restart activity after activating connectivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0)
        {
            WifiManager wifiManager = (WifiManager)
                    getSystemService(Context.WIFI_SERVICE);
            if(Connectivity.Checkinternet(Add_Transport1.this))
            {
//                Intent intent1 = new Intent(getApplicationContext(),Add_Transport1.class);
//                Activity ac=getParent();
//                ac.finish();
//                startActivity(intent1);
                Intent intent1 = getIntent();
                finish();
                startActivity(intent1);
            }
            else
            {
                Activity activity = Add_Transport1.this;
                Intent intent= new Intent(activity,MainClient.class);
                startActivity(intent);
                activity.finish();
            }
        }
        else
            if(requestCode==11)
            {
                if(Connectivity.checkGPS(Add_Transport1.this))
                {
                    Intent intent1 = getIntent();
                    finish();
                    startActivity(intent1);
                }
                else
                {
                    Activity activity = Add_Transport1.this;
                    Intent intent= new Intent(activity,MainClient.class);
                    startActivity(intent);
                    activity.finish();
                }
            }
    }

    //Check internet connexion and GPS if it's ok go ahead else finish the activity
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (!PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION) &&
            ! PermissionUtils.isPermissionGranted(permissions,grantResults,Manifest.permission.ACCESS_COARSE_LOCATION)) {

                Intent i = new  Intent(Add_Transport1.this,MainClient.class);
                this.finish();
                startActivity(i);

        }
        else
        if (Connectivity.Checkinternet(this))
            setupmap();
    }


    @Override
    public void onMapReady(GoogleMap map) {
        // Assign the map on the local variable
        MapForm = map;

        if (PermissionUtils.isMarshMellow()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(Add_Transport1.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                        ,Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_location);
            }
            else
                setupmap();
        }
        else
        { map.setMyLocationEnabled(true);
         setupmap();}

    }

    private void setupmap() {


        String ville;
        GPSTracker gps = new GPSTracker(this);
        if (Connectivity.Checkinternet(this))
        {
            LatLng actuellocation = null;
            mapUIsettings = MapForm.getUiSettings();
            //trying to reach actuel location
            while(gps.getLatitude()==0 || gps.getLongitude()==0)
            {}
            if (gps.getLongitude()!=0 &&  gps.getLatitude()!=0)
            {
                actuellocation = new LatLng(gps.getLatitude(), gps.getLongitude());
                Address adr=null;
                Geocoder geo = new Geocoder(Add_Transport1.this);
                ville="non identifié";

                try {
                    List<Address> list = geo.getFromLocation(actuellocation.latitude,actuellocation.longitude,1);
                    if (list.size()>0 && list!=null) {
                        adr = list.get(0);
                        Start_Country = adr.getCountryName();
                        if (adr.getMaxAddressLineIndex()<=1)
                            ville=adr.getAddressLine(0);
                        else
                        {   startposition = adr.getAddressLine(0);
                            ville=adr.getAddressLine(1);
                        }
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                actuellocation = new LatLng(35.769231, 10.819741);
                ville="Monastir";
            }
            MapForm.moveCamera(CameraUpdateFactory.newLatLngZoom(actuellocation, 15));

            MarkerOptions startmarkerop = new MarkerOptions()
                    .title(ville)
                    .draggable(false)
                    .snippet("START LOCATION")
                    .position(actuellocation)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            startmarker = MapForm.addMarker(startmarkerop);}


        // enable zoom options & disable gesture iténeraire
        mapUIsettings.setZoomControlsEnabled(true);
        mapUIsettings.setZoomGesturesEnabled(true);
        mapUIsettings.setTiltGesturesEnabled(true);
        mapUIsettings.setScrollGesturesEnabled(true);
        MapForm.setOnMapClickListener(this);
        //check location option enabled
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            MapForm.setMyLocationEnabled(true);

        mGoogleApiClient = new GoogleApiClient.Builder(Add_Transport1.this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        Autocomplete_place_field.setThreshold(5);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS, null);
        Autocomplete_place_field.setAdapter(mPlaceArrayAdapter);
        Autocomplete_place_field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
                AutoCompleteTextView acomplete =(AutoCompleteTextView) v;
                acomplete.setHint("exemple : le Louvre");
            }
        });
        //change focus event
        Autocomplete_place_field.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                clear();
            }
        });
        //semi automatic place search
        Autocomplete_place_field.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            //choix d'emplacement
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
                final String placeId = String.valueOf(item.placeId);

                //
                Places.GeoDataApi.getPlaceById(mGoogleApiClient,placeId).setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getStatus().isSuccess()) {
                            final Place myPlace = places.get(0);
                            LatLng queriedLocation = myPlace.getLatLng();
                            geocod = new Geocoder(getApplicationContext());
                            try {
                                String Country = "";
                                String ville="inconnue";
                                Address location=null;
                                List<Address> add = geocod.getFromLocation(queriedLocation.latitude,queriedLocation.longitude,2);
                                if(add.size()>0)
                                {
                                    location = add.get(0);
                                    Destination_Country = location.getCountryName();
                                    if (location.getMaxAddressLineIndex()<=1)
                                        ville= location.getAddressLine(0);
                                    else
                                    {
                                        destposition= location.getAddressLine(0);
                                        ville = location.getAddressLine(1);
                                    }
                                    Country = location.getCountryName();
                                    if (Country.equals("Tunisie")||Country.equals("France")||Country.equals("Tunisia")) {
                                        LatLng ltlng = new LatLng(location.getLatitude(), location.getLongitude());
                                        if (destinationmarker != null)
                                             destinationmarker.remove();
                                         destinationmarker = MapForm.addMarker(new MarkerOptions()
                                                                .title(ville)
                                                                .snippet("destination place")
                                                                .position(ltlng)
                                                                .draggable(false)
                                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                                        cameraBTN.setEnabled(true);
                                        BTN_Addtransport1.setEnabled(true);
                                        zoomOutFit();
                                        Autocomplete_place_field.clearFocus();
                                        KeyboardUtil.hideKeyboard(Add_Transport1.this);
                                    }
                                    else
                                    {WrongPlace();}
                                }
                                else
                                        WrongPlace();
                            }
                            catch (IOException e)
                            {   e.printStackTrace();}


                        }
                        places.release();

                    }
                });
            }
        });
        //                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
//                        .getPlaceById(mGoogleApiClient, placeId);
//                geocod = new Geocoder(getApplicationContext());
//                //trying to reach place searched on map
//                try {
//                    Address location = null;
//                    List<Address> add = geocod.getFromLocationName(item.description.toString(), 10);
//                    if (add.size() > 0) {
//                        int i = 0;
//                        while (!add.get(i).hasLatitude() || !add.get(i).hasLongitude()) {
//                            i++;
//                        }
//                        if (i < add.size()) {
//                            location = add.get(i);
//                            Country = location.getCountryName();
//                        }
//                        if (Country.equals("Tunisie")||Country.equals("France")) {
//                            //check existance of the city
//
//                            LatLng ltlng = new LatLng(location.getLatitude(), location.getLongitude());
//                            if (destinationmarker != null)
//                                destinationmarker.remove();
//                            destinationmarker = MapForm.addMarker(new MarkerOptions()
//                                    .title(location.getLocality())
//                                    .snippet("destination place")
//                                    .position(ltlng)
//                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
//                            //		                .position(new LatLng(47.634654, 6.838812)));
//                            destinationmarker.setDraggable(true);
//                            positiondest = destinationmarker.getPosition();
//                            zoomOutFit();
//                            Autocomplete_place_field.clearFocus();
//                            KeyboardUtil.hideKeyboard(Add_Transport1.this);
//
//                        }
//                         else {
//                           WrongPlace();
//                        }
//                    }
//                    else
//                        WrongPlace();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

}

    private void WrongPlace() {
        KeyboardUtil.hideKeyboard(Add_Transport1.this);
        clear();
        Autocomplete_place_field.clearFocus();
//        Toast.makeText(Add_Transport1.this, getString(R.string.no_places_found), Toast.LENGTH_LONG).show();
        CoordinatorLayout coord= (CoordinatorLayout) findViewById(R.id.layout_add__transport);
        make(coord, R.string.no_places_found,Snackbar.LENGTH_SHORT).show();
    }

    //event clear text typing
    private void clear() {
        Autocomplete_place_field.setText("");
    }

    private void zoomOutFit() {
        if (startmarker != null && destinationmarker!=null) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            LatLng Latlng1 = new LatLng(startmarker.getPosition().latitude, startmarker.getPosition().longitude);
            LatLng Latlng2 = new LatLng(destinationmarker.getPosition().latitude, destinationmarker.getPosition().longitude);
            builder.include(Latlng1);
            builder.include(Latlng2);
            LatLngBounds bounds = builder.build();
            MapForm.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 180));
        }
    }

    public void removestartMarker() {
        if (startmarker != null) {
            startmarker.remove();
            startmarker = null;
        }
    }

    public void removedestMarker() {
        if (destinationmarker != null) {
            destinationmarker.remove();
            destinationmarker = null;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e("add_transport", "Google Places API connection suspended.");
        CoordinatorLayout coord = (CoordinatorLayout) findViewById(R.id.layout_add__transport);
        make(coord, R.string.suspended_connection, Snackbar.LENGTH_SHORT).show();
    }
    //  TODO progress dialog option while the process is running
    @Override
    public void onMapClick(LatLng point) {
            removedestMarker();
            Address adr;
            //Convert LatLng to Location
            Location location = new Location("Test");
            location.setLatitude(point.latitude);
            location.setLongitude(point.longitude);
            String ville = "inconnue";
            Geocoder geo = new Geocoder(getApplicationContext());
            if (Connectivity.Checkinternet(this)) {
                try {
                    List<Address> list = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 2);
                    if (list.size() > 0 && list != null) {
                        adr = list.get(0);
                        if (adr.getMaxAddressLineIndex() <= 1)
                            ville = adr.getAddressLine(0);
                        else {
                            destposition = adr.getAddressLine(0);
                            ville = adr.getAddressLine(1);
                            Destination_Country = adr.getCountryName();
                        }
//		        location.setTime(new Date().getTime()); //Set time as current Date
                        if (adr.getCountryName().equals("France") || adr.getCountryName().equals("Tunisie") || adr.getCountryName().equals("Tunisia"))
                        //Convert Location to LatLng
                        {
                            LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(newLatLng)
                                    .draggable(false)
                                    .title(ville)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            destinationmarker = MapForm.addMarker(markerOptions);
                            cameraBTN.setEnabled(true);
                            BTN_Addtransport1.setEnabled(true);
                            zoomOutFit();
                            Autocomplete_place_field.clearFocus();
                            KeyboardUtil.hideKeyboard(Add_Transport1.this);
                        } else {

//                    Toast.makeText(getApplicationContext(),getString(R.string.no_places_found), Toast.LENGTH_LONG).show();
                            CoordinatorLayout coord = (CoordinatorLayout) findViewById(R.id.layout_add__transport);
                            Snackbar snackbar = Snackbar.make(coord, R.string.no_places_found, Snackbar.LENGTH_LONG);
                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_gray));
                            TextView tv = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                            tv.setTextColor(Color.WHITE);
                            snackbar.show();
                        }
                    } else {
                        CoordinatorLayout coord = (CoordinatorLayout) findViewById(R.id.layout_add__transport);
                        Snackbar.make(coord, R.string.geting_position_error, Snackbar.LENGTH_LONG).show();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    CoordinatorLayout coord = (CoordinatorLayout) findViewById(R.id.layout_add__transport);
                    Snackbar snackbar = Snackbar.make(coord,"un erreur est survenue lors de la récupération de la position"
                            , Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_gray)); // snackbar background color
                    snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.White));
                    snackbar.show();
                }
            }

    }


//TODO disable GPS after exiting activity (Location Manager)
}