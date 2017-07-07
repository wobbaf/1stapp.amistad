package com.example.maciu.a1stapp.list.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.maciu.a1stapp.R;
import com.example.maciu.a1stapp.object.Route;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.*;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class NewPlaceFragment extends android.support.v4.app.Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    @BindView(R.id.googlemaps_select_location)
    FloatingActionButton googleButton;
    @BindView(R.id.confirm)
    Button button;
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.getName)
    EditText editText;
    private GoogleMap gMap;
    private Polyline polyline;
    Marker marker;
    Location location = new Location("passedLocation");
    String name;
    protected Location mLastLocation;
    Location currentLocation = new Location("currentLocation");
    private LocationRequest mLocationRequest;
    private static final String TAG = NewPlaceFragment.class.getName();
    private static final String ARGS_TITLE = TAG + "ARGS_TITLE";
    private static final String ARGS_LONGITUDE = TAG + "ARGS_LONGITUDE";
    private static final String ARGS_LATITUDE = TAG + "ARGS_LATITUDE";
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    protected GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;

    @OnClick(R.id.googlemaps_select_location)
    public void onClick() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setMessage("Czy chcesz wyświetlić swoją lokalizację?").setTitle(R.string.dialog_title);
//        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                if (mLastLocation != null) {
//                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())).zoom(10).build();
//                    gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                }
//                else{
//                    Log.e(String.valueOf(mLastLocation.getLatitude()),String.valueOf(mLastLocation.getLongitude()));
//                }
//            }
//        });
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
    }

    @OnClick(R.id.confirm)
    public void confOnClick(){
        com.example.maciu.a1stapp.list.activity.ListActivity.start(getContext(), editText.getText().toString(), marker.getPosition().latitude, marker.getPosition().longitude, "+1");
    }
    public NewPlaceFragment() {
        // Required empty public constructor
    }

    public static NewPlaceFragment newInstance(Location startLocation, String name) {
        NewPlaceFragment fragment = new NewPlaceFragment();
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
        mFusedLocationClient = getFusedLocationProviderClient(this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_new_place, container, false);
        ButterKnife.bind(this, root);
        mFusedLocationClient = getFusedLocationProviderClient(getActivity());
        location.setLongitude(getArguments().getDouble(ARGS_LONGITUDE, 0));
        location.setLatitude(getArguments().getDouble(ARGS_LATITUDE, 0));
        name = getArguments().getString(ARGS_TITLE);
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

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
