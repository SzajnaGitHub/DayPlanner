package com.inc.dayplanner.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inc.dayplanner.R;

public class AboutFragment extends Fragment {

    TextView mstv;
    TextView kctv;
    TextView infotv;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_about, container, false);

        mstv = view.findViewById(R.id.MSlinked);
        kctv = view.findViewById(R.id.KSlinked);
        infotv = view.findViewById(R.id.textInfo);


        String linkText = "Reach me at <a href='https://www.linkedin.com/in/marcin-szajna-54ab61175/'>LinkedIn</a> or <a href='https://github.com/SzajnaGitHub'>GitHub</a> ";
        mstv.setText(Html.fromHtml(linkText));
        mstv.setMovementMethod(LinkMovementMethod.getInstance());

        String linkText2 = "Reach me at <a href='https://www.linkedin.com/in/kacper-seweryn-539444158/'>LinkedIn</a> or <a href='https://github.com/CapseL98'>GitHub</a> ";
        kctv.setText(Html.fromHtml(linkText2));
        kctv.setMovementMethod(LinkMovementMethod.getInstance());

        infotv.setText("DayPlanner 1.0 was created in 01.07.2019 by Espresso inc. ");

        return  view;
    }

}
