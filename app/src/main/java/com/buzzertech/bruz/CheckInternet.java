package com.buzzertech.bruz;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

import com.novoda.merlin.MerlinsBeard;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Prasad on 5/9/2017.
 */

public class CheckInternet {

    private static final String TAG = "HasInternetAccess";
    Context context;

    MerlinsBeard merlinsBeard;
    ExecutorService threadPoolExecutor = Executors.newSingleThreadExecutor();

    Runnable  runnable;
    Handler handler;
    Thread thread;
    Boolean notcancelled;

    public CheckInternet(Context context) {
        this.context = context;
    }

    public void checkConnection(){

        runnable = new Runnable() {
            @Override
            public void run() {
                if(!isDeviceOnline(context)){
                    Intent i = new Intent(context, NoInternet.class);
                    context.startActivity(i);
                }
                else if (isDeviceOnline(context)){
                    //continue with your stuff
                    handler.postDelayed(runnable, 10000);
                    Log.d(context.getClass().getSimpleName(), "Pushed Runnable in Activity!");
                }
            }
        };
        handler = new Handler();
        handler.postDelayed(runnable, 3000);
    }


    public void cancelHandler(){
        handler.removeCallbacks(runnable);
        Log.d(TAG, "Removed handler");
    }

    public static boolean isDeviceOnline(Context context) {

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isOnline = (networkInfo != null && networkInfo.isConnected());
        if(isOnline){}
        return isOnline;
    }

}
