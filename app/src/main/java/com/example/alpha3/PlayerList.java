package com.example.alpha3;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Determines how groups are displayed in the list
 */

public class PlayerList extends ArrayAdapter {
    public Activity context;
    public List<Player> data;

    /**
     *
     * @param context The screen where the Array adapter is implemented
     * @param data A list containing the player details
     */

    public PlayerList(Activity context, List<Player> data)
    {
        super(context, R.layout.list_layout ,data);
        this.context = context;
        this.data = data;
    }

    /**
     *
     * @return the list view
     */

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View v = inflater.inflate(R.layout.list_layout, null, true);
        TextView fname = v.findViewById(R.id.fname);
        TextView lname = v.findViewById(R.id.lname);
        TextView num = v.findViewById(R.id.num);
        Player p = this.data.get(position);
        fname.setText(p.fname);
        lname.setText(p.lname);
        num.setText("" + p.num);

        return v;
    }
}