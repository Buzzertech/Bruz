package com.buzzertech.bruz;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private List<WallpapersList> wallpapers;
    private List<CarouselList> carouselList;
    private RecyclerView recyclerView;
    private WallpaperAdapter adapter;
    NavigationView navigationView;
    CheckInternet checkInternet;
    ProgressDialog progressDialog;

    boolean updateAvailable;
    private JSONArray storeArray;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        requestPerm();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);
        int size = navigationView.getMenu().size();
        for(int i = 1; i < size; i++){
            navigationView.getMenu().getItem(i).setChecked(false);
        }

        recyclerView = (RecyclerView) findViewById(R.id.wallpaperGallery);
        recyclerView.setHasFixedSize(true);

        //getCarouselData();
        carouselList = new ArrayList<>();


        int value;
        value = getApplication().getResources().getConfiguration().orientation;
        if(value == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
            recyclerView.setLayoutManager(layoutManager);

        }
        else{
            final GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return adapter.isHeader(position) ? layoutManager.getSpanCount() : 1;
                }
            });
            recyclerView.setLayoutManager(layoutManager);
        }


        wallpapers = new ArrayList<>();

        if(CheckInternet.isDeviceOnline(getApplicationContext())){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading Wallpapers!");
            getCarouselData();
            refreshcontent();
        }

        checkInternet = new CheckInternet(this);
        checkInternet.checkConnection();


        CheckAppVersion checkAppVersion = new CheckAppVersion(this);
        checkAppVersion.checkVersion();
    }



    private void getUpdatedWallpapers() {

        StringRequest stringrequest = new StringRequest(Request.Method.GET, AppConfig.FETCH_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject mainObj = new JSONObject(response);
                    JSONArray mainArray = mainObj.getJSONArray("wallpapers");
                    if (mainArray.length() > wallpapers.size()) {
                        wallpapers.clear();
                        for (int i = 0; i < mainArray.length(); i++) {
                            JSONObject wallpaper = mainArray.getJSONObject(i);
                            WallpapersList list = new WallpapersList(
                                    wallpaper.getString("link"),
                                    wallpaper.getString("thumb"), wallpaper.getInt("ID"),
                                    wallpaper.getString("Source"),
                                    wallpaper.getString("Designer"),
                                    wallpaper.getString("Profile")
                            );
                            //String links = wallpaper.getString("link");
                            wallpapers.add(list);
                            //Toast.makeText(Home.this, "Wallpapers are updated!", Toast.LENGTH_SHORT).show();
                        }
                    } else if (mainArray.length() < storeArray.length()) {
                        wallpapers.clear();
                        for (int i = 0; i < mainArray.length() - storeArray.length(); i++) {
                            JSONObject wallpaper = mainArray.getJSONObject(i);
                            WallpapersList list = new WallpapersList(
                                    wallpaper.getString("link"),
                                    wallpaper.getString("thumb"), wallpaper.getInt("ID"),
                                    wallpaper.getString("Source"),
                                    wallpaper.getString("Designer"),
                                    wallpaper.getString("Profile")
                            );
                            //String links = wallpaper.getString("link");
                            wallpapers.add(list);
                            adapter.notifyItemInserted(i);
                            adapter.notifyItemRangeChanged(i, mainArray.length());
                        }

                    } else {
                        Toast.makeText(Home.this, "No new wallpapers available right now.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        RequestQueue requestqueue = Volley.newRequestQueue(getApplicationContext());
        requestqueue.add(stringrequest);

        StringRequest stringrequest2 = new StringRequest(Request.Method.GET, AppConfig.CAROUSEL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject mainObj = new JSONObject(response);
                    JSONArray mainArray = mainObj.getJSONArray("wallpapers");
                    if (mainArray.length() > carouselList.size()) {
                        carouselList.clear();
                        for (int i = 0; i < mainArray.length(); i++) {
                            JSONObject wallpaper = mainArray.getJSONObject(i);
                            CarouselList list = new CarouselList(
                                    wallpaper.getString("link"),
                                    wallpaper.getInt("ID"),
                                    wallpaper.getString("Source"),
                                    wallpaper.getString("Designer"),
                                    wallpaper.getString("Profile")
                            );
                            //String links = wallpaper.getString("link");
                            carouselList.add(list);
                            //Toast.makeText(Home.this, "Wallpaper list is updated!", Toast.LENGTH_SHORT).show();

                        }
                    } else if (mainArray.length() < storeArray.length()) {
                        carouselList.clear();
                        for (int i = 0; i < mainArray.length(); i++) {
                            JSONObject wallpaper = mainArray.getJSONObject(i);
                            CarouselList list = new CarouselList(
                                    wallpaper.getString("link"),
                                    wallpaper.getInt("ID"),
                                    wallpaper.getString("Source"),
                                    wallpaper.getString("Designer"),
                                    wallpaper.getString("Profile")
                            );
                            //String links = wallpaper.getString("link");
                            carouselList.add(list);
                        }

                        //Toast.makeText(Home.this, "No new wallpapers available right now.", Toast.LENGTH_SHORT).show();
                    }
                    else{

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestqueue2 = Volley.newRequestQueue(getApplicationContext());
        requestqueue2.add(stringrequest2);

    }

    private void getCarouselData() {

        progressDialog.show();
        StringRequest stringrequest = new StringRequest(Request.Method.GET, AppConfig.CAROUSEL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject mainObj = new JSONObject(response);
                    JSONArray mainArray = mainObj.getJSONArray("wallpapers");
                    for(int i = 0; i < mainArray.length(); i++){
                        JSONObject carousel = mainArray.getJSONObject(i);
                        CarouselList list = new CarouselList(
                                carousel.getString("link"),
                                carousel.getInt("ID"),
                                carousel.getString("Source"),
                                carousel.getString("Designer"),
                                carousel.getString("Profile")
                        );
                        carouselList.add(list);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
              getData();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestqueue = Volley.newRequestQueue(this);
        requestqueue.add(stringrequest);

    }


    private void refreshcontent() {
        final SwipeRefreshLayout swiperefresh = (SwipeRefreshLayout) findViewById(R.id.contenthome);

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

    private void requestPerm() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {}
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {PermissionListener dialogPermissionListener =
                            DialogOnDeniedPermissionListener.Builder
                                    .withContext(Home.this)
                                    .withTitle("Write External Storage Permission")
                                    .withMessage("Write External Storage permission is needed to set your wallpapers!")
                                    .withButtonText(android.R.string.ok)
                                    .withIcon(R.mipmap.ic_launcher)
                                    .build();}
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {}
                }).check();

    }

    private void getData() {

        StringRequest stringrequest = new StringRequest(Request.Method.GET, AppConfig.FETCH_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject mainObj = new JSONObject(response);
                    JSONArray mainArray = mainObj.getJSONArray("wallpapers");
                    storeArray = mainArray;
                    for(int i = 0; i < mainArray.length(); i++){
                        JSONObject wallpaper = mainArray.getJSONObject(i);
                        WallpapersList list = new WallpapersList(
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new WallpaperAdapter(Home.this, carouselList, wallpapers);
                adapter.notifyItemRangeChanged(1, storeArray.length());
                recyclerView.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestqueue = Volley.newRequestQueue(this);
        requestqueue.add(stringrequest);
        progressDialog.dismiss();
        //setupWindowAnimations();

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
        //getMenuInflater().inflate(R.menu.home, menu);
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
        drawer.closeDrawer(GravityCompat.START);
        if (id == R.id.nav_home) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_category) {
            drawer.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(Home.this, CategoryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
            checkInternet.cancelHandler();
        } else if (id == R.id.nav_pick) {
            PickRandom pickRandom = new PickRandom(Home.this);
            pickRandom.pickRandomWallpaper();
            checkInternet.cancelHandler();
        } else if (id == R.id.nav_share) {
            ShareLink shareLink = new ShareLink(this);
            shareLink.shareLink();
            checkInternet.cancelHandler();
        } else if (id == R.id.nav_feedback) {
            Feedback feedback = new Feedback(this);
            feedback.getFeedback();
            checkInternet.cancelHandler();
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
            checkInternet.cancelHandler();
        }
        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        checkInternet.checkConnection();
        navigationView.getMenu().getItem(0).setChecked(true);
        int size = navigationView.getMenu().size();
        for(int i = 1; i < size; i++){
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }

    @Override
    protected void onPause(){
        checkInternet.cancelHandler();
        super.onPause();
    }
}


