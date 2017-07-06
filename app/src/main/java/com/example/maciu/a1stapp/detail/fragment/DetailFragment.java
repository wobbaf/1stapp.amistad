package com.example.maciu.a1stapp.detail.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.example.maciu.a1stapp.R;
import com.example.maciu.a1stapp.list.activity.ListActivity;

public class DetailFragment extends android.support.v4.app.Fragment {
    private static final String TAG = DetailFragment.class.getName();
    private static final String URL_PREFIX = "http://www.traseo.pl/zdjecia/";
    private static final String ARGS_THUMBID = TAG + "ARGS_THUMBID";
    private static final String ARGS_NAME = TAG + "ARGS_NAME";
    private static final String ARGS_DISTANCE = TAG + "ARGS_DISTANCE";
    @BindView(R.id.iView)
    ImageView imageView;

    @BindView(R.id.nameText)
    TextView textView;

    public static DetailFragment newInstance(String name, int thumbid, double distance) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_NAME, name);
        args.putInt(ARGS_THUMBID, thumbid);
        args.putDouble(ARGS_DISTANCE, distance);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, root);
        if (getArguments() != null) {
            if(!ListActivity.isPortrait(getActivity())){
                textView.setText(getArguments().getString(ARGS_NAME));
                textView.setAllCaps(true);
                textView.setVisibility(View.VISIBLE);
                imageView.setPadding(20,0,20,0);
            }
            int localThumbid = getArguments().getInt(ARGS_THUMBID, 0);
            Glide.with(this).load(URL_PREFIX + localThumbid).into(imageView);
        }
        return root;
    }
}
