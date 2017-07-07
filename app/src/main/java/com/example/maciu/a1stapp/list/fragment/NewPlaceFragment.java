package com.example.maciu.a1stapp.list.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.maciu.a1stapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class NewPlaceFragment extends android.support.v4.app.Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    @BindView(R.id.googlemaps_select_location)
    FloatingActionButton googleButton;
    @BindView(R.id.confirm)
    FloatingActionButton button;
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.getName)
    EditText editText;
    private GoogleMap gMap;
    private Marker marker;
    private Location mLastLocation;
    private static final String TAG = NewPlaceFragment.class.getName();
    private static final String ARGS_ACTION_ADD = TAG + "ARGS_ACTION_ADD";
    private GoogleApiClient mGoogleApiClient;

    @OnClick(R.id.googlemaps_select_location)
    void onClick() {
                if (mLastLocation != null) {
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(mLastLocation.getLatitude(),
                            mLastLocation.getLongitude())).zoom(10).build();
                    gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
                else{
                    Log.e("NewPlaceFragment:","Last location is null");
                }
    }

    @OnClick(R.id.confirm)
    void confOnClick(){
        if(editText != null && marker != null) {
            com.example.maciu.a1stapp.list.activity.ListActivity.start(getContext(),
                    editText.getText().toString(),
                    marker.getPosition().latitude,
                    marker.getPosition().longitude,
                    marker.getPosition().latitude,
                    0,
                    ARGS_ACTION_ADD);
        }
        else{
            this.onDetach();
        }
        }
    public NewPlaceFragment() {
        // Required empty public constructor
    }

    public static NewPlaceFragment newInstance() {
        NewPlaceFragment fragment = new NewPlaceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_new_place, container, false);
        ButterKnife.bind(this, root);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap mMap) {
                mMap.getUiSettings().setCompassEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng point) {
                        mMap.clear();
                        marker = mMap.addMarker(new MarkerOptions().position(point));
                    }
                });
                gMap = mMap;
            }
        });
        return root;
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
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
