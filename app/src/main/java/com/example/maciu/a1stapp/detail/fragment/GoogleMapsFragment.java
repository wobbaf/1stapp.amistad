package com.example.maciu.a1stapp.detail.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.maciu.a1stapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.*;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class GoogleMapsFragment extends android.support.v4.app.Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    @BindView(R.id.googlemaps_select_location)
    FloatingActionButton googleButton;
    @BindView(R.id.mapView)
    MapView mapView;
    private GoogleMap gMap;
    private Location location = new Location("passedLocation");
    private String name;
    private Location mLastLocation;
    private static final String TAG = GoogleMapsFragment.class.getName();
    private static final String ARGS_TITLE = TAG + "ARGS_TITLE";
    private static final String ARGS_LONGITUDE = TAG + "ARGS_LONGITUDE";
    private static final String ARGS_LATITUDE = TAG + "ARGS_LATITUDE";
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private GoogleApiClient mGoogleApiClient;

    @OnClick(R.id.googlemaps_select_location)
    void onClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Czy chcesz wyświetlić swoją lokalizację?").setTitle(R.string.dialog_title);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (mLastLocation != null) {
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())).zoom(10).build();
                    gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
                else{
                    Log.e("LastLocation in GMF", "= null");
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.e("No location of", "user");
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public GoogleMapsFragment() {
    }

    public static GoogleMapsFragment newInstance(Location startLocation, String name) {
        GoogleMapsFragment fragment = new GoogleMapsFragment();
        Bundle args = new Bundle();
        fragment.setLocation(startLocation);
        args.putString(ARGS_TITLE, name);
        args.putDouble(ARGS_LONGITUDE, startLocation.getLongitude());
        args.putDouble(ARGS_LATITUDE, startLocation.getLatitude());
        fragment.setArguments(args);
        return fragment;
    }

    private void setLocation(Location passedLocation) {
        this.location = passedLocation;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_google_maps, container, false);
        buildGoogleApiClient();
        startLocationUpdates();
        ButterKnife.bind(this, root);
        location.setLongitude(getArguments().getDouble(ARGS_LONGITUDE, 0));
        location.setLatitude(getArguments().getDouble(ARGS_LATITUDE, 0));
        name = getArguments().getString(ARGS_TITLE);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mMap.getUiSettings().setCompassEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                gMap = mMap;
                final LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                gMap.addMarker(new MarkerOptions().position(loc).title(name));
                gMap.addPolyline(new PolylineOptions().add(loc,new LatLng(50.056926,19.932781)).width(5).color(Color.BLACK));
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Log.e("No permissions granted", "Location");
                }else {
                    getLastLocation();
                }
            }
        });
        return root;
    }

    private void startLocationUpdates() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(this.getActivity());
        settingsClient.checkLocationSettings(locationSettingsRequest);

        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        getFusedLocationProviderClient(this.getActivity()).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    private void onLocationChanged(Location location) {
       // LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mLastLocation = location;
//        if(gMap != null) {
//            gMap.clear();
//            gMap.addMarker(new MarkerOptions().position(latLng).title("Your position"));
//        }
    }

    private void getLastLocation() {
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this.getActivity());

        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            onLocationChanged(location);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("LastLocation ", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
