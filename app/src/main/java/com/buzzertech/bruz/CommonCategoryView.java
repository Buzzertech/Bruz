package com.buzzertech.bruz;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommonCategoryView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    List<CategoryViewList> wallpapers;
    Intent receivedIntent;
    int category;
    String catString;
    private String CategoryURL;
    CommonAdapter adapter;
    RecyclerView recyclerView;
    ProgressDialog pDialog;
    private JSONArray storeArray;
    NavigationView navigationView;
    private CheckInternet checkInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_category_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
        navigationView.getMenu().getItem(1).setChecked(true);


        recyclerView = (RecyclerView) findViewById(R.id.categoryGallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        wallpapers = new ArrayList<>();
        receivedIntent = getIntent();
        category = receivedIntent.getExtras().getInt("categoryName");
        catString = getString(category);
        setTitle(catString + " | Category");
        getData();
        setRefreshContent();
        checkInternet = new CheckInternet(this);
        checkInternet.checkConnection();

    }

    private void setRefreshContent() {
        final SwipeRefreshLayout swiperefresh = (SwipeRefreshLayout) findViewById(R.id.contentCommonCategoryView);

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUpdatedWallpapers();
                swiperefresh.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swiperefresh.setRefreshing(false);
                    }
                }, 1500);
            }
        });
    }

    private void getUpdatedWallpapers() {
        
        StringRequest stringrequest = new StringRequest(Request.Method.GET, CategoryURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                updateList(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestqueue = Volley.newRequestQueue(this);
        requestqueue.add(stringrequest);
    }

    private void updateList(String response) {
        try {

            JSONObject mainObj = new JSONObject(response);
            JSONArray mainArray = mainObj.getJSONArray("wallpapers");
            if (mainArray.length() > wallpapers.size()) {
                wallpapers.clear();
                for (int i = 0; i < mainArray.length(); i++) {
                    JSONObject wallpaper = mainArray.getJSONObject(i);
                    CategoryViewList list = new CategoryViewList(
                            wallpaper.getString("link"),
                            wallpaper.getString("thumb"),
                            wallpaper.getInt("ID"),
                            wallpaper.getString("Source"),
                            wallpaper.getString("Designer"),
                            wallpaper.getString("Profile")
                    );
                    //String links = wallpaper.getString("link");

                    wallpapers.add(list);
                    Toast.makeText(this, "Wallpaper list is updated!", Toast.LENGTH_SHORT).show();

                }
            } else if (mainArray.length() < storeArray.length()) {
                wallpapers.clear();
                for (int i = 0; i < mainArray.length(); i++) {
                    JSONObject wallpaper = mainArray.getJSONObject(i);
                    CategoryViewList list = new CategoryViewList(
                            wallpaper.getString("link"),
                            wallpaper.getString("thumb"),
                            wallpaper.getInt("ID"),
                            wallpaper.getString("Source"),
                            wallpaper.getString("Designer"),
                            wallpaper.getString("Profile")
                    );
                    //String links = wallpaper.getString("link");

                    wallpapers.add(list);
                }

                //Toast.makeText(Home.this, "No new wallpapers available right now.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No new wallpapers available right now.", Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getData() {
        if (category == R.string.cat_all) {
            CategoryURL = AppConfig.FETCH_ALL;
        }
        else if (category ==  R.string.cat_animals){
            CategoryURL = AppConfig.FETCH_ANIMALS;
        }

        else if (category == R.string.cat_best){
            CategoryURL = AppConfig.FETCH_BEST;
        }

        else if (category == R.string.cat_cars){
            CategoryURL = AppConfig.FETCH_CARS;
        }

        else if (category == R.string.cat_cities){
            CategoryURL = AppConfig.FETCH_CITIES;
        }

        else if (category == R.string.cat_love){
            CategoryURL = AppConfig.FETCH_LOVE;
        }

        else if (category == R.string.cat_movies){
            CategoryURL = AppConfig.FETCH_MOVIES;
        }

        else if (category == R.string.cat_nature){
            CategoryURL = AppConfig.FETCH_NATURE;
        }

        else if (category == R.string.cat_quotes){
            CategoryURL = AppConfig.FETCH_QUOTES;
        }

        else if (category == R.string.cat_sports){
            CategoryURL = AppConfig.FETCH_SPORTS;
        }
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest stringrequest = new StringRequest(Request.Method.GET, CategoryURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseWallpapers(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestqueue = Volley.newRequestQueue(this);
        requestqueue.add(stringrequest);
    }

    private void parseWallpapers(String response) {
      pDialog.dismiss();
        try {
            JSONObject mainObj = new JSONObject(response);
            JSONArray mainArray = mainObj.getJSONArray("wallpapers");
            storeArray = mainArray;
            if(mainArray.length() == 0){
                LinearLayout errorLay = (LinearLayout) findViewById(R.id.errorLay);
                errorLay.setVisibility(View.VISIBLE);
            }
            else {
                for (int i = 0; i < mainArray.length(); i++) {
                    JSONObject wallpaper = mainArray.getJSONObject(i);
                    CategoryViewList list = new CategoryViewList(
                            wallpaper.getString("link"),
                            wallpaper.getString("thumb"),
                            wallpaper.getInt("ID"),
                            wallpaper.getString("Source"),
                            wallpaper.getString("Designer"),
                            wallpaper.getString("Profile")
                    );
                    //String links = wallpaper.getString("link");

                    wallpapers.add(list);

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new CommonAdapter(CommonCategoryView.this, wallpapers);
        recyclerView.setAdapter(adapter);

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
       // getMenuInflater().inflate(R.menu.home, menu);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (id == R.id.nav_home) {
            drawer.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(CommonCategoryView.this, Home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_category) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_pick) {
            PickRandom pickRandom = new PickRandom(this);
            pickRandom.pickRandomWallpaper();
        }
        else if (id == R.id.nav_share) {
            ShareLink shareLink = new ShareLink(this);
            shareLink.shareLink();
        } else if (id == R.id.nav_feedback) {
            Feedback feedback = new Feedback(this);
            feedback.getFeedback();
        } else if (id == R.id.nav_rate) {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(myAppLinkToMarket);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
            }
            checkInternet.cancelHandler();
        } else if (id == R.id.nav_about) {
            Intent about = new Intent(this, AboutUs.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(about, bundle);
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        checkInternet.checkConnection();
    }

    @Override
    protected void onPause(){
        checkInternet.cancelHandler();
        super.onPause();
    }


}
