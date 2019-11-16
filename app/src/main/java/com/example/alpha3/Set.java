package com.example.alpha3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Set extends AppCompatActivity {

    TextView name1, name2, set1, set2, time;
    Button points1, points2;
    Button[] playersl, players2;
    List<Player> playersList1, playersList2, playing1, playing2;
    RadioButton serve1, serve2;
    List<String> times;
    boolean prev1, prev2;

    DatabaseReference r = FirebaseDatabase.getInstance().getReference("Game").child("Players");

    int pt1, pt2, limit, s1, s2;

    public static final int LIMIT = 25;
    public static final int SETLIMIT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        name1 = findViewById(R.id.name1);
        name2 = findViewById(R.id.name2);
        points1 = findViewById(R.id.points1);
        points2 = findViewById(R.id.points2);
        set1 = findViewById(R.id.set1);
        set2 = findViewById(R.id.set2);
        serve1 = findViewById(R.id.serve1);
        serve2 = findViewById(R.id.serve2);
        time = findViewById(R.id.time);

        playersl = new Button[6];
        players2 = new Button[6];

        playersl[0] = findViewById(R.id.p11);
        playersl[1] = findViewById(R.id.p12);
        playersl[2] = findViewById(R.id.p13);
        playersl[3] = findViewById(R.id.p14);
        playersl[4] = findViewById(R.id.p15);
        playersl[5] = findViewById(R.id.p16);

        players2[0] = findViewById(R.id.p21);
        players2[1] = findViewById(R.id.p22);
        players2[2] = findViewById(R.id.p23);
        players2[3] = findViewById(R.id.p24);
        players2[4] = findViewById(R.id.p25);
        players2[5] = findViewById(R.id.p26);

        playersList1 = new ArrayList<>();
        playersList2 = new ArrayList<>();
        playing1 = new ArrayList<>();
        playing2 = new ArrayList<>();

        prev1 = false;
        prev2 = false;
        if ((int)(Math.random() * 2) == 0) {
            serve1.setChecked(true);
            prev1 = true;
        }
        else {
            serve2.setChecked(true);
            prev2 = true;
        }

        times = new ArrayList<>();
        saveTime();

        pt1 = 0;
        pt2 = 0;
        s1 = 0;
        s2 = 0;
        limit = LIMIT;

        points1.setEnabled(false);
        points2.setEnabled(false);

        Intent t = getIntent();
        if (t != null)
        {
            name1.setText(t.getStringExtra("name1"));
            name2.setText(t.getStringExtra("name2"));

            r.orderByChild("team").equalTo(Integer.parseInt(name1.getText().toString().split(" - ")[0])).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        Player player = ds.getValue(Player.class);
                        playersList1.add(player);
                    }
                    for (int i = 0; i < playersl.length; i++) {
                        playing1.add(playersList1.get(i));
                        playersl[i].setText("" + playersList1.get(i).num);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            r.orderByChild("team").equalTo(Integer.parseInt(name2.getText().toString().split(" - ")[0])).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        Player player = ds.getValue(Player.class);
                        playersList2.add(player);
                    }
                    for (int i = 0; i < players2.length; i++) {
                        playing2.add(playersList1.get(i));
                        players2[i].setText("" + playersList2.get(i).num);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        points1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pt1 >= limit - 1) {
                    s1++;
                    points1.setEnabled(false);
                    points2.setEnabled(false);
                }
                pt1++;
                if (pt1 == pt2 && pt2 >= LIMIT - 1)
                    limit++;
                points1.setText("" + pt1);

                serve1.setChecked(true);
                serve2.setChecked(false);

                if (prev1 != serve1.isChecked() && serve1.isChecked())
                {
                    Player p = playing1.remove(0);
                    playing1.add(p);
                    for (int i = 0; i < playersl.length; i++)
                        playersl[i].setText("" + playing1.get(i).num);
                }
                prev1 = true;
                prev2 = false;
            }
        });
        points2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pt2 >= limit - 1) {
                    s2++;
                    points1.setEnabled(false);
                    points2.setEnabled(false);
                }
                pt2++;
                if (pt1 == pt2 && pt2 >= LIMIT - 1)
                    limit++;
                points2.setText("" + pt2);

                serve1.setChecked(false);
                serve2.setChecked(true);

                if (prev2 != serve2.isChecked() && serve2.isChecked())
                {
                    Player p = playing2.remove(0);
                    playing2.add(p);
                    for (int i = 0; i < players2.length; i++)
                        players2[i].setText("" + playing2.get(i).num);
                }
                prev2 = true;
                prev1 = false;
            }
        });
    }

    public void saveTime() {
        Calendar c = Calendar.getInstance();
        times.add(c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE));
        time.setText(times.get(times.size() - 1));
    }

    public void checkWin()
    {
        Intent t = new Intent(this, Results.class);
        if (s1 == SETLIMIT) {
            t.putExtra("winner", name1.getText().toString());
            startActivity(t);
        }
        else if (s2 == SETLIMIT) {
            t.putExtra("winner", name2.getText().toString());
            startActivity(t);
        }
    }

    public void startSet(View view) {
        saveTime();
        pt1 = 0;
        pt2 = 0;
        set1.setText("" + s1);
        set2.setText("" + s2);
        points1.setText("" + pt1);
        points2.setText("" + pt2);
        limit = LIMIT;
        checkWin();
        points1.setEnabled(true);
        points2.setEnabled(true);
    }
}
