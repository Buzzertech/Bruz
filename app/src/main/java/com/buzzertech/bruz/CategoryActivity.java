package com.buzzertech.bruz;

import android.app.ActivityOptions;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    List<CategoryList> categoryList = new ArrayList<>();
    CategoryAdapter adapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(false);
        navigationView.getMenu().getItem(1).setChecked(true);

        recyclerView = (RecyclerView) findViewById(R.id.catergoryView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        setCategoryList();
        setupWindowAnimations();
    }

    private void setCategoryList() {
        CategoryList catList;

        catList = new CategoryList(R.drawable.cat_all, R.string.cat_all);
        categoryList.add(catList);

        catList = new CategoryList(R.drawable.cat_animal, R.string.cat_animals);
        categoryList.add(catList);

        catList = new CategoryList(R.drawable.cat_best, R.string.cat_best);
        categoryList.add(catList);

        catList = new CategoryList(R.drawable.cat_car, R.string.cat_cars);
        categoryList.add(catList);

        catList = new CategoryList(R.drawable.cat_city, R.string.cat_cities);
        categoryList.add(catList);

        catList = new CategoryList(R.drawable.cat_love, R.string.cat_love);
        categoryList.add(catList);

        catList = new CategoryList(R.drawable.cat_movie, R.string.cat_movies);
        categoryList.add(catList);

        catList = new CategoryList(R.drawable.cat_nature, R.string.cat_nature);
        categoryList.add(catList);

        catList = new CategoryList(R.drawable.cat_quotes, R.string.cat_quotes);
        categoryList.add(catList);

        catList = new CategoryList(R.drawable.cat_sports, R.string.cat_sports);
        categoryList.add(catList);

        adapter = new CategoryAdapter(CategoryActivity.this, categoryList);
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
            Intent intent = new Intent(CategoryActivity.this, Home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_category) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_pick) {
            PickRandom pickRandom = new PickRandom(CategoryActivity.this);
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
        } else if (id == R.id.nav_about) {
            Intent about = new Intent(this, AboutUs.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(about, bundle);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
}

    private void setupWindowAnimations() {
        /*Explode explode = new Explode();
        explode.setDuration(300);
        explode.setStartDelay(50);
        getWindow().setExitTransition(explode);*/

        Slide exitSlide = new Slide();
        exitSlide.setSlideEdge(Gravity.LEFT);
        exitSlide.setDuration(270);
        exitSlide.setInterpolator(new AccelerateDecelerateInterpolator());

        getWindow().setExitTransition(exitSlide);
        getWindow().setAllowEnterTransitionOverlap(true);



        Slide enterSlide = new Slide();
        enterSlide.setSlideEdge(Gravity.LEFT);
        enterSlide.setDuration(270);
        enterSlide.setInterpolator(new OvershootInterpolator());
        enterSlide.setStartDelay(200);
        getWindow().setEnterTransition(enterSlide);
    }
}
