package com.example.maciu.a1stapp.list.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.maciu.a1stapp.detail.fragment.DetailFragment;
import com.example.maciu.a1stapp.list.activity.ListActivity;
import com.example.maciu.a1stapp.object.Card;
import com.example.maciu.a1stapp.R;
import com.example.maciu.a1stapp.detail.activity.DetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maciu on 03.07.2017.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<Card> mDataset;
    private Context context;
    private Activity listActivity;
    private List<Bitmap> loadedPictures = null;
    private static final String URL_PHOTO_PREFIX = "http://www.traseo.pl/zdjecia/";
    private DetailFragment detailFragment;
    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.info_text)
        TextView mTextView;
        @BindView(R.id.info_pic)
        ImageView mImageView;
        @BindView(R.id.dist_text)
        TextView mDistView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.linLay)
        public void onClick() {
            Location location = new Location("passedLocation");
            if (mDataset.get(getAdapterPosition()).getLatitude() != null) {
                location.setLatitude(mDataset.get(getAdapterPosition()).getLatitude());
                location.setLongitude(mDataset.get(getAdapterPosition()).getLongitude());
            }
            if(ListActivity.isPortrait(context)) {
                DetailActivity.start(context, mDataset.get(getAdapterPosition()).getName(), mDataset.get(getAdapterPosition()).getThumbId(), mDataset.get(getAdapterPosition()).getDistance(), location);
            }
            else{
                //Glide.with(context).load(URL_PHOTO_PREFIX + mDataset.get(getAdapterPosition()).getThumbId()).into(imageView);
                ((ListActivity)listActivity).addDetailsSplit(mDataset.get(getAdapterPosition()).getName(), mDataset.get(getAdapterPosition()).getThumbId(), mDataset.get(getAdapterPosition()).getDistance());
            }
        }
    }

    public ListAdapter(Context c, List<Card> myDataset, Activity passedActivity) {
        mDataset = new ArrayList<>();
        mDataset = myDataset;
        context = c;
        this.listActivity = passedActivity;
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        loadedPictures = new ArrayList<>();
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        if (mDataset != null && loadedPictures.size() < mDataset.size()) {
            final Card card = mDataset.get(viewType);
            holder.mTextView.setText(card.getName());
            holder.mTextView.setShadowLayer(5, 5, 2, Color.GRAY);
            holder.mTextView.setAllCaps(true);
            holder.mDistView.setText((card.getDistance().toString()) + " km");
            holder.mDistView.setShadowLayer(5, 5, 2, Color.GRAY);
            holder.mDistView.setAllCaps(true);
            Glide.with(context).load(URL_PHOTO_PREFIX + card.getThumbId().toString()).asBitmap().centerCrop().override(200, 200)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            loadedPictures.add(resource);
                            holder.mImageView.setImageBitmap(resource);
                        }
                    });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (mDataset != null)
            return mDataset.size();
        return 0;
    }
}