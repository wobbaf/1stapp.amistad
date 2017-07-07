package com.example.maciu.a1stapp.list.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.*;
import butterknife.*;
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

import java.util.*;

import static com.example.maciu.a1stapp.detail.fragment.GoogleMapsFragment.MY_PERMISSIONS_REQUEST_LOCATION;

public class ListActivity extends AppCompatActivity {


    private static final String TAG = ListActivity.class.getName();
    private static final String ARGS_TITLE = TAG + "ARGS_NAME";
    private static final String ARGS_SCORE = TAG + "ARGS_SCORE";
    private static final String ARGS_LONGITUDE = TAG + "ARGS_LONGITUDE";
    private static final String ARGS_LATITUDE = TAG + "ARGS_LATITUDE";
    private static final String ARGS_DISTANCE = TAG + "ARGS_DISTANCE";
    private static final String ARGS_ACTION_ADD = NewPlaceFragment.class.getName() + "ARGS_ACTION_ADD";
    private static final String URL_PREFIX = "http://www.traseo.pl/";
    private static final String BUNDLE_TAG = "BUNDLE_TAG";

    public static void start(Context context, String name, double longitude, double latitude, double distance, double score, String action) {
        Intent intent = new Intent(context, ListActivity.class);
        intent.setAction(action);
        intent.putExtra(ARGS_TITLE, name);
        intent.putExtra(ARGS_LONGITUDE, longitude);
        intent.putExtra(ARGS_LATITUDE, latitude);
        intent.putExtra(ARGS_DISTANCE, distance);
        intent.putExtra(ARGS_ACTION_ADD, action);
        intent.putExtra(ARGS_SCORE, score);
        context.startActivity(intent);
    }

    private RouteDBHelper dbHelper;
    @BindView(R.id.addButton)
    ImageButton imageButton;

    @OnClick(R.id.addButton)
    void onClick() {
        NewPlaceFragment gmFragment = NewPlaceFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, gmFragment).addToBackStack(null).commit();
    }

    @BindArray(R.array.sort_array)
    String[] mSortTitles;

    @OnItemClick(R.id.left_drawer)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Comparator comparator = null;
        switch (position) {
            case 0:
                Log.e("Sort by", "name");

                comparator = new Comparator<Route>() {
                    @Override
                    public int compare(Route o, Route o1) {
                        return o.getName().compareTo(o1.getName());
                    }
                };
                break;
            case 1:
                Log.e("Sort by", "dist");
                comparator = new Comparator<Route>() {
                    @Override
                    public int compare(Route o, Route o1) {
                        return Double.compare(o.getDistance(), o1.getDistance());
                    }
                };
                break;
        }
        Collections.sort(mRoutes, comparator);
        Log.e(mRoutes.get(0).getName(), mRoutes.get(1).getName());
        Log.e(String.valueOf(mRoutes.get(0).getDistance()), String.valueOf(mRoutes.get(1).getDistance()));
        Log.e(String.valueOf(mRoutes.get(2).getDistance()), String.valueOf(mRoutes.get(3).getDistance()));
        mDrawerList.setItemChecked(position, true);
        passToFragment(mRoutes);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.left_drawer)
    ListView mDrawerList;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private boolean provider = false;
    private ListFragment listFragment;
    private ArrayList<Card> routesList;
    private List<Route> mRoutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        dbHelper = new RouteDBHelper(getApplication());
        ButterKnife.bind(this);
        Log.e("listActivity", " created");
        routesList = new ArrayList<>();
        mRoutes = new ArrayList<>();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        listFragment = ListFragment.newInstance(convertListToRoutes(routesList));
        Intent intent = getIntent();
        if (!isPortrait(this)) {
            imageButton.setVisibility(View.INVISIBLE);
        }
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.navdraw_row,
                R.id.navdrawtext, mSortTitles));

        fragmentTransaction.replace(R.id.activity_main, listFragment);
        fragmentTransaction.commit();
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);
        if (savedInstanceState == null) {
            Log.e("saved == null", " creating main");
            if (intent.getAction() != null) {
                if (intent.getAction().equals(ARGS_ACTION_ADD) &&
                        !mRoutes.contains(new Route(intent.getStringExtra(ARGS_TITLE),
                                12341,
                                new LatLng(intent.getDoubleExtra(ARGS_LATITUDE,
                                        0),
                                        intent.getDoubleExtra(ARGS_LONGITUDE, 0)),
                                intent.getDoubleExtra(ARGS_DISTANCE, 0),
                                intent.getDoubleExtra(ARGS_SCORE, 0)))) {
                    addToAPI(new Route(intent.getStringExtra(ARGS_TITLE),
                            12341,
                            new LatLng(intent.getDoubleExtra(ARGS_LATITUDE,
                                    0), intent.getDoubleExtra(ARGS_LONGITUDE,
                                    0)), intent.getDoubleExtra(ARGS_DISTANCE, 0),
                            intent.getIntExtra(ARGS_SCORE, 0)));
                }
                fillList();
            }
        } else {
            routesList = savedInstanceState.getParcelableArrayList(BUNDLE_TAG);
            fillList();
        }
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportFragmentManager() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public void addDetailsSplit(String name, int Thumbid, double distance, double score) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DetailFragment detailFragment = DetailFragment.newInstance(name, Thumbid, distance, score);
        fragmentTransaction.replace(R.id.container, detailFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList(BUNDLE_TAG, routesList);
    }

    private void passToFragment(List<Route> passedList) {
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
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(URL_PREFIX).addConverterFactory(GsonConverterFactory.create()).build();
        final ApiService api = retrofit.create(ApiService.class);
        if (isOnline()) {
            provider = false;
        }
        if (provider) {
            RouteProvider routeProvider = new RouteProvider() {

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
            if (isOnline()) {
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
                                mRoutes.add(new Route(routesList.get(i)));
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
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Brak połączenia z internetem. Czy chcesz spróbować ponownie?").setTitle(R.string.dialog_title);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        if (isOnline()) {
                            provider = false;
                            fillList();
                        } else {
                            provider = true;
                            fillList();
                        }
                    }
                });

                builder.setNegativeButton(R.string.local_data, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        provider = true;
                        fillList();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    private boolean isOnline() {
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

    private void addToAPI(Route route) {
//         dbHelper.dropTable();
//         dbHelper.createDB();
        dbHelper.createEntries(route);
    }

    private ArrayList<Route> convertListToRoutes(ArrayList<Card> cards) {
        ArrayList<Route> routes = new ArrayList<>();
        for (Card card : cards) {
            routes.add(new Route(card));
        }
        return routes;
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
