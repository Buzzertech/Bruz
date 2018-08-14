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
 * Created by Prasad on 5/10/2017.
 */

public class FragmentOne extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_one, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AssetManager am = getActivity().getApplicationContext().getAssets();

        Typeface typeface = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "Montserrat-Regular.ttf"));
        TextView welcome = (TextView) getActivity().findViewById(R.id.welcome);
        welcome.setTypeface(typeface);

        TextView welcomep = (TextView) getActivity().findViewById(R.id.welcomepara);
        welcomep.setTypeface(typeface);

    }
}
