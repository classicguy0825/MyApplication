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
import com.windeco.listviewfeed.view.FeedImageView;

public class InstallationCaseViewActivity extends AppCompatActivity implements View.OnClickListener {

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    ImageView likeImage;
    int likeBtnFlag = 1;
    private Toolbar mToolbar;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.installation_case_item_view);

        mToolbar = (Toolbar) findViewById(R.id.installationCaseToolbar);

        mToolbar.setTitle("시공 사례");
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

        TextView txtProduct = (TextView) findViewById(R.id.product_label);
        TextView txtStatus = (TextView) findViewById(R.id.status);
        FeedImageView feedImageView = (FeedImageView) findViewById(R.id.detailViewImg);
        likeImage = (ImageView)findViewById(R.id.likeBtn);
        LinearLayout likeBtnLayout = (LinearLayout) findViewById(R.id.likeBtnLayout);
        Button consultResBtn = (Button)findViewById(R.id.consultResBtn);

        Intent i = getIntent();
        // getting attached intent data
        String product = i.getStringExtra("product");
        String status = i.getStringExtra("status");
        String image = i.getStringExtra("image");
        // displaying selected product name
        txtProduct.setText("시공업체 : " + product);
        txtStatus.setText(status);

        if (imageLoader == null) {
            imageLoader = AppController.getInstance().getImageLoader();
        }

        if (image != null) {
            feedImageView.setImageUrl(image, imageLoader);
            feedImageView.setVisibility(View.VISIBLE);
            feedImageView
                    .setResponseObserver(new FeedImageView.ResponseObserver() {
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

        likeBtnLayout.setOnClickListener(this);
        consultResBtn.setOnClickListener(this);
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
                Intent i = new Intent(InstallationCaseViewActivity.this , ConsultRequestViewActivity.class);
                startActivity(i);
                break;

        }

    }
}
