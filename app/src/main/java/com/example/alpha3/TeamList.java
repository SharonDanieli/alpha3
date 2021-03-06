package com.example.alpha3;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Determines how groups are displayed in the list
 */

    public class TeamList extends ArrayAdapter<Team> {
    private Activity context;
    private List<Team> teamsList;

    /**
     *
     * @param context The screen where the Array adapter is implemented
     * @param teamsList A list containing the team details
     */
    public TeamList(Activity context, List<Team> teamsList)
    {
        super(context, R.layout.team_list, teamsList); //מאתחל את התכונות ואת הפעולות של המחלקה היורשת
        this.context = context;
        this.teamsList = teamsList;
    }

    /**
     *
     * @return the list view
     */

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v;
        LayoutInflater inflater = context.getLayoutInflater();
        v = inflater.inflate(R.layout.team_list, null, true);
        TextView name = v.findViewById(R.id.name);
        TextView num = v.findViewById(R.id.num);
        Team pos = teamsList.get(position);
        name.setText(pos.name);
        num.setText("" + pos.num);

        return v;
    }
}
