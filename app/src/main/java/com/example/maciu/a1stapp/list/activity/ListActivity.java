package com.example.maciu.a1stapp.list.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.maciu.a1stapp.R;
import com.example.maciu.a1stapp.detail.fragment.DetailFragment;
import com.example.maciu.a1stapp.list.fragment.ListFragment;
import com.example.maciu.a1stapp.list.fragment.NewPlaceFragment;
import com.example.maciu.a1stapp.network.ApiService;
import com.example.maciu.a1stapp.network.RouteDBHelper;
import com.example.maciu.a1stapp.object.Card;
import com.example.maciu.a1stapp.object.MyResponse;
import com.example.maciu.a1stapp.object.Route;
import com.google.android.gms.maps.model.LatLng;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

import static com.example.maciu.a1stapp.detail.fragment.GoogleMapsFragment.MY_PERMISSIONS_REQUEST_LOCATION;

public class ListActivity extends AppCompatActivity {


    private static final String TAG = ListActivity.class.getName();
    private static final String ARGS_TITLE = TAG + "ARGS_NAME";
    private static final String ARGS_LONGITUDE = TAG + "ARGS_LONGITUDE";
    private static final String ARGS_LATITUDE = TAG + "ARGS_LATITUDE";

    public static void start(Context context, String name, double longitude, double latitude, String action) {
        Intent intent = new Intent(context, ListActivity.class);
        intent.setAction(action);
        intent.putExtra(ARGS_TITLE, name);
        intent.putExtra(ARGS_LONGITUDE, longitude);
        intent.putExtra(ARGS_LATITUDE, latitude);
        context.startActivity(intent);
    }

    private RouteDBHelper dbHelper;
    @BindView(R.id.addButton)
    ImageButton imageButton;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.addButton)
    public void onClick() {
        NewPlaceFragment gmFragment = NewPlaceFragment.newInstance(new Location("hej"), "tak");
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, gmFragment).addToBackStack(null).commit();
    }

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    boolean provider = true;
    private static final String URL_PREFIX = "http://www.traseo.pl/";
    private static final String BUNDLE_TAG = "BUNDLE_TAG";
    private ListFragment listFragment;
    ArrayList<Card> routesList;
    List<Route> mRoutes;
    RouteProvider routeProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        dbHelper = new RouteDBHelper(getApplication());
        ButterKnife.bind(this);
        if (isPortrait(this)) {

        }
        Log.e("listActivity", " created");
        routesList = new ArrayList<Card>();
        mRoutes = new ArrayList<>();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        listFragment = ListFragment.newInstance(routesList);
        Intent intent = getIntent();

        fragmentTransaction.replace(R.id.activity_main, listFragment);
        fragmentTransaction.commit();
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);
        if (savedInstanceState == null) {
            Log.e("saved == null", " creating main");
            if (intent.getAction() == "+1" && !mRoutes.contains(new Route(intent.getStringExtra(ARGS_TITLE), 12341, new LatLng(intent.getDoubleExtra(ARGS_LATITUDE, 0), intent.getDoubleExtra(ARGS_LONGITUDE, 0))))) {
                addToAPI(new Route(intent.getStringExtra(ARGS_TITLE), 12341, new LatLng(intent.getDoubleExtra(ARGS_LATITUDE, 0), intent.getDoubleExtra(ARGS_LONGITUDE, 0))));
                mRoutes.add(new Route(intent.getStringExtra(ARGS_TITLE), 12341, new LatLng(intent.getDoubleExtra(ARGS_LATITUDE, 0), intent.getDoubleExtra(ARGS_LONGITUDE, 0))));
            }
            fillList();
        } else {
            routesList = savedInstanceState.getParcelableArrayList(BUNDLE_TAG);
            mRoutes = new ArrayList<>();
            for (int i = 0; i < routesList.size(); i++) {
                mRoutes.add(new Route(routesList.get(i).getName(), routesList.get(i).getThumbId(), new LatLng(routesList.get(i).getLatitude(), routesList.get(i).getLongitude())));
            }
            fillList();
        }
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void addDetailsSplit(String name, int Thumbid, double distance) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DetailFragment detailFragment = DetailFragment.newInstance(name, Thumbid, distance);
        fragmentTransaction.replace(R.id.container, detailFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList(BUNDLE_TAG, routesList);
    }

    private void passToFragment(List passedList) {
        listFragment.setValues(passedList);
        listFragment.refreshFragment();
    }

    public static boolean isPortrait(Context context) {
        return context.getResources().getBoolean(R.bool.is_portrait);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void fillList() {
        if (isOnline()) {
            progressBar.setVisibility(View.VISIBLE);
            Retrofit retrofit = new Retrofit.Builder().baseUrl(URL_PREFIX).addConverterFactory(GsonConverterFactory.create()).build();
            final ApiService api = retrofit.create(ApiService.class);
            if (provider) {
                routeProvider = new RouteProvider() {
                    @Override
                    public void loadRoutes(Callback callback) {
                        callback.loaded(dbHelper.getEntries());
                        Log.e("load", " routes");
                    }

                    @Override
                    public void cancel() {
                    }
                };
                RouteProvider.Callback cb = new RouteProvider.Callback() {
                    @Override
                    public void loaded(List<Route> routes) {
                        Log.e("db call ", "succesfull");
                        mRoutes = new ArrayList<>();
                        mRoutes.addAll(routes);
                        passToFragment(mRoutes);
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void failed(Throwable throwable) {
                        Log.e(throwable.getMessage(), " failed");
                    }
                };
                routeProvider.loadRoutes(cb);
            } else {
                Call<MyResponse> call = api.getResponse();
                call.enqueue(new Callback<MyResponse>() {
                    @Override
                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                        if (response.isSuccessful()) {
                            Log.e("call ", "succesfull");
                            routesList = new ArrayList<>();
                            routesList.addAll(response.body().getRoutes());
                            mRoutes = new ArrayList<>();
                            for (int i = 0; i < routesList.size(); i++) {
                                mRoutes.add(new Route(routesList.get(i).getName(), routesList.get(i).getThumbId(), new LatLng(routesList.get(i).getLatitude(), routesList.get(i).getLongitude())));
                            }
                            passToFragment(mRoutes);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<MyResponse> call, Throwable t) {
                        Log.e(t.getMessage(), "failed");
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

//    public void fillAPI() {
//        dbHelper.dropTable();
//        dbHelper.createDB();
//        List<Route> troutes = new ArrayList<>();
//        Route route = new Route("name", 12321, new LatLng(0, 0));
//        Route route2 = new Route("name2", 12321, new LatLng(0, 0));
//        Route route3 = new Route("name3", 12321, new LatLng(0, 0));
//        troutes.add(route);
//        troutes.add(route2);
//        troutes.add(route3);
//        dbHelper.createEntries(troutes);
//    }

    public void addToAPI(Route route) {
//         dbHelper.dropTable();
//         dbHelper.createDB();
        dbHelper.createEntries(route);
    }
}

interface RouteProvider {
    void loadRoutes(Callback callback);

    void cancel();

    interface Callback {
        void loaded(List<Route> routes);

        void failed(Throwable throwable);
    }

}
