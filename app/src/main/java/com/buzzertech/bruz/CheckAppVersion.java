package com.buzzertech.bruz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
 * Created by Prasad on 5/9/2017.
 */

public class CheckAppVersion {

    Context context;
    String versionCode = BuildConfig.VERSION_NAME;
    public static final String BRUZ_PREFERENCES = "MyPrefs" ;
    public static final String APP_VERISON = "nameKey";

    public CheckAppVersion(Context context) {
        this.context = context;
    }



   public void checkVersion(){
       StringRequest stringrequest = new StringRequest(Request.Method.GET, AppConfig.APP_VERSION_URL, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {

               try {
                   JSONObject mainObj = new JSONObject(response);
                   String appversion = mainObj.getString("AppVersion");
                   String appLink = mainObj.getString("AppLink");
                   if(!Objects.equals(versionCode, appversion)){
                       showModal(appversion, appLink);
                   }
               }catch (JSONException e){
                   e.printStackTrace();
               }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {

           }
       });

       RequestQueue requestqueue = Volley.newRequestQueue(context);
       requestqueue.add(stringrequest);
   }

    private Dialog showModal(String appversion, final String appLink) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("A new update is available for Bruz: v" + appversion)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(appLink));
                        context.startActivity(i);
                    }
                })
                .setNegativeButton("Remind me later", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        builder.setCancelable(false);
        builder.show();
        return builder.create();
    }
}
