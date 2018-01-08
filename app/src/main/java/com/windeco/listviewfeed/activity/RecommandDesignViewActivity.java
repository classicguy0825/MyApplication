package com.windeco.listviewfeed.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.windeco.listviewfeed.R;
import com.windeco.listviewfeed.app.AppController;
import com.windeco.listviewfeed.view.BannerImageView;
import com.windeco.listviewfeed.view.FeedImageView;

public class RecommandDesignViewActivity extends AppCompatActivity implements View.OnClickListener {

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    ImageView likeImage;
    int likeBtnFlag = 1;
    private Toolbar mToolbar;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.recommand_design_item_view);

        setToolBar();

        if (imageLoader == null) {
            imageLoader = AppController.getInstance().getImageLoader();
        }

        TextView txtSubject = (TextView) findViewById(R.id.subject);
        TextView txtPyeong = (TextView) findViewById(R.id.pyeong);
        TextView txtHashtag = (TextView) findViewById(R.id.hashtag);

        TextView txtPlace1 = (TextView) findViewById(R.id.place1);
        TextView txtExplain1 = (TextView) findViewById(R.id.explain1);
        BannerImageView image1 = (BannerImageView) findViewById(R.id.image1);

        TextView txtPlace2 = (TextView) findViewById(R.id.place2);
        TextView txtExplain2 = (TextView) findViewById(R.id.explain2);
        BannerImageView image2 = (BannerImageView) findViewById(R.id.image2);

        TextView txtPlace3 = (TextView) findViewById(R.id.place3);
        TextView txtExplain3 = (TextView) findViewById(R.id.explain3);
        BannerImageView image3 = (BannerImageView) findViewById(R.id.image3);

        likeImage = (ImageView)findViewById(R.id.likeBtn);
        LinearLayout likeBtnLayout = (LinearLayout) findViewById(R.id.likeBtnLayout);
        Button consultResBtn = (Button)findViewById(R.id.consultResBtn);

        Intent i = getIntent();
        // getting attached intent data
        txtSubject.setText(i.getStringExtra("subject"));
        txtPyeong.setText("[건물 평수] : "+i.getStringExtra("pyeong"));
        txtHashtag.setText("#"+i.getStringExtra("hashtag"));

        txtPlace1.setText("| "+ i.getStringExtra("place1"));
        txtExplain1.setText(i.getStringExtra("explain1"));

        if (image1 != null) {
            image1.setImageUrl(i.getStringExtra("image1"), imageLoader);
            image1.setVisibility(View.VISIBLE);

        } else {
            image1.setVisibility(View.GONE);
        }

        txtPlace2.setText("| "+i.getStringExtra("place2"));
        txtExplain2.setText(i.getStringExtra("explain2"));

        if (image2 != null) {
            image2.setImageUrl(i.getStringExtra("image2"), imageLoader);
            image2.setVisibility(View.VISIBLE);

        } else {
            image2.setVisibility(View.GONE);
        }

        txtPlace3.setText("| "+i.getStringExtra("place3"));
        txtExplain3.setText(i.getStringExtra("explain3"));

        if (image3 != null) {
            image3.setImageUrl(i.getStringExtra("image3"), imageLoader);
            image3.setVisibility(View.VISIBLE);

        } else {
            image3.setVisibility(View.GONE);
        }

        likeBtnLayout.setOnClickListener(this);
        consultResBtn.setOnClickListener(this);
	}

    private void setToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.recommand_designToolbar);

        mToolbar.setTitle("추천 디자인");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Status color Start
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.violetSecondColor));
        //Status color End
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.likeBtnLayout:
                if (likeBtnFlag % 2 == 1) {
                    likeImage.setImageResource(R.drawable.like);
                    likeBtnFlag++;
                }
                else {
                    likeImage.setImageResource(R.drawable.unlike);
                    likeBtnFlag--;
                }
                break;

            case R.id.consultResBtn:
                Intent i = new Intent(RecommandDesignViewActivity.this , ConsultRequestViewActivity.class);
                startActivity(i);
                break;

        }

    }
}
