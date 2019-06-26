package com.example.alpha3;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class DataList extends ArrayAdapter {
    public Activity context;
    public List<Data> data;

    public DataList(Activity context, List<Data> data)
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
        TextView data = v.findViewById(R.id.data);
        Data d = this.data.get(position);
        data.setText(d.data);

        return v;
    }
}