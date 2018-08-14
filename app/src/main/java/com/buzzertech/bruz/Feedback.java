package com.buzzertech.bruz;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Prasad on 5/9/2017.
 */

public class Feedback {

    Context context;

    public Feedback(Context context) {
        this.context = context;
    }

    public void getFeedback(){
        String email = "mailbruz@gmail.com";
        String subject = "Feedback regarding Bruz";
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
//emailIntent.putExtra(Intent.EXTRA_HTML_TEXT, body); //If you are using HTML in your body text

        context.startActivity(Intent.createChooser(emailIntent, "Choose an email client"));
    }
}

