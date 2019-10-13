package com.example.alpha3;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class PlayerList extends ArrayAdapter {
    public Activity context;
    public List<Player> data;

    public PlayerList(Activity context, List<Player> data)
    {
        super(context, R.layout.list_layout ,data);
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View v = inflater.inflate(R.layout.list_layout, null, true);
        TextView fname = v.findViewById(R.id.fname);
        TextView lname = v.findViewById(R.id.lname);
        TextView birth = v.findViewById(R.id.birth);
        TextView id = v.findViewById(R.id.id);
        TextView team = v.findViewById(R.id.team);
        TextView num = v.findViewById(R.id.num);
        Player p = this.data.get(position);
        fname.setText(p.fname);
        lname.setText(p.lname);
        birth.setText(p.birth);
        id.setText(p.id);
        team.setText(p.team);
        num.setText("" + p.num);

        return v;
    }
}