package com.windeco.listviewfeed.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.toolbox.ImageLoader;
import com.windeco.listviewfeed.MainActivity;
import com.windeco.listviewfeed.activity.InstallationCaseViewActivity;
import com.windeco.listviewfeed.activity.RecommandDesignViewActivity;
import com.windeco.listviewfeed.data.BannerItem;
import com.windeco.listviewfeed.view.BannerImageView;
import com.windeco.listviewfeed.R;
import com.windeco.listviewfeed.app.AppController;

import java.util.List;

/**
 * Created by ajay singh dewari on 11/5/17.
 */

public class BannerPagerAdapter extends PagerAdapter {

    private Context mContext;
    //TypedArray bannerArray;
    private List<BannerItem> bannerItems;
    //private View mfeedImageView;
    Intent i;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public  BannerPagerAdapter(Context context, List<BannerItem> bannerItems){
        mContext=context;
        this.bannerItems = bannerItems;

    }

    @Override
    public int getCount() {
        return bannerItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        BannerImageView bannerImageView;
//        return super.instantiateItem(container, position);
        LayoutInflater inflater = LayoutInflater.from(mContext);


        View mBannerImageView = inflater.inflate(R.layout.banner_fragment_layout, container, false);

        bannerImageView = (BannerImageView) mBannerImageView.findViewById(R.id.bannerImageView);
        TextView codeNumber = (TextView) mBannerImageView.findViewById(R.id.banner_code);
        TextView subject = (TextView) mBannerImageView.findViewById(R.id.banner_subject);
        TextView price = (TextView) mBannerImageView.findViewById(R.id.banner_price);

        codeNumber.setText(bannerItems.get(position).getCode());
        subject.setText(bannerItems.get(position).getSubject());
        price.setText(bannerItems.get(position).getPrice());

        BannerItem item = bannerItems.get(position);

        if (item.getImage1() != null) {
            bannerImageView.setImageUrl(item.getImage1(), imageLoader);
            //Log.d("로그", "setImageUrl : "+ bannerImageView.getImageHeight());
            bannerImageView.setVisibility(View.VISIBLE);
        } else {
            bannerImageView.setVisibility(View.GONE);
        }

        bannerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"swipe clicked"+position, Toast.LENGTH_LONG).show();

                i = new Intent(mContext , RecommandDesignViewActivity.class);
                // sending data to new activity

                i.putExtra("code", bannerItems.get(position).getCode());
                i.putExtra("subject", bannerItems.get(position).getSubject());
                i.putExtra("price", bannerItems.get(position).getPrice());
                i.putExtra("pyeong", bannerItems.get(position).getPyeong());
                i.putExtra("hashtag", bannerItems.get(position).getHashtag());
                i.putExtra("clientName", bannerItems.get(position).getClientName());
                i.putExtra("category", bannerItems.get(position).getCategory());
                i.putExtra("company", bannerItems.get(position).getCompany());
                i.putExtra("date", bannerItems.get(position).getDate());

                i.putExtra("place1", bannerItems.get(position).getPlace1());
                i.putExtra("explain1", bannerItems.get(position).getExplain1());
                i.putExtra("image1", bannerItems.get(position).getImage1());

                i.putExtra("place2", bannerItems.get(position).getPlace2());
                i.putExtra("explain2", bannerItems.get(position).getExplain2());
                i.putExtra("image2", bannerItems.get(position).getImage2());

                i.putExtra("place3", bannerItems.get(position).getPlace3());
                i.putExtra("explain3", bannerItems.get(position).getExplain3());
                i.putExtra("image3", bannerItems.get(position).getImage3());

                mContext.startActivity(i);

            }
        });

        container.addView(mBannerImageView);
        return mBannerImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
        container.removeView ((RelativeLayout) object);
    }
}