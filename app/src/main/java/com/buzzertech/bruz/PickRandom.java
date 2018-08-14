package com.buzzertech.bruz;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Prasad on 5/8/2017.
 */

public class PickRandom {

    Context context;
    ProgressDialog pDialog;
    public PickRandom(){}
    public PickRandom(Context context){
        this.context = context;
    }

    public void pickRandomWallpaper(){
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Picking a wallpaper for you! \uD83D\uDE09");
        pDialog.show();

        StringRequest stringrequest = new StringRequest(Request.Method.GET, AppConfig.RANDOM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseWallpapers(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestqueue = Volley.newRequestQueue(context);
        requestqueue.add(stringrequest);
    }

    private void parseWallpapers(String response) {
        pDialog.dismiss();
        try {
            JSONObject mainObj = new JSONObject(response);
            JSONArray mainArray = mainObj.getJSONArray("wallpapers");
            JSONObject wallpaper = mainArray.getJSONObject(0);
            String url = wallpaper.getString("link");
            int id = wallpaper.getInt("ID");
            String source = wallpaper.getString("Source");
            String Designer = wallpaper.getString("Designer");
            String Profile = wallpaper.getString("Profile");
            Intent setWall = new Intent(context, downloadActivity.class);
            setWall.putExtra("url", url);
            setWall.putExtra("id", id);
            setWall.putExtra("source", source);
            setWall.putExtra("designer", Designer);
            setWall.putExtra("designersprofile", Profile);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((Activity) context).toBundle();
            context.startActivity(setWall, bundle);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
