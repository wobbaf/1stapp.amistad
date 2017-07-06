package com.example.maciu.a1stapp.list.fragment;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.maciu.a1stapp.list.activity.ListActivity;
import com.example.maciu.a1stapp.list.adapter.ListAdapter;
import com.example.maciu.a1stapp.object.Card;
import com.example.maciu.a1stapp.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.maciu.a1stapp.detail.fragment.GoogleMapsFragment.MY_PERMISSIONS_REQUEST_LOCATION;

public class ListFragment extends Fragment {
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Card> localList;

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    public static ListFragment newInstance(ArrayList initList) {
        Bundle args = new Bundle();
        ListFragment fragment = new ListFragment();
        fragment.setValues(initList);
        fragment.setArguments(args);
        return fragment;
    }

    public void setValues(List<Card> list) {
        localList = new ArrayList<>();
        this.localList = list;
    }

    public void refreshFragment() {
        if(mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        Log.e("notify", String.valueOf(localList.size()));
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this,root);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(root.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ListAdapter(getActivity(), this.localList, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        ((ListActivity)getActivity()).fillList();
                    }
                }
        );
        return root;
    }
}
