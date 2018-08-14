package com.buzzertech.bruz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Prasad on 5/8/2017.
 */

public class ShareLink {

    Context context;
    ProgressDialog pDialog;
    String appUrl;
    public String thisurl;

    public ShareLink(Context context) {
        this.context = context;
    }

    public void shareLink(){
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Just a second..");
        pDialog.show();
        StringRequest stringrequest = new StringRequest(Request.Method.GET, AppConfig.SHARE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                share(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestqueue = Volley.newRequestQueue(context);
        requestqueue.add(stringrequest);
    }

    public void share(String response) {
        pDialog.dismiss();
        try{
            JSONObject mainObj = new JSONObject(response);
            appUrl = mainObj.getString("AppLink");
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, "Download Bruz - a Super Awesome Wallpaper App for you, from the Play Store " + appUrl);
            context.startActivity(Intent.createChooser(i, "Share Bruz!"));

        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void getLink(){

    }
}
