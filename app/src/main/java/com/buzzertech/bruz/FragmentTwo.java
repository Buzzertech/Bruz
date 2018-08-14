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

public class FragmentTwo extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment2, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AssetManager am = getActivity().getApplicationContext().getAssets();

        Typeface typeface = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "Montserrat-Regular.ttf"));

        TextView Why = (TextView) getActivity().findViewById(R.id.why);
        TextView Bruz = (TextView) getActivity().findViewById(R.id.Bruz);
        TextView NO = (TextView) getActivity().findViewById(R.id.no);
        TextView ads = (TextView) getActivity().findViewById(R.id.ads);
        TextView no_adspara = (TextView) getActivity().findViewById(R.id.no_ads_p);

       /* Why.setTypeface(typeface);
        Bruz.setTypeface(typeface);
        NO.setTypeface(typeface);
        ads.setTypeface(typeface);
        no_adspara.setTypeface(typeface);

        TextView welcomep = (TextView) getActivity().findViewById(R.id.welcomepara);
        welcomep.setTypeface(typeface);*/
    }
}
