package com.example.alpha3;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Set extends AppCompatActivity {

    TextView name1, name2, set1, set2, time;
    Button points1, points2, startSet, to1, to2;
    Button[] playersl, players2;
    List<Integer> playersList1, playersList2, playing1, playing2;
    RadioButton serve1, serve2;
    List<String> times1, times2, times;
    boolean prev1, prev2;

    DatabaseReference r = FirebaseDatabase.getInstance().getReference("Game").child("Players");

    int pt1, pt2, limit, s1, s2, size1, size2;

    String[] positions;

    public static final int LIMIT = 25;
    public static final int SETLIMIT = 3;
    public static final int TEAM_SIZE = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        positions = getResources().getStringArray(R.array.positions);

        name1 = findViewById(R.id.name1);
        name2 = findViewById(R.id.name2);
        points1 = findViewById(R.id.points1);
        points2 = findViewById(R.id.points2);
        startSet = findViewById(R.id.startSet);
        set1 = findViewById(R.id.set1);
        set2 = findViewById(R.id.set2);
        serve1 = findViewById(R.id.serve1);
        serve2 = findViewById(R.id.serve2);
        time = findViewById(R.id.time);
        to1 = findViewById(R.id.to1);
        to2 = findViewById(R.id.to2);

        times1= new ArrayList<>();
        times2= new ArrayList<>();


        startSet.setEnabled(false);
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
                    size1 = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        Player player = ds.getValue(Player.class);
                        insert(playersList1, player.num);
                        size1++;
                    }

                    setButtons();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            r.orderByChild("team").equalTo(Integer.parseInt(name2.getText().toString().split(" - ")[0])).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    size2 = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        Player player = ds.getValue(Player.class);
                        insert(playersList2, player.num);
                        size2++;
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
                    Integer p = playing1.remove(0);
                    playing1.add(p);
                    for (int i = 0; i < playersl.length; i++)
                        playersl[i].setText("" + playing1.get(i));
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
                    int p = playing2.remove(0);
                    playing2.add(p);
                    for (int i = 0; i < players2.length; i++)
                        players2[i].setText("" + playing2.get(i));
                }
                prev2 = true;
                prev1 = false;
            }
        });
    }



    private void setButtons() {
        for (int i = 0; i < playersl.length; i++) {
            final Button player = playersl[i];
            final String pos = positions[i];
            player.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String text = player.getText().toString();
                    if (text.startsWith("I") || text.startsWith("V"))//בודק אם בכפתור הוכנס שחקן
                    {
                        AlertDialog.Builder adb = new AlertDialog.Builder(Set.this);
                        ArrayAdapter adp = new ArrayAdapter(Set.this, R.layout.support_simple_spinner_dropdown_item, playersList1);
                        adb.setAdapter(adp, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // String text = player.getText().toString();
                                int num = playersList1.remove(i);

                                //if (!text.startsWith("I") && !text.startsWith("V"))//בודק אם בכפתור הוכנס שחקן


                                player.setText("" + num);

                                if (playersList1.size() == size1 - TEAM_SIZE && playersList2.size() == size2 - TEAM_SIZE)
                                    addPlayers();
                            }
                        });
                        AlertDialog ad = adb.create();
                        ad.show();
                    } else {
                        insert(playersList1, Integer.parseInt(text));
                        player.setText(pos);
                    }
                }
            });
        }
        for (int i = 0; i < players2.length; i++) {
            final Button player = players2[i];
            final String pos = positions[i];
            player.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String text = player.getText().toString();
                    if (text.startsWith("I") || text.startsWith("V"))//בודק אם בכפתור הוכנס שחקן
                    {
                        AlertDialog.Builder adb = new AlertDialog.Builder(Set.this);
                        ArrayAdapter adp = new ArrayAdapter(Set.this, R.layout.support_simple_spinner_dropdown_item, playersList2);
                        adb.setAdapter(adp, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // String text = player.getText().toString();
                                int num = playersList2.remove(i);

                                /*if (!text.startsWith("I") && !text.startsWith("V"))//בודק אם בכפתור הוכנס שחקן
                                    insert(playersList2, Integer.parseInt(text));*/

                                player.setText("" + num);

                                if (playersList1.size() == size1 - TEAM_SIZE && playersList2.size() == size2 - TEAM_SIZE)
                                    addPlayers();
                            }
                        });
                        AlertDialog ad = adb.create();
                        ad.show();
                    }
                    else {
                        insert(playersList2, Integer.parseInt(text));
                        player.setText(pos);
                    }
                }
            });
        }
    }

    private void addPlayers() {
        startSet.setEnabled(true);
        for (int i = 0 ; i < TEAM_SIZE; i++) {
            playing1.add(Integer.parseInt(playersl[i].getText().toString()));
            playing2.add(Integer.parseInt(players2[i].getText().toString()));
            // playersl[i].setEnabled(false);
            // players2[i].setEnabled(false);
        }
    }

    public void saveTime() {
        /*Calendar c = Calendar.getInstance();
        times.add(c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE));
        time.setText(times.get(times.size() - 1));*/
        SimpleDateFormat format=new SimpleDateFormat("HH:mm", Locale.getDefault());
        time.setText(format.format(new Date().getTime()));
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

    public void insert(List<Integer> l, int num) //מכניסה את השחקן במקום הנכון (מערך ממוין)
    {
        int i = 0;
        int s = l.size();
        while (i < s)
        {
            if (num < l.get(i))
                break;
            i++;
        }
        l.add(i, num);
    }

    public void startSet(View view) {
        saveTime();
        //לשמור את פסקי הזמן של המערכה
        times1.clear();
        times2.clear();
        pt1 = 0;
        pt2 = 0;
        set1.setText("" + s1);
        set2.setText("" + s2);
        points1.setText("" + pt1);
        points2.setText("" + pt2);
        limit = LIMIT;
        to1.setEnabled(true);
        to2.setEnabled(true);
        checkWin();
        points1.setEnabled(true);
        points2.setEnabled(true);
    }

    public void timeout1(View view) {
        if (times1.size() < 2) {
            times1.add(points1.getText().toString() + ":" + points2.getText().toString());
            if (times1.size() == 2)
                to1.setEnabled(false);
        }Log.e("times1", times1.toString());
    }

    public void timeout2(View view) {
        if (times2.size() < 2) {
            times2.add(points2.getText().toString() + ":" + points1.getText().toString());
            if (times2.size() == 2)
                to2.setEnabled(false);
        }
        Log.e("times2", times2.toString());
    }
}
