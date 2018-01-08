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

import com.android.volley.toolbox.ImageLoader;
import com.windeco.listviewfeed.CalendarViewActivity;
import com.windeco.listviewfeed.R;
import com.windeco.listviewfeed.app.AppController;

public class ConsultRequestViewActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Button mConsultReqBtn;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.consult_request_view);

        mToolbar = (Toolbar) findViewById(R.id.conReqToolbar);
        mConsultReqBtn = (Button) findViewById(R.id.requestDate);

        mToolbar.setTitle("커튼 & 블라인드 견적 요청");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //mToolbar.setNavigationIcon(R.drawable.arrow);

        //Status color Start
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.violetSecondColor));
        //Status color End

        mConsultReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.requestDate:
                        Intent i = new Intent(ConsultRequestViewActivity.this , CalendarViewActivity.class);
                        startActivity(i);
                        break;
                }
            }
        });
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
}
