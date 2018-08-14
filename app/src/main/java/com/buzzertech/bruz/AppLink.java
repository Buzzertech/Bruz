package com.buzzertech.bruz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Prasad on 5/16/2017.
 */

public class AppLink {
    String appUrl;
    Context context;
    ProgressDialog pDialog;
    int id;

    public AppLink(Context context, int id) {
        this.context = context;
        this.id = id;
    }

    public void getLink(){
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
            i.setType("image/jpeg");
            i.putExtra(Intent.EXTRA_TEXT, "Download such awesome wallpapers for yourself from Bruz - " + appUrl);
            i.putExtra(Intent.EXTRA_STREAM, Uri.parse(Environment.getExternalStorageDirectory() + "/Bruz/" + id + ".jpg"));
            context.startActivity(Intent.createChooser(i, "Share Bruz!"));
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }
}
