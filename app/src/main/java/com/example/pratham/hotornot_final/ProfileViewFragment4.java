package com.example.pratham.hotornot_final;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProfileViewFragment4 extends Fragment {


    public ProfileViewFragment4() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_view_fragment4, container, false);
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/avenir.otf");
        TextView head = view.findViewById(R.id.head);
        TextView subHead = view.findViewById(R.id.subhead);
        head.setTypeface(typeface);
        subHead.setTypeface(typeface);
        return view;
    }

}
