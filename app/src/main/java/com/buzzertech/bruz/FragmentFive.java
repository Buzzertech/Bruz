package com.buzzertech.bruz;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by Prasad on 5/11/2017.
 */

public class FragmentFive extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment5, container, false);

    }

    @SuppressWarnings("ResourceType")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AssetManager am = getActivity().getApplicationContext().getAssets();

        Typeface typeface = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "Montserrat-Regular.ttf"));

        Typeface typeface2 = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "Montserrat-Bold.ttf"));

        TextView allset = (TextView) getActivity().findViewById(R.id.ALLset);
        allset.setTypeface(typeface2);

        TextView happyBruzzing = (TextView) getActivity().findViewById(R.id.happyBruzzing);
        happyBruzzing.setTypeface(typeface);



    }

}
