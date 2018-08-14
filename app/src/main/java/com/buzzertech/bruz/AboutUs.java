package com.buzzertech.bruz;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class AboutUs extends AppCompatActivity {

    //Setting @variables for social media <ImageViews>
    ImageView twitter, mail, youtube, website;;

    //@Variable for <textview> displaying version of the app
    TextView version;

    // @Variables for <Cardview>
    CardView contributors, copyright;

    // Strings for storing links
    String Twitter, Youtube, Website;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        //Init
        twitter = (ImageView) findViewById(R.id.twitter_ic);
        youtube = (ImageView) findViewById(R.id.youtube_ic);
        website = (ImageView) findViewById(R.id.website_ic);
        mail = (ImageView) findViewById(R.id.mail_ic);

        version = (TextView) findViewById(R.id.versionText);

        contributors = (CardView) findViewById(R.id.cardFour);

        copyright = (CardView) findViewById(R.id.cardFive);

        getLinks();

        CheckAppVersion checkversion = new CheckAppVersion(this);
        version.setText("v" + checkversion.versionCode);

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(Twitter));
                startActivity(i);
            }
        });

        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(Youtube));
                startActivity(i);
            }
        });

        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(Website));
                startActivity(i);
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = "contactbuzzertech@gmail.com";
                String subject = "";
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                startActivity(Intent.createChooser(emailIntent, "Choose an email client"));
            }
        });

        contributors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AboutUs.this);
                builder.create();
                builder.setTitle("Contributors");
                builder.setMessage("\u2022 Prasad Nayak (UI Designer, App Developer, Owner)\n" +
                        "\n" +
                        "\u2022 Himanshu Kharkar (Contributor - Managing list of wallpapers)");
                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AboutUs.this);
                builder.create();
                builder.setTitle("Copyright & Policies");
                builder.setMessage("This app doesn't owns any of the wallpapers included in the list. All the possible copyright owners have been " +
                        "credited in the details section of the wallpaper view section. All the image rights are subjected to their respective owners. If you are the owner of any copyrighted image included " +
                        "in the list, then just shoot me a mail, and we'll resolve the issue.");
                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
        setupWindowAnimations();
    }

    private void getLinks() {
        StringRequest stringrequest = new StringRequest(Request.Method.GET, AppConfig.SOCIAL_LINKS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject mainObj = new JSONObject(response);
                    Twitter = mainObj.getString("Twitter");
                    Youtube = mainObj.getString("YouTube");
                    Website = mainObj.getString("Website");

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        RequestQueue requestqueue = Volley.newRequestQueue(this);
        requestqueue.add(stringrequest);
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


        Slide enterSlide = new Slide();
        enterSlide.setSlideEdge(Gravity.LEFT);
        enterSlide.setDuration(270);
        enterSlide.setInterpolator(new AccelerateDecelerateInterpolator());
        getWindow().setEnterTransition(enterSlide);
    }
}
