package com.example.pratham.hotornot_final;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EditProfileAdapter extends BaseAdapter {
    UserInfo userInfo;
    Context context;
    LayoutInflater inflater;
    String[] fields = {"Name", "Username","Uploads","Website","Bio","Mobile","Gender"};

    public EditProfileAdapter(UserInfo userInfo, Context context) {
        this.userInfo = userInfo;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return fields.length;
    }

    @Override
    public Object getItem(int position) {
        return fields[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.editprofilelistview, parent,false);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/avenir.otf");
            TextView field = convertView.findViewById(R.id.field);
            TextView value = convertView.findViewById(R.id.values);
            field.setTypeface(typeface);
            value.setTypeface(typeface);
            field.setText(fields[i]);
            switch (i){
                case 0:value.setText(userInfo.getName());
                    break;
                case 1:value.setText(userInfo.getUsername());
                    break;
                case 2:value.setText(userInfo.getTotalImages());
                    break;
                case 3:value.setText(userInfo.getWebsite());
                    break;
                case 4:value.setText(userInfo.getBio());
                    break;
                case 5:value.setText(userInfo.getMobile());
                    break;
                case 6:value.setText(userInfo.getGender());
                    break;
            }
        return convertView;
    }
}
