package com.windeco.listviewfeed.adapter;

import com.windeco.listviewfeed.view.FeedImageView;
import com.windeco.listviewfeed.R;
import com.windeco.listviewfeed.app.AppController;
import com.windeco.listviewfeed.data.FeedItem;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.volley.toolbox.ImageLoader;

public class FeedListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<FeedItem> feedItems;

    //int positionCount;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public FeedListAdapter(Activity activity, List<FeedItem> feedItems) {

        this.activity = activity;
        this.feedItems = feedItems;

    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.feed_item, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        FeedImageView feedImageView = (FeedImageView) convertView.findViewById(R.id.feedImage1);

        FeedItem item = feedItems.get(position);

        // Feed image
        if (item.getImage() != null) {
            feedImageView.setImageUrl(item.getImage(), imageLoader);
            //Log.d("로그", "FeedListAdapter InSide : " + item.getImage());
            feedImageView.setVisibility(View.VISIBLE);
            feedImageView.setResponseObserver(new FeedImageView.ResponseObserver() {
                @Override
                public void onError() {
                }

                @Override
                public void onSuccess() {

                }
            });
        } else {
            feedImageView.setVisibility(View.GONE);
        }

        return convertView;
    }
}
