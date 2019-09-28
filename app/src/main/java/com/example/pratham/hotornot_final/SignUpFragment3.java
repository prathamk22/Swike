package com.example.pratham.hotornot_final;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment3 extends Fragment {


    public SignUpFragment3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up_fragment3, container, false);
        TextView textView =  view.findViewById(R.id.text);
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/avenir.otf");
        textView.setTypeface(typeface);
        return view;
    }

}
