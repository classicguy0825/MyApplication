package com.windeco.listviewfeed;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.windeco.listviewfeed.activity.ConsultRequestViewActivity;
import com.windeco.listviewfeed.activity.InstallationCaseViewActivity;
import com.windeco.listviewfeed.adapter.BannerPagerAdapter;
import com.windeco.listviewfeed.adapter.FeedListAdapter;
import com.windeco.listviewfeed.app.AppController;
import com.windeco.listviewfeed.data.BannerItem;
import com.windeco.listviewfeed.data.FeedItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener  {

    private static final String TAG = MainActivity.class.getSimpleName();
    private CustomListView listView;
    private FeedListAdapter listAdapter;
    private List<BannerItem> bannerItems;
    private List<FeedItem> feedItems;
    private ViewPager mBannerViewPager;
    private com.windeco.listviewfeed.adapter.BannerPagerAdapter mBannerPagerAdapter;
    private LinearLayout mBannerDotsLayout;
    private int numberOfBannerImage;
    private View[] mBannerDotViews;

    private String URL_BANNER = "https://mission22.herokuapp.com/public/images/banner.json";
    private String URL_FEED = "https://mission22.herokuapp.com/public/images/feed.json";

    private String URL_MONGO = "http://192.168.0.5:3000/process/listuser";

    //private String URL_BANNER = "https://mission22.herokuapp.com/public/images/feed.json";

    private static final String logTag = "SwipeDetector";


	@Override
	protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme_NoActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        Toolbar toolbar = setToolBar();
        setActionBar();
        setDrawer(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button consultRequestBtn = (Button) findViewById(R.id.consultRequestBtn);
        consultRequestBtn.setOnClickListener(this);

        bannerItems = new ArrayList<BannerItem>();
        feedItems = new ArrayList<FeedItem>();


        /*===================Banner Pager Configuration=================*/
        mBannerViewPager = (ViewPager) findViewById(R.id.bannerViewPager);
        mBannerDotsLayout= (LinearLayout) findViewById(R.id.bannerDotsLayout);
        mBannerPagerAdapter = new BannerPagerAdapter(MainActivity.this, bannerItems);
        autoSwipeBanner();

        getFeedJson(URL_MONGO);

        // ListView Start
        listView = (CustomListView) findViewById(R.id.list);
        listAdapter = new FeedListAdapter(this, feedItems);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this ,feedItems.get(position).getName(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this , InstallationCaseViewActivity.class);
                // sending data to new activity
                i.putExtra("product", feedItems.get(position).getName());
                i.putExtra("image", feedItems.get(position).getImage());
                i.putExtra("status", feedItems.get(position).getSubject());
                startActivity(i);
                Log.d(logTag, "OnItemClick");
            }
        });
        // ListView End

        getItemUrl(URL_BANNER);
        getItemUrl(URL_FEED);


        numberOfBannerImage = bannerItems.size();
        mBannerDotViews = new View[numberOfBannerImage]; // create an empty array;
        makeDotLayout();

        mBannerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                changeDotBG(position);
                //Toast.makeText(MainActivity.this,"swipe clicked"+position, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        /*===========================END Banner Configuration Code ======================================*/

        mBannerViewPager.setAdapter(mBannerPagerAdapter);
        listView.setAdapter(listAdapter);
    }

    public void getFeedJson(String url) {

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("로그", "response : " + response);
                    //Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                    parseJsonFeeds(new JSONArray(response));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };

        request.setShouldCache(false);
        Volley.newRequestQueue(this).add(request);
        //println("웹서버에 요청함 : " + url);
    }



    private void setDrawer(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void setActionBar() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private Toolbar setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Status color Start
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.violetSecondColor));
        //Status color End
        return toolbar;
    }

    private void makeDotLayout() {
        for (int i = 0; i < numberOfBannerImage; i++) {
            // create a new textview
            final View bannerDotView = new View(this);

            /*Creating the dynamic dots for banner*/
            LinearLayout.LayoutParams dotLayoutParm = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dotLayoutParm.height = getResources().getDimensionPixelSize(R.dimen.standard_10);
            dotLayoutParm.width = getResources().getDimensionPixelSize(R.dimen.standard_10);
            dotLayoutParm.setMargins(getResources().getDimensionPixelSize(R.dimen.standard_8),0,0,0);
            bannerDotView.setLayoutParams(dotLayoutParm);
            bannerDotView.setBackground(getResources().getDrawable(R.drawable.shape_deselected_dot));

            // add the textview to the linearlayout
            mBannerDotsLayout.addView(bannerDotView);

            // save a reference to the textview for later
            mBannerDotViews[i] = bannerDotView;
        }
    }

    private void getItemUrl(String url) {

        String strURL = null;
        strURL = url;

        // We first check for cached request
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Entry entry = cache.get(strURL);

        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET, strURL, null, new Response.Listener<JSONObject>() {

@Override
public void onResponse(JSONObject response) {
        VolleyLog.d(TAG, "Response: " + response.toString());
        if (response != null) {
        parseJsonFeed(response);
        }
        }
        }, new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
        VolleyLog.d(TAG, "Error: " + error.getMessage());
        }
        });

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(jsonReq);
        }
    }

    public void autoSwipeBanner() {

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                int currentPage = mBannerViewPager.getCurrentItem();

                if (currentPage == numberOfBannerImage - 1) {
                    currentPage = -1;
                }
                mBannerViewPager.setCurrentItem(currentPage + 1, true);
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 500, 5000);
    }

    private void changeDotBG(int position) {

        for (int i = 0; i < numberOfBannerImage; i++) {
            if(position == i) {
                mBannerDotViews[i].setBackground(getResources().getDrawable(R.drawable.shape_selected_dot));
            }else{
                mBannerDotViews[i].setBackground(getResources().getDrawable(R.drawable.shape_deselected_dot));
            }
        }
    }

    private void parseJsonFeeds(JSONArray response) {

        JSONArray feedItemArray = response;
        //int feedItemsLength = response.length();
        try {
            for (int i = 0; i < feedItemArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedItemArray.get(i);

                FeedItem item = new FeedItem();
                item.setCategory(feedObj.getString("category"));
                item.setName(feedObj.getString("name"));
                item.setCompany(feedObj.getString("company"));
                item.setSubject(feedObj.getString("subject"));
                //item.setProfilePic(feedObj.getString("profilePic"));
                item.setLikeCount(feedObj.getString("likeCount"));

                // Image might be null sometimes
                String image = feedObj.isNull("image") ? null : feedObj.getString("image");
                item.setImage(image);
                feedItems.add(item);

                Log.d("로그", "feedItems :" + feedItems);

                //Toast.makeText(MainActivity.this, response.length() + "", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void parseJsonFeed(JSONObject response) {

        JSONArray jsonName = response.names();
        String strURL = null;

        try {
            strURL = jsonName.getString(0);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*if (strURL.equals("feed")) {
            try {
                JSONArray feedArray = response.getJSONArray("feed");

                for (int i = 0; i < feedArray.length(); i++) {
                    JSONObject feedObj = (JSONObject) feedArray.get(i);

                    FeedItem item = new FeedItem();
                    item.setId(feedObj.getInt("id"));
                    item.setName(feedObj.getString("name"));

                    // Image might be null sometimes
                    String image = feedObj.isNull("image") ? null : feedObj.getString("image");
                    item.setImge(image);
                    item.setStatus(feedObj.getString("status"));
                    item.setProfilePic(feedObj.getString("profilePic"));
                    item.setTimeStamp(feedObj.getString("timeStamp"));

                    // url might be null sometimes
                    String feedUrl = feedObj.isNull("url") ? null : feedObj.getString("url");
                    item.setUrl(feedUrl);

                    feedItems.add(item);
                }

                // notify data changes to list adapater
                listAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/

        if (strURL.equals("banner")) {
            try {
                JSONArray bannerArray = response.getJSONArray("banner");

                for (int i = 0; i < bannerArray.length(); i++) {
                    JSONObject bannerObj = (JSONObject) bannerArray.get(i);

                    BannerItem item = new BannerItem();

                    item.setCode(bannerObj.getString("code"));
                    item.setSubject(bannerObj.getString("subject"));
                    item.setPrice(bannerObj.getString("price"));
                    item.setPyeong(bannerObj.getString("pyeong"));
                    item.setHashtag(bannerObj.getString("hashtag"));
                    item.setClientName(bannerObj.getString("clientName"));
                    item.setCategory(bannerObj.getString("category"));
                    item.setCompany(bannerObj.getString("company"));
                    item.setDate(bannerObj.getString("date"));

                    item.setPlace1(bannerObj.getString("place1"));
                    item.setExplain1(bannerObj.getString("explain1"));
                    String image1 = bannerObj.isNull("image1") ? null : bannerObj.getString("image1");
                    item.setImage1(image1);

                    item.setPlace2(bannerObj.getString("place2"));
                    item.setExplain2(bannerObj.getString("explain2"));
                    String image2 = bannerObj.isNull("image2") ? null : bannerObj.getString("image2");
                    item.setImage2(image2);

                    item.setPlace3(bannerObj.getString("place3"));
                    item.setExplain3(bannerObj.getString("explain3"));
                    String image3 = bannerObj.isNull("image3") ? null : bannerObj.getString("image3");
                    item.setImage3(image3);

                    bannerItems.add(item);

                    Log.d("로그", "bannerItems :" + bannerItems);
                }

                // notify data changes to list adapater
                mBannerPagerAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.consultRequestBtn:
                Intent i = new Intent(MainActivity.this , ConsultRequestViewActivity.class);
                // sending data to new activity
                i.putExtra("product", "");
                startActivity(i);
                break;
        }
    }
}