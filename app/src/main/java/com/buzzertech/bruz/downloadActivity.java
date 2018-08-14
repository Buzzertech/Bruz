package com.buzzertech.bruz;

import android.Manifest;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.transition.Slide;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ablanco.zoomy.Zoomy;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class downloadActivity extends AppCompatActivity  {


    private static final String TAG = "Hello";
    private static final int WRITE_STORAGE_PERMISSION = 1;
    Bitmap downloadedImage;
    Bitmap expImage;
    ImageView caretDown;
    SlideUp slideUp;
    SlideUp setWallSlide;
    SlideUp detailsDialog;

    String url;
    String source;
    String designer;
    String profile;
    int id;
    StringRequest stringrequest;
    RequestQueue requestqueue;

    ProgressDialog pDialog;

    String appurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        //Enter and Exit Transitions
        setupWindowAnimations();

        //Getting URL
        Intent intent = getIntent();
        url = intent.getExtras().getString("url");
        id = intent.getExtras().getInt("id");
        source = intent.getExtras().getString("source");
        designer = intent.getExtras().getString("designer");
        profile = intent.getExtras().getString("designersprofile");

        //Checking if bitmap already exists
        //checkBitmap();
        //Initializing layouts and buttons
        //Initialing Button for setting Wallpapers
        final ImageButton setWallpaper = (ImageButton) findViewById(R.id.setWall);
        final ImageButton getDetails = (ImageButton) findViewById(R.id.detailWall);
        //Initialing Button for sharing Wallpapers
        final ImageButton shareWallpaper = (ImageButton) findViewById(R.id.shareWall);
        final LinearLayout button = (LinearLayout) findViewById(R.id.infoBtn); //CaretUp button
        final View slideView = findViewById(R.id.slideView); //Panel to be slide up
        final ImageButton downloadWall = (ImageButton) findViewById(R.id.downloadWall);
        final RelativeLayout detailDialog = (RelativeLayout) findViewById(R.id.modalLayout);
        final FrameLayout closeBlock = (FrameLayout) findViewById(R.id.closeLay);
        caretDown = (ImageView) findViewById(R.id.caretDown); //Caret down button used in SlideUp Panel

        button.setTranslationY(300);
        //Initializing Wallpaper Imageview
        final ImageView wallpaperPrev = (ImageView) findViewById(R.id.wallpaperPre);
        final DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        final int width = displayMetrics.widthPixels;
        final int height = displayMetrics.heightPixels;
        //Loading wallpaper preview in imageview
        Glide.with(this).load(url).diskCacheStrategy(DiskCacheStrategy.NONE).override(width, height).placeholder(R.drawable.previewlarge)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, com.bumptech.glide.request.target.Target<GlideDrawable> target, boolean isFirstResource) {
                        Toast.makeText(downloadActivity.this, "We're currently not able to load the preview for you. Please try again in some time.", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, com.bumptech.glide.request.target.Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        return false;
                    }
                })
                .into(wallpaperPrev);

         slideUp = new SlideUp.Builder(slideView) // Building SlideUp Menu
                .withListeners(new SlideUp.Listener() {
                    @Override
                    public void onSlide(float percent) {
                        animateCaret(percent);
                    }

                    @Override
                    public void onVisibilityChanged(int visibility) {

                        if(visibility == View.VISIBLE){
                            if(button.getTranslationY() == 0){
                                    animateBtn();
                            }
                        }
                        if(visibility == View.GONE){

                            if(setWallSlide.isVisible() | detailsDialog.isVisible() ){

                            }
                            else {
                                animateBackBtn();
                            }
                             // Animating CaretUp button back to original state
                        }
                    }
                })
                .withStartState(SlideUp.State.HIDDEN)
                .withStartGravity(Gravity.BOTTOM)
                .withGesturesEnabled(true)
                .build();

        final LinearLayout slideView2 = (LinearLayout) findViewById(R.id.slideView2);
        setWallSlide = new SlideUp.Builder(slideView2)
                .withListeners(new SlideUp.Listener() {
                    @Override
                    public void onSlide(float percent) {

                    }

                    @Override
                    public void onVisibilityChanged(int visibility) {
                        if(visibility == View.GONE){
                            if(slideUp.isVisible() | detailsDialog.isVisible()){
                            }
                            else {
                                animateBackBtn();
                            }
                        }

                        if(visibility == View.VISIBLE){

                        }
                    }
                })
                .withStartState(SlideUp.State.HIDDEN)
                .withStartGravity(Gravity.TOP)
                .withGesturesEnabled(true)
                .withHideSoftInputWhenDisplayed(true)
                .build();

        detailsDialog = new SlideUp.Builder(detailDialog)
                .withListeners(new SlideUp.Listener() {
                    @Override
                    public void onSlide(float percent) {

                    }

                    @Override
                    public void onVisibilityChanged(int visibility) {
                        if(visibility == View.GONE){
                            if(slideUp.isVisible() | setWallSlide.isVisible()){

                            }
                            else {
                                animateBackBtn();
                            }
                        }
                    }
                })
                .withStartState(SlideUp.State.HIDDEN)
                .withStartGravity(Gravity.TOP)
                .withInterpolator(new FastOutSlowInInterpolator())
                .withGesturesEnabled(true)
                .build();

        final ImageView caretUp = (ImageView) findViewById(R.id.caretDown2);
        caretUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideUp.show();
                setWallSlide.hide();
            }
        });


        //Setting onClickListeners for caretDown button
        caretDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideUp.hide(); // Hiding SlideUp Panel
                animateCaretBack();// Animating the CaretDown Button back to its original state
            }
        });

        //Setting onClickListener for setting Wallpaper
        setWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(android.os.Build.VERSION.SDK_INT >= 24){
                    setWallSlide.show();

                }else {
                    // Method to set Wallpaper
                    SetWallpaperTask set = new SetWallpaperTask();
                    set.execute();
                    incrementDownload(String.valueOf(id));

                }
                slideUp.hide(); //Hiding SlideUp Panel
            }
        });

        shareWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWall();
            }
        });

        //Setting onClickListener for caretup button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideUp.show(); // Showing the slideUp panel when button is clicked
                animateBtn(); //animating the button to go off screen
            }
        });

        getDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsDialog.show();
                slideUp.hide();
            }
        });

        closeBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsDialog.hide();
                slideUp.show();
            }
        });
        ImageButton setHome = (ImageButton) findViewById(R.id.setHome);
        setHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetWallpaperTask set = new SetWallpaperTask();
                set.execute();
            }
        });

        ImageButton setLock = (ImageButton) findViewById(R.id.setLock);
        setLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetLockWallpaper set = new SetLockWallpaper();
                set.execute();
            }
        });

        downloadWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(downloadActivity.this);
                pDialog.setMessage("Downloading...");
                pDialog.show();
                final File fileURL = new File(Environment.getExternalStorageDirectory() + "/Bruz/" + id + ".jpg");
                final String downloadedFile = Environment.getExternalStorageDirectory() + "/Bruz/" + id + ".jpg";
                final Uri fileURL2 = FileProvider.getUriForFile(downloadActivity.this, downloadActivity.this.getApplicationContext().getPackageName() + ".provider", fileURL);
                if(!fileURL.exists()){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            downloadWall(fileURL2);
                        }
                        else{
                            pDialog.dismiss();
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    WRITE_STORAGE_PERMISSION);
                        }
                    }
                    else{
                        downloadWall(fileURL2);

                    }

                } else {
                    pDialog.dismiss();
                    final AlertDialog.Builder alert = new AlertDialog.Builder(downloadActivity.this);
                    alert.setMessage("The wallpaper you are trying to download has already been downloaded. Do you really want to download again ?");
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.setNeutralButton("Show in Gallery", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setDataAndType(fileURL2, "image/*");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(intent);
                        }
                    });
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                fileURL.delete();
                                downloadWall(fileURL2);
                            }
                    });
                    alert.create();
                    alert.show();
                }

            }
        });

        setDesignerName(designer);
        checkSource(source);
        setDesignersProfile();

        Zoomy.Builder builder = new Zoomy.Builder(this).target(wallpaperPrev);
        builder.register();

        CheckVerification();
    }

    @Override
    public void onBackPressed() {
        if(slideUp.isVisible()){
            slideUp.hide();
        }
        else if(setWallSlide.isVisible()){
            setWallSlide.hide();
        }
        else if(detailsDialog.isVisible()){
            detailsDialog.hide();
            slideUp.show();
        }
        else{
            super.onBackPressed();
        }
    }

    private void incrementDownload(final String id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.INCREMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DOWNLOAD", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("DOWNLOAD", "failed");
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put("id", id);
                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void CheckVerification() {
        stringrequest = new StringRequest(Request.Method.GET, AppConfig.VERIFIED_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject mainObj = new JSONObject(response);
                    JSONArray mainArray = mainObj.getJSONArray("verified");
                    Boolean verified = mainArray.toString().contains(designer);

                    if(Objects.equals(designer, ""))
                        verified = false;
                    if (verified) {
                        ImageView verifiedImage = (ImageView) findViewById(R.id.verifiedImage);
                        verifiedImage.setVisibility(View.VISIBLE);
                    } else {
                        ImageView verifiedImage = (ImageView) findViewById(R.id.verifiedImage);
                        verifiedImage.setVisibility(View.GONE);
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

        stringrequest.setTag(getApplicationContext());

        requestqueue = Volley.newRequestQueue(getApplicationContext());
        requestqueue.add(stringrequest);


    }


    private void setDesignersProfile() {
        ImageView profilePic = (ImageView) findViewById(R.id.designersProfile);
        if(Objects.equals(profile, "n/a") || Objects.equals(profile, "N/A")|| Objects.equals(profile, "")){
            profilePic.setImageDrawable(getDrawable(R.drawable.ic_profile));
        }else {
            Glide.with(downloadActivity.this).load(profile).diskCacheStrategy(DiskCacheStrategy.SOURCE).dontAnimate().into(profilePic);
        }
    }

    private void checkSource(final String source) {
        Button sourceBtn = (Button) findViewById(R.id.sourceBtn);

        if(Objects.equals(source, "unk") || Objects.equals(source, "Unk")){
            sourceBtn.setEnabled(false);
            sourceBtn.setText(getString(R.string.source_button));
            sourceBtn.setBackground(getDrawable(R.drawable.disabled_btn));

        }
        else{
            sourceBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ProgressDialog pDialog = new ProgressDialog(downloadActivity.this);
                    pDialog.setMessage("Loading Wallpaper's Source...");
                    pDialog.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pDialog.cancel();
                            Intent viewSource = new Intent(Intent.ACTION_VIEW, Uri.parse(source));
                            startActivity(viewSource);
                        }
                    }, 2000);

                   /* ToDo - Work and launch this by Version 1.5
                   Intent viewSource = new Intent(downloadActivity.this, SourceWebview.class);
                   viewSource.putExtra("sourceUrl", source);
                    startActivity(viewSource);*/
                }
            });
        }
    }

    private void setDesignerName(String designer) {
        TextView designerName = (TextView) findViewById(R.id.designerName);

        if(Objects.equals(designer, "N/A") || Objects.equals(designer, "n/a")){
            designerName.setText(getString(R.string.unknown_designer));
            designerName.setTextColor(ContextCompat.getColor(this , R.color.shadeColor));
        }
        else if(Objects.equals(designer, "")){
            designerName.setText(getString(R.string.unknown_designer));
            designerName.setTextColor(ContextCompat.getColor(this , R.color.shadeColor));
        }
        else{
            designerName.setText(designer);
        }
    }

    private void shareWall() {
        File fileURL = new File(Environment.getExternalStorageDirectory() + "/Bruz/" + id + ".jpg");
        if(!fileURL.exists()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    shareDWall();
                }
                else{
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            WRITE_STORAGE_PERMISSION);
                }
            }
            else{
                shareDWall();
            }
        } else {
            shareIntent(id);
        }
    }

    private void shareDWall() {
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/Bruz");

        if (!direct.exists()) {
            direct.mkdirs();
        }


        final DownloadManager mgr = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setTitle("Download Wallpaper")
                .setDescription(null)
                .setDestinationInExternalPublicDir("/Bruz", id + ".jpg");
        mgr.enqueue(request);

        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                String action = intent.getAction();

                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    shareIntent(id);
                }

            }
        };

        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void downloadWall(final Uri fileURL2) {
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/Bruz");

        if (!direct.exists()) {
            direct.mkdirs();
        }


        final DownloadManager mgr = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setTitle("Download Wallpaper")
                .setDescription(null)
                .setDestinationInExternalPublicDir("/Bruz", id + ".jpg");
        mgr.enqueue(request);

        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                String action = intent.getAction();

                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    pDialog.dismiss();
                    Intent newIntent = new Intent();
                    newIntent.setAction(Intent.ACTION_VIEW);
                    newIntent.setDataAndType(fileURL2, "image/*");
                    newIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(newIntent);
                    Log.d("DownloadManager", "Download done!");
                }

            }
        };

        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }


    private void shareIntent(int id) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Just a second,,");
        pDialog.show();

        AppLink appLink = new AppLink(this, id);
        appLink.getLink();

        pDialog.dismiss();
    }

    //Animating CaretDown
    private void animateCaretBack() {
        caretDown.animate().rotation(0).setInterpolator(new OvershootInterpolator()).setDuration(100).start();
    }
    //Animating Caret on the basis of panel drag
    private void animateCaret(float percent) {
        float rotate = percent * 180 / 100;
        caretDown.animate().rotation(rotate).setInterpolator(new FastOutSlowInInterpolator()).setDuration(100).start();
    }

    private void animateBtn() {
        LinearLayout button = (LinearLayout) findViewById(R.id.infoBtn);
        button.animate().translationY(300).rotation(0).setDuration(300).setInterpolator(new OvershootInterpolator()).start();
    }
    //Animating caretup button to its original state
    private void animateBackBtn() {
        LinearLayout button = (LinearLayout) findViewById(R.id.infoBtn);
        button.animate().translationY(0).setDuration(300).setInterpolator(new OvershootInterpolator()).start();
    }





    private class SetWallpaperTask extends AsyncTask<Object, Object, Void> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            // Runs on the UI thread
            // Do any pre-executing tasks here, for example display a progress bar
            progressDialog = new ProgressDialog(downloadActivity.this);
            progressDialog.setMessage("Setting wallpaper...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            Log.d(TAG, "About to set wallpaper...");
        }

        @Override
        protected Void doInBackground(Object... params) {
            // Runs on the background thread

            try {
                downloadedImage = Picasso.with(getApplicationContext())
                        .load(url)
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            wallpaperIntent();
            return null;
        }

        @Override
        protected void onPostExecute(Void res) {
            // Runs on the UI thread
            // Here you can perform any post-execute tasks, for example remove the
            // progress bar (if you set one).
            PushNotificaiton();
            progressDialog.dismiss();
            downloadedImage.recycle();
            Log.d(TAG, "Wallpaper set!");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                }
            }, 3000);

            incrementDownload(String.valueOf(id));
        }


        private void wallpaperIntent() {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            // get the height and width of screen
            int height = metrics.heightPixels;
            int width = metrics.widthPixels;

            try {
                wallpaperManager.setBitmap(downloadedImage);

                wallpaperManager.suggestDesiredDimensions(width, height);

                //Toast.makeText(this, "Wallpaper Set", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        private void PushNotificaiton() {


            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
            mBuilder.setSmallIcon(R.drawable.bruz)
                    .setContentTitle("Done!")
                    .setColor(ContextCompat.getColor(getApplicationContext(), R.color.baseColor))
                    .setContentText("Your wallpaper has been set!")
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL);


            Intent resultIntent = new Intent(getApplicationContext(), Home.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
            stackBuilder.addParentStack(Home.class);

            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
            mNotificationManager.notify(0, mBuilder.build());

        }

    }


    private class SetLockWallpaper extends AsyncTask<Object, Object, Void> {

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            // Runs on the UI thread
            // Do any pre-executing tasks here, for example display a progress bar
            progressDialog = new ProgressDialog(downloadActivity.this);
            progressDialog.setMessage("Setting wallpaper...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            Log.d(TAG, "About to set wallpaper...");
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Object... params) {
            // Runs on the background thread

            try {
                downloadedImage = Picasso.with(getApplicationContext())
                        .load(url)
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            lockwallpaperIntent();
            return null;
        }

        @Override
        protected void onPostExecute(Void res) {
            // Runs on the UI thread
            // Here you can perform any post-execute tasks, for example remove the
            // progress bar (if you set one).
            progressDialog.dismiss();
            downloadedImage.recycle();
            Log.d(TAG, "Wallpaper set!");

            incrementDownload(String.valueOf(id));
        }


        @RequiresApi(api = Build.VERSION_CODES.N)
        private void lockwallpaperIntent() {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            // get the height and width of screen
            int height = metrics.heightPixels;
            int width = metrics.widthPixels;

            try {
                wallpaperManager.setBitmap(downloadedImage,  null, true, WallpaperManager.FLAG_LOCK);

                wallpaperManager.suggestDesiredDimensions(width, height);


                 PushNotificaiton();
                //Toast.makeText(this, "Wallpaper Set", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void PushNotificaiton() {


            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
            mBuilder.setSmallIcon(R.drawable.bruz)
                    .setContentTitle("Done!")
                    .setColor(ContextCompat.getColor(getApplicationContext(), R.color.baseColor))
                    .setContentText("Your lock wallpaper has been set!")
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL);


            Intent resultIntent = new Intent(getApplicationContext(), Home.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
            stackBuilder.addParentStack(Home.class);

            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
            mNotificationManager.notify(0, mBuilder.build());

        }

    }

    private void setupWindowAnimations() {
        /*Explode explode = new Explode();
        explode.setDuration(300);
        explode.setStartDelay(50);
        getWindow().setExitTransition(explode);*/

        Slide exitSlide = new Slide();
        exitSlide.setSlideEdge(Gravity.BOTTOM);
        exitSlide.setDuration(270);
        exitSlide.setInterpolator(new AccelerateDecelerateInterpolator());

        getWindow().setExitTransition(exitSlide);


        Slide enterSlide = new Slide();
        enterSlide.setSlideEdge(Gravity.TOP);
        enterSlide.setDuration(270);
        enterSlide.setInterpolator(new AccelerateDecelerateInterpolator());
        getWindow().setEnterTransition(enterSlide);
    }

    @Override
    protected void onPause(){
        super.onPause();
    }
    @Override
    protected void onDestroy(){
        requestqueue.cancelAll(downloadActivity.this);
        if(downloadedImage != null) {
            downloadedImage.recycle();
        }
        finish();
        super.onDestroy();
    }

}
