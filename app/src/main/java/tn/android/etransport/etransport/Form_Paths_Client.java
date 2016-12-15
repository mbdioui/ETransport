package tn.android.etransport.etransport;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
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
import com.rey.material.widget.ImageButton;

import java.io.IOException;
import java.util.List;

import utils.AlertDialogCustom;
import utils.Connectivity;
import utils.GPSTracker;
import utils.KeyboardUtil;
import utils.PermissionUtils;

import static android.app.Activity.RESULT_OK;

public class Form_Paths_Client extends android.support.v4.app.Fragment implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMapClickListener {

    private BootstrapButton startmarkerBTN;
    private BootstrapButton endmarkerBTN;
    private android.widget.Switch toggle_start;
    private ImageButton resizemap;
    private AutoCompleteTextView startposition_autocomplete;
    private AutoCompleteTextView destposition_autocomplete;
    private GoogleMap MapForm;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private Geocoder geocod;
    private UiSettings mapUIsettings;
    private final int REQUEST_location = 100;

    public Marker getStartmarker() {
        return startmarker;
    }

    public void setStartmarker(Marker startmarker) {
        this.startmarker = startmarker;
    }

    public Marker getDestinationmarker() {
        return destinationmarker;
    }

    public void setDestinationmarker(Marker destinationmarker) {
        this.destinationmarker = destinationmarker;
    }

    public String getStart_Country() {
        return Start_Country;
    }

    public void setStart_Country(String start_Country) {
        Start_Country = start_Country;
    }

    public String getDestination_Country() {
        return Destination_Country;
    }

    public void setDestination_Country(String destination_Country) {
        Destination_Country = destination_Country;
    }

    private final LatLngBounds BOUNDS = new LatLngBounds(
            new LatLng(25.57152, -4.50047), new LatLng(51.56979, 7.27688));
    private Marker startmarker;
    private Marker destinationmarker;
    private android.widget.Switch toggle_end;
    private String startposition;
    private String Start_Country;
    private String destposition="";
    private String Destination_Country;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_form_paths_client, container, false);
        settup(v);
        return v;
    }

    private void settup(View view) {
        toggle_start = (android.widget.Switch) view.findViewById(R.id.toggle_marker_start);
        toggle_end = (android.widget.Switch) view.findViewById(R.id.toggle_marker_end);
        resizemap = (ImageButton) view.findViewById(R.id.BTN_resizemap);
        startposition_autocomplete = (AutoCompleteTextView) view.findViewById(R.id.start_place_autocomplete);
        destposition_autocomplete = (AutoCompleteTextView) view.findViewById(R.id.dest_place_autocomplete);
        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.MapHere_affreteur);
        if (Connectivity.Checkinternet(getActivity()))
            mapFragment.getMapAsync(this);
        else
            AlertDialogCustom.show(getContext(), "Vous devez etre lié a internet");


        //select Start position Marker
        toggle_start.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    if (toggle_end.isChecked())
                        toggle_end.setChecked(false);
            }
        });
        //select End position Marker
        toggle_end.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    if (toggle_start.isChecked())
                        toggle_start.setChecked(false);
            }
        });
        resizemap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomOutFit();
            }
        });

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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getContext(),
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapClick(LatLng point) {
        if(!toggle_end.isChecked() && !toggle_start.isChecked())
            Toast.makeText(getActivity(),"cochez un marqueur",Toast.LENGTH_LONG).show();
        else if(toggle_start.isChecked())
        {
           placestartposition(point);
        }
        else if (toggle_end.isChecked())
        {
            placeendposition(point);
        }
    }

    private void placeendposition(LatLng point) {
        removedestMarker();
        clear(destposition_autocomplete);
        Address adr;
        //Convert LatLng to Location
        Location location = new Location("Test");
        location.setLatitude(point.latitude);
        location.setLongitude(point.longitude);
        String ville = "inconnue";
        Geocoder geo = new Geocoder(getActivity());
        if (Connectivity.Checkinternet(getActivity())) {
            try {
                List<Address> list = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 2);
                if (list.size() > 0 && list != null) {
                    adr = list.get(0);
                    if (adr.getMaxAddressLineIndex() <= 1)
                        if(adr.getAddressLine(0).contains(" "))
                            ville = adr.getAddressLine(0).split(" ")[1];
                        else
                            ville = adr.getAddressLine(0);
                    else {
                        destposition = adr.getAddressLine(0);
                        if(adr.getAddressLine(1).contains(" "))
                            ville = adr.getAddressLine(1).split(" ")[1];
                        else
                            ville = adr.getAddressLine(1);
                        Destination_Country= adr.getCountryName();
                    }
                    if (adr.getCountryName().equals("France") || adr.getCountryName().equals("Tunisie") || adr.getCountryName().equals("Tunisia"))
                    //Convert Location to LatLng
                    {
                        LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(newLatLng)
                                .draggable(true)
                                .title(ville)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        destinationmarker= MapForm.addMarker(markerOptions);
                        zoomOutFit();
                        KeyboardUtil.hideKeyboard(getActivity());
                        if(!destposition_autocomplete.getText().equals(""))
                        {
                            destposition="";
                        }
                        for (int i=0;i<adr.getMaxAddressLineIndex();i++) {
                            if (!adr.getAddressLine(i).equals("Unnamed Road"))
                                destposition= destposition.concat(", "+adr.getAddressLine(i));
                        }
                        if (!destposition.equals(""))
                        {
                            destposition=destposition.substring(1);
                            destposition_autocomplete.setText(destposition);
                        }
                        destposition_autocomplete.dismissDropDown();
                    } else {

                        Toast.makeText(getActivity(),getString(R.string.no_places_found), Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(getActivity(),getString(R.string.no_places_found), Toast.LENGTH_LONG).show();


                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),"un erreur est survenue lors de la récupération de la position"
                        , Toast.LENGTH_LONG).show();

            }
        }
    }

    private void placestartposition(LatLng point) {
        removestartMarker();
        clear(startposition_autocomplete);
        Address adr;
        //Convert LatLng to Location
        Location location = new Location("Test");
        location.setLatitude(point.latitude);
        location.setLongitude(point.longitude);
        String ville = "inconnue";
        Geocoder geo = new Geocoder(getActivity());
        if (Connectivity.Checkinternet(getActivity())) {
            try {
                List<Address> list = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 2);
                if (list.size() > 0 && list != null) {
                    adr = list.get(0);
                    if (adr.getMaxAddressLineIndex() <= 1)
                    {
                        if(adr.getAddressLine(0).contains(" "))
                            ville = adr.getAddressLine(0).split(" ")[1];
                        else
                            ville = adr.getAddressLine(0);
                    }
                    else {
                        if(adr.getAddressLine(1).contains(" "))
                            ville = adr.getAddressLine(1).split(" ")[1];
                        else
                            ville = adr.getAddressLine(1);
                        Start_Country= adr.getCountryName();
                    }
//		        location.setTime(new Date().getTime()); //Set time as current Date
                    if (adr.getCountryName().equals("France") || adr.getCountryName().equals("Tunisie") || adr.getCountryName().equals("Tunisia"))
                    //Convert Location to LatLng
                    {
                        LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(newLatLng)
                                .draggable(true)
                                .title(ville)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                        startmarker= MapForm.addMarker(markerOptions);
                        zoomOutFit();
                        KeyboardUtil.hideKeyboard(getActivity());
                        if(!startposition_autocomplete.getText().equals(""))
                        {
                            startposition="";
                        }
                        for (int i=0;i<adr.getMaxAddressLineIndex();i++) {
                            if (!adr.getAddressLine(i).equals("Unnamed Road"))
                                startposition= startposition.concat(", "+adr.getAddressLine(i));
                        }
                        if (!startposition.equals(""))
                        {
                            startposition=startposition.substring(1);
                            startposition_autocomplete.setText(startposition);
                        }
                        startposition_autocomplete.dismissDropDown();
                    } else {

                        Toast.makeText(getActivity(),getString(R.string.no_places_found), Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(getActivity(),getString(R.string.no_places_found), Toast.LENGTH_LONG).show();


                }
            } catch (IOException e)
                {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"un erreur est survenue lors de la récupération de la position"
                    , Toast.LENGTH_LONG).show();
                }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapForm = googleMap;
        if (PermissionUtils.isMarshMellow()) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_location);
            } else {
                setupmap();
                googleMap.setMyLocationEnabled(true);
            }
        } else {
            googleMap.setMyLocationEnabled(true);
            setupmap();
        }
        MapForm.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                //empty code
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                //empty code
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                if (marker.hashCode()==startmarker.hashCode())
                {
                    placestartposition(marker.getPosition());
                }
                else if (marker.hashCode()==destinationmarker.hashCode())
                {
                    placeendposition(marker.getPosition());
                }
            }
        });

    }

    private void setupmap() {
        mapUIsettings = MapForm.getUiSettings();
        // enable zoom options & disable gesture iténeraire
        mapUIsettings.setZoomControlsEnabled(true);
        mapUIsettings.setZoomGesturesEnabled(true);
        mapUIsettings.setTiltGesturesEnabled(true);
        mapUIsettings.setScrollGesturesEnabled(true);
        MapForm.setOnMapClickListener(this);

        String ville;
        GPSTracker gps = new GPSTracker(getActivity());
        if (Connectivity.Checkinternet(getActivity())) {
            LatLng actuellocation = null;
            mapUIsettings = MapForm.getUiSettings();
            //trying to reach actuel location
            while (gps.getLatitude() == 0 || gps.getLongitude() == 0) {
            }
            if (gps.getLongitude() != 0 && gps.getLatitude() != 0) {
                actuellocation = new LatLng(gps.getLatitude(), gps.getLongitude());
                placestartposition(actuellocation);
                Address adr = null;
                Geocoder geo = new Geocoder(getActivity());
                ville = "non identifié";

                try {
                    List<Address> list = geo.getFromLocation(actuellocation.latitude, actuellocation.longitude, 1);
                    if (list.size() > 0 && list != null) {
                        adr = list.get(0);
                        Start_Country = adr.getCountryName();
                        if (adr.getMaxAddressLineIndex() <= 1)
                            ville = adr.getAddressLine(0);
                        else {
                            startposition = adr.getAddressLine(0);
                            ville = adr.getAddressLine(1);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                actuellocation = new LatLng(35.769231, 10.819741);
                ville = "Monastir";
            }
            MapForm.moveCamera(CameraUpdateFactory.newLatLngZoom(actuellocation, 15));

//            MarkerOptions startmarkerop = new MarkerOptions()
//                    .title(ville)
//                    .draggable(true)
//                    .snippet("START position")
//                    .position(actuellocation)
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
//            startmarker = MapForm.addMarker(startmarkerop);
        }



        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        startposition_autocomplete.setThreshold(3);
        destposition_autocomplete.setThreshold(3);
        mPlaceArrayAdapter = new PlaceArrayAdapter(getContext(), android.R.layout.simple_list_item_1,
                BOUNDS, null);
        startposition_autocomplete.setAdapter(mPlaceArrayAdapter);
        startposition_autocomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear(startposition_autocomplete);
                AutoCompleteTextView acomplete =(AutoCompleteTextView) v;
            }
        });
        //semi automatic place search
        destposition_autocomplete.setAdapter(mPlaceArrayAdapter);
        destposition_autocomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear(destposition_autocomplete);
                AutoCompleteTextView acomplete =(AutoCompleteTextView) v;
            }
        });
        //semi automatic place search
        destposition_autocomplete.setOnItemClickListener(clickitemdestevent);
        startposition_autocomplete.setOnItemClickListener(clickstartitemevent);
    }

    private void WrongPlace() {
        KeyboardUtil.hideKeyboard(getActivity());
        startposition_autocomplete.clearFocus();
        destposition_autocomplete.clearFocus();
//        Toast.makeText(Add_Transport1.this, getString(R.string.no_places_found), Toast.LENGTH_LONG).show();
        Toast.makeText(getActivity(), R.string.no_places_found, Toast.LENGTH_SHORT).show();
    }

    //restart activity after activating connectivity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode != RESULT_OK)
                Toast.makeText(getContext(), "GPS est déactivé", Toast.LENGTH_LONG).show();
            else {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    setupmap();
                    MapForm.setMyLocationEnabled(true);
                }
            }
        }
    }


    //Check internet connexion and GPS if it's ok go ahead else finish the activity
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (Connectivity.Checkinternet(getActivity())) {
            if (!PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION) &&
                    !PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_COARSE_LOCATION))
                setupmap();
                else
                {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {  setupmap();
                        MapForm.setMyLocationEnabled(true);}
                }
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
    //event clear text typing
    private void clear(AutoCompleteTextView view) {
        view.setText("");
    }

    private void zoomOutFit() {
        if (startmarker != null && destinationmarker!=null)
        {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            LatLng Latlng1 = new LatLng(startmarker.getPosition().latitude, startmarker.getPosition().longitude);
            LatLng Latlng2 = new LatLng(destinationmarker.getPosition().latitude, destinationmarker.getPosition().longitude);
            builder.include(Latlng1);
            builder.include(Latlng2);
            LatLngBounds bounds = builder.build();
            MapForm.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
        }
        else if(startmarker!=null || destinationmarker!=null)
        {
            if (startmarker!=null)
            {
                LatLng latlng= new LatLng(startmarker.getPosition().latitude,startmarker.getPosition().longitude);
                MapForm.moveCamera(CameraUpdateFactory.newLatLng(latlng));
            }
            if (destinationmarker!=null)
            {
                LatLng latlng= new LatLng(destinationmarker.getPosition().latitude,destinationmarker.getPosition().longitude);
                MapForm.moveCamera(CameraUpdateFactory.newLatLng(latlng));
            }
        }
    }

    protected AdapterView.OnItemClickListener clickstartitemevent= new AdapterView.OnItemClickListener()
    {

        //choix d'emplacement
        @Override
        public void onItemClick(final AdapterView<?> parent, final View view, int position, long id)
        {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
                Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId).setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getStatus().isSuccess()) {
                            final Place myPlace = places.get(0);
                            LatLng queriedLocation = myPlace.getLatLng();
                            geocod = new Geocoder(getContext());

                            try {
                                String Country = "";
                                String ville = "inconnue";
                                Address location = null;
                                List<Address> add = geocod.getFromLocation(queriedLocation.latitude, queriedLocation.longitude, 2);
                                if (add.size() > 0) {
                                    location = add.get(0);
                                    Start_Country = location.getCountryName();
                                    if (location.getMaxAddressLineIndex() <= 1)
                                        ville = location.getAddressLine(0);
                                    else {
                                        startposition= location.getAddressLine(0);
                                        ville = location.getAddressLine(1);
                                    }
                                    Country = location.getCountryName();
                                    if (Country.equals("Tunisie") || Country.equals("France") || Country.equals("Tunisia")) {
                                        LatLng ltlng = new LatLng(location.getLatitude(), location.getLongitude());
                                        if (startmarker!= null)
                                            startmarker.remove();
                                        startmarker = MapForm.addMarker(new MarkerOptions()
                                                .title(ville)
                                                .snippet("start position")
                                                .position(ltlng)
                                                .draggable(true)
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                                        resizemap.setEnabled(true);
                                        zoomOutFit();
                                        KeyboardUtil.hideKeyboard(getActivity());
                                    } else {
                                        WrongPlace();
                                        clear(startposition_autocomplete);
                                    }


                                } else
                                { WrongPlace();
                                clear(startposition_autocomplete);}
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                        places.release();
                    }
                });

        }
    };
    protected AdapterView.OnItemClickListener clickitemdestevent= new AdapterView.OnItemClickListener()
    {

        //choix d'emplacement
        @Override
        public void onItemClick(final AdapterView<?> parent, final View view, int position, long id)
        {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);

                Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId).setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getStatus().isSuccess()) {
                            final Place myPlace = places.get(0);
                            LatLng queriedLocation = myPlace.getLatLng();
                            geocod = new Geocoder(getContext());

                            try {
                                String Country = "";
                                String ville = "inconnue";
                                Address location = null;
                                List<Address> add = geocod.getFromLocation(queriedLocation.latitude, queriedLocation.longitude, 2);
                                if (add.size() > 0) {
                                    location = add.get(0);
                                    Destination_Country = location.getCountryName();
                                    if (location.getMaxAddressLineIndex() <= 1)
                                        ville = location.getAddressLine(0);
                                    else {
                                        destposition = location.getAddressLine(0);
                                        ville = location.getAddressLine(1);
                                    }
                                    Country = location.getCountryName();
                                    if (Country.equals("Tunisie") || Country.equals("France") || Country.equals("Tunisia")) {
                                        LatLng ltlng = new LatLng(location.getLatitude(), location.getLongitude());
                                        if (destinationmarker != null)
                                            destinationmarker.remove();
                                        destinationmarker = MapForm.addMarker(new MarkerOptions()
                                                .title(ville)
                                                .snippet("destination position")
                                                .position(ltlng)
                                                .draggable(true)
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                                        resizemap.setEnabled(true);
                                        zoomOutFit();
                                        KeyboardUtil.hideKeyboard(getActivity());
                                    } else {
                                        WrongPlace();
                                        clear(destposition_autocomplete);
                                    }


                                } else
                                {WrongPlace();
                                clear(destposition_autocomplete);}
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                        places.release();
                    }
                });
            }
    };



}
