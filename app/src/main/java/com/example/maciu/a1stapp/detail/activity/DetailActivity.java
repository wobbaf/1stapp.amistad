package com.example.maciu.a1stapp.detail.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.maciu.a1stapp.detail.fragment.DetailFragment;
import com.example.maciu.a1stapp.detail.fragment.GoogleMapsFragment;
import com.example.maciu.a1stapp.R;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;

    private static final String TAG = DetailActivity.class.getName();
    private static final String TAG_TAB_INFO = "INFO";
    private static final String TAG_TAB_MAP = "MAPA";
    private static final String ARGS_TITLE = TAG + "ARGS_TITLE";
    private static final String ARGS_PICTURE = TAG + "ARGS_PICTURE";
    private static final String ARGS_DISTANCE = TAG + "ARGS_DISTANCE";
    private static final String ARGS_LONGITUDE = TAG + "ARGS_LONGITUDE";
    private static final String ARGS_LATITUDE = TAG + "ARGS_LATITUDE";
    private static final String ARGS_SCORE = TAG + "ARGS_SCORE";
    private static Location localLocation = new Location("passedLocation");

    public static void start(Activity activity, Context context, String title, int picture, double distance, Location startLocation, double score) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(ARGS_TITLE, title);
        intent.putExtra(ARGS_PICTURE, picture);
        intent.putExtra(ARGS_DISTANCE, distance);
        intent.putExtra(ARGS_LONGITUDE, startLocation.getLongitude());
        intent.putExtra(ARGS_LATITUDE, startLocation.getLatitude());
        intent.putExtra(ARGS_SCORE, score);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(activity, R.anim.fadein, R.anim.fadeout);
        context.startActivity(intent, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Intent recievedIntent = getIntent();
        String localTitle = recievedIntent.getStringExtra(ARGS_TITLE);
        int localThumbid = recievedIntent.getIntExtra(ARGS_PICTURE, 0);
        double localScore = recievedIntent.getDoubleExtra(ARGS_SCORE,0);
        double localDistance = recievedIntent.getDoubleExtra(ARGS_DISTANCE, 0);
        localLocation.setLongitude(recievedIntent.getDoubleExtra(ARGS_LONGITUDE, 0));
        localLocation.setLatitude(recievedIntent.getDoubleExtra(ARGS_LATITUDE, 0));

        //setting toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //setting toolbar title
        toolbarTitle.setText(localTitle);
        toolbarTitle.setAllCaps(true);
        toolbarTitle.setTypeface(null, Typeface.BOLD);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        //adding fragments
        viewPagerAdapter.addFragment(DetailFragment.newInstance(localTitle, localThumbid, localDistance, localScore),TAG_TAB_INFO);
        viewPagerAdapter.addFragment(GoogleMapsFragment.newInstance(localLocation, localTitle),TAG_TAB_MAP);

        //applying adapter
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
