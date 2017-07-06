package com.example.maciu.a1stapp.list.activity;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.maciu.a1stapp.R;
import com.example.maciu.a1stapp.detail.fragment.DetailFragment;
import com.example.maciu.a1stapp.list.fragment.ListFragment;
import com.example.maciu.a1stapp.network.ApiService;
import com.example.maciu.a1stapp.object.Card;
import com.example.maciu.a1stapp.object.MyResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;

import static com.example.maciu.a1stapp.detail.fragment.GoogleMapsFragment.MY_PERMISSIONS_REQUEST_LOCATION;

public class ListActivity extends AppCompatActivity {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private static final String URL_PREFIX = "http://www.traseo.pl/";
    private static final String BUNDLE_TAG = "BUNDLE_TAG";
    private ListFragment listFragment;
    ArrayList<Card> routesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
//        if(!isPortrait(this)){
//            DetailFragment detailFragment = DetailFragment.newInstance();
//        }
//        else{
        ButterKnife.bind(this);
        Log.e("listActivity", " created");
        routesList = new ArrayList<Card>();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        listFragment = ListFragment.newInstance(routesList);
        fragmentTransaction.replace(R.id.activity_main, listFragment);
        fragmentTransaction.commit();
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);
        if (savedInstanceState == null) {
            Log.e("saved == null", " creating main");
            fillList();
        } else {
            routesList = savedInstanceState.getParcelableArrayList(BUNDLE_TAG);
            listFragment.setValues(routesList);
            listFragment.refreshFragment();
        }
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //}
    }

    public void addDetailsSplit(String name, int Thumbid, double distance){
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

    private void passToFragment(ArrayList passedList) {
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
            ApiService api = retrofit.create(ApiService.class);
            Call<MyResponse> call = api.getResponse();
            call.enqueue(new Callback<MyResponse>() {
                @Override
                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                    if (response.isSuccessful()) {
                        Log.e("call ", "succesfull");
                        routesList = new ArrayList<>();
                        routesList.addAll(response.body().getRoutes());
                        passToFragment(routesList);
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
}

