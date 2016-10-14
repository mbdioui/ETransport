package tn.android.etransport.etransport;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
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


public class Add_Transport1 extends Activity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMapClickListener, View.OnClickListener {

    private final int REQUEST_location = 100;
    private final LatLngBounds BOUNDS = new LatLngBounds(
            new LatLng(32.438329, 11.556740), new LatLng(48.40003249610686, -4.625244140625));
    private GoogleMap mMap;
    private UiSettings mapUIsettings;
    private Marker startmarker;
    private Marker destinationmarker;
    private AutoCompleteTextView Autocomplete_place_field;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private Geocoder geocod; // get ltd,lgt from adress
    private LatLng positiondest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add__transport);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.MapHere);
        //TODO check connectivity to internet
        if (isNetworkAvailable())
            mapFragment.getMapAsync(this);
        else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("vous devez etre lié à internet pour effectuer cette opération");
            alertDialogBuilder.setPositiveButton("continuez", null);
            alertDialogBuilder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
        }
        ImageButton BTN = (ImageButton) findViewById(R.id.add_transport1_BTN);
        BTN.setOnClickListener(this);

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (!PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (isChild()) {
                Intent i = new Intent(Add_Transport1.this, getParent().getClass());
                startActivity(i);
                this.finish();
            } else
                finish();
        } else if (Connectivity.Checkinternet(this))
            setupmap();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        if (PermissionUtils.canMakeSmores()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(Add_Transport1.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_location);
            else
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_location);
        } else
            map.setMyLocationEnabled(true);
        GPSTracker gps = new GPSTracker(this);
        if (Connectivity.Checkinternet(this)) {
            Location loc=gps.getLocation();
            LatLng actuellocation;
            mapUIsettings = map.getUiSettings();
            //		LatLng école = new LatLng(47.6410701, 6.844562);
            if (loc!=null)
                actuellocation = new LatLng(loc.getLatitude(), loc.getLongitude());
            else
                actuellocation = new LatLng(35.769233,10.819574);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(actuellocation, 13));

            MarkerOptions startmarkerop = new MarkerOptions()
                    .title("Location")
                    .draggable(true)
                    .snippet("START LOCATION")
                    .position(actuellocation)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            startmarker = map.addMarker(startmarkerop);
            startmarker.setDraggable(true);
        }
    }

    private void setupmap() {
        // enable zoom options & disable gesture iténeraire
        mapUIsettings.setZoomControlsEnabled(true);
        mapUIsettings.setZoomGesturesEnabled(true);
        mapUIsettings.setTiltGesturesEnabled(false);
        mMap.setOnMapClickListener(this);
        //check location option enabled
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            mMap.setMyLocationEnabled(true);

        mGoogleApiClient = new GoogleApiClient.Builder(Add_Transport1.this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        Autocomplete_place_field = (AutoCompleteTextView) findViewById(R.id.Autocomplete_places);
        Autocomplete_place_field.setThreshold(3);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS, null);
        Autocomplete_place_field.setAdapter(mPlaceArrayAdapter);
        Autocomplete_place_field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });

        //semi automatic place search
        Autocomplete_place_field.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //choix d'emplacement
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
                final String placeId = String.valueOf(item.placeId);
                String Country = "";
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                geocod = new Geocoder(getApplicationContext());
                
                try {
                    Address location = null;
                    List<Address> add = geocod.getFromLocationName(item.description.toString(), 10);
                    if (add.size() > 0) {
                        int i = 0;
                        while (!add.get(i).hasLatitude() || !add.get(i).hasLongitude()) {
                            i++;
                        }
                        if (i < add.size()) {
                            location = add.get(i);
                            Country = location.getCountryName();
                        }
                        if (Country.equals("Tunisia")||Country.equals("France")) {
                            //check existance of the city

                            LatLng ltlng = new LatLng(location.getLatitude(), location.getLongitude());
                            if (destinationmarker != null)
                                destinationmarker.remove();
                            destinationmarker = mMap.addMarker(new MarkerOptions()
                                    .title(location.getLocality())
                                    .snippet("destination place")
                                    .position(ltlng)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                            //		                .position(new LatLng(47.634654, 6.838812)));
                            destinationmarker.setDraggable(true);
                            positiondest = destinationmarker.getPosition();
                            zoomOutFit();
                            Autocomplete_place_field.clearFocus();
                            KeyboardUtil.hideKeyboard(Add_Transport1.this);

                        }
                         else {
                            Toast.makeText(Add_Transport1.this, getString(R.string.no_places_found), Toast.LENGTH_LONG).show();
                            clear();
                            Autocomplete_place_field.clearFocus();

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        //event on change focus
        Autocomplete_place_field.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    v.clearFocus();
                else
                    clear();
            }
        });
    }

    //event clear text typing
    private void clear() {
        Autocomplete_place_field.setText("");
        Autocomplete_place_field.requestFocus();
    }

    private void zoomOutFit() {
        if (startmarker != null) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            LatLng Latlng1 = new LatLng(startmarker.getPosition().latitude, startmarker.getPosition().longitude);
            LatLng Latlng2 = new LatLng(destinationmarker.getPosition().latitude, destinationmarker.getPosition().longitude);
            builder.include(Latlng1);
            builder.include(Latlng2);
            LatLngBounds bounds = builder.build();
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 180));
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
    }

    @Override
    public void onMapClick(LatLng point) {

        removedestMarker();
        Address adr;
        //Convert LatLng to Location
        Location location = new Location("Test");
        location.setLatitude(point.latitude);
        location.setLongitude(point.longitude);
        Geocoder geo = new Geocoder(getApplicationContext());
        try {
            List<Address> list=geo.getFromLocation(location.getLatitude(),location.getLongitude(),2);
            if (list.size()>0 && list!=null) {
              adr = list.get(0);

//		  location.setTime(new Date().getTime()); //Set time as current Date
                if (adr.getCountryName().equals("France")||adr.getCountryName().equals("Tunisia"))
                    //Convert Location to LatLng
                { LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                         MarkerOptions markerOptions = new MarkerOptions()
                        .position(newLatLng)
                        .title(adr.getLocality());
                destinationmarker = mMap.addMarker(markerOptions);}
                else
                {
                    Toast.makeText(getApplicationContext(),getString(R.string.no_places_found), Toast.LENGTH_LONG).show();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        Intent transportPage2 = new Intent(Add_Transport1.this, Add_Transport2.class);
        if(destinationmarker!=null)
        transportPage2.putExtra("Destination_Place", destinationmarker.getPosition());
        transportPage2.putExtra("Start_place",startmarker.getPosition());
        startActivity(transportPage2);
    }
}