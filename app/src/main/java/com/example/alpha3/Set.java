package com.example.alpha3;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Set extends AppCompatActivity {

    TextView name1, name2, set1, set2, time;
    Button points1, points2, startSet, to1, to2;
    Button[] playersl, players2;
    List<Integer> playersList1, playersList2, playing1, playing2;
    RadioButton serve1, serve2;
    List<String> times1, times2, sanctions1, sanctions2;
    List<TeamResults> resultsA, resultsB;
    boolean prev1, prev2;
    String[] letters;
    boolean hasStarted;

    DatabaseReference r;

    int pt1, pt2, limit, s1, s2, size1, size2;

    String[] positions;

    public static final int LIMIT = 25;
    public static final int SETLIMIT = 3;
    public static final int TEAM_SIZE = 6;
    private List<Integer> first1;
    private List<Integer> first2;
    ArrayList<String> points;
    ArrayList<String> swaps1 = new ArrayList<String>();
    ArrayList<String> swaps2 = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        hasStarted = false;

        letters = getResources().getStringArray(R.array.sanctions);

        positions = getResources().getStringArray(R.array.positions);

        r = FirebaseDatabase.getInstance().getReference("Game").child("Players");

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

        resultsA = new ArrayList<>();
        resultsB = new ArrayList<>();

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

        sanctions1 = new ArrayList<>();
        sanctions2 = new ArrayList<>();

        points = new ArrayList<>();
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
            name1.setText(t.getStringExtra("team1"));
            name2.setText(t.getStringExtra("team2"));

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
                pt1++;
                points1.setText("" + pt1);
                if (pt1 - 1 >= limit - 1) {
                    s1++;
                    points1.setEnabled(false);
                    points2.setEnabled(false);

                    upload();
                }
                if (pt1 == pt2 && pt2 >= LIMIT - 1)
                    limit++;

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
                pt2++;
                points2.setText("" + pt2);
                if (pt2 - 1 >= limit - 1) {
                    s2++;
                    points1.setEnabled(false);
                    points2.setEnabled(false);

                    upload();
                }

                if (pt1 == pt2 && pt2 >= LIMIT - 1)
                    limit++;

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

    private void upload() {

        startSet.setEnabled(true);
        hasStarted = false;

        String id = getIntent().getStringExtra("id");
        points.add(pt1 + ":" + pt2);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Games/" + id).child("Results");
        resultsA.add(new TeamResults(
            first1,
            swaps1,
            (ArrayList<String>)times1,
            (ArrayList<String>) sanctions1
        ));
        resultsB.add(new TeamResults(
            first2,
            swaps2,
            (ArrayList<String>)times2,
            (ArrayList<String>) sanctions2
        ));
        SetInfo setInfo = new SetInfo(
                new ArrayList<Boolean>(),
                points,
                resultsA,
                resultsB
        );
        ref.setValue(setInfo);
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
                        if (hasStarted)
                        {
                            int out = Integer.valueOf(text);
                            playing1.remove(playing1.indexOf(out));
                            insert(playersList1, Integer.valueOf(out));
                            insert(playing1, Integer.parseInt(player.getText().toString()));
                        }
                        else {
                            insert(playersList1, Integer.parseInt(text));
                            player.setText(pos);
                        }
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

                                //if (!text.startsWith("I") && !text.startsWith("V"))//בודק אם בכפתור הוכנס שחקן


                                player.setText("" + num);
                                if (playersList1.size() == size1 - TEAM_SIZE && playersList2.size() == size2 - TEAM_SIZE)
                                    addPlayers();
                            }
                        });
                        AlertDialog ad = adb.create();
                        ad.show();
                    }
                    else {
                    if (hasStarted)
                    {
                        int out = Integer.valueOf(text);
                        playing2.remove(playing2.indexOf(out));
                        insert(playersList2, Integer.valueOf(out));
                        insert(playing2, Integer.parseInt(player.getText().toString()));
                    }
                    else {
                        insert(playersList2, Integer.parseInt(text));
                        player.setText(pos);
                    }
                }
                }
            });
        }
    }

    private void addPlayers() {
        first1 = new ArrayList<>();
        first2 = new ArrayList<>();
        startSet.setEnabled(true);
        for (int i = 0 ; i < TEAM_SIZE; i++) {
            first1.add(Integer.parseInt(playersl[i].getText().toString()));
            first2.add(Integer.parseInt(players2[i].getText().toString()));
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
        hasStarted = true;
        startSet.setEnabled(false);

        saveTime();
        //לשמור את פסקי הזמן של המערכה
        times1.clear();
        times2.clear();
        sanctions1.clear();
        sanctions2.clear();
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

    public void addSanction1(View view) {
        ArrayAdapter adp = new ArrayAdapter(Set.this, R.layout.support_simple_spinner_dropdown_item, letters);
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setAdapter(adp, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, final int i) {
                if (i == 3)
                    sanctions1.add(letters[i] + ", " + pt1 + ":" + pt2);
                else {
                    final List<Integer> players = new ArrayList<>();
                    players.addAll(playersList1);
                    players.addAll(playing1);
                    Collections.sort(players); // מיון השחקנים לפי המספרים
                    AlertDialog.Builder adb = new AlertDialog.Builder(Set.this);
                    ArrayAdapter adp = new ArrayAdapter(Set.this, R.layout.support_simple_spinner_dropdown_item, players);

                    adb.setAdapter(adp, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int j) {
                            sanctions1.add(letters[i] + " - " + players.get(j) + ", " + pt1 + ":" + pt2);
                            Log.e("sanctions1", sanctions1.toString());
                        }
                    });

                    AlertDialog ad = adb.create();
                    ad.show();
                }
            }
        });
        AlertDialog ad = adb.create();
        ad.show();


        // sanctions1.add(points1.getText().toString() + ":" + points2.getText().toString());
    }
    public void addSanction2(View view) {
        // sanctions2.add(points1.getText().toString() + ":" + points2.getText().toString());
        ArrayAdapter adp = new ArrayAdapter(Set.this, R.layout.support_simple_spinner_dropdown_item, letters);
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setAdapter(adp, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, final int i) {
                if (i == 3)
                    sanctions2.add(letters[i] + ", " + pt2 + ":" + pt1);
                else {
                    final List<Integer> players = new ArrayList<>();
                    players.addAll(playersList2);
                    players.addAll(playing2);
                    Collections.sort(players); // מיון השחקנים לפי המספרים
                    AlertDialog.Builder adb = new AlertDialog.Builder(Set.this);
                    ArrayAdapter adp = new ArrayAdapter(Set.this, R.layout.support_simple_spinner_dropdown_item, players);

                    adb.setAdapter(adp, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int j) {
                            sanctions2.add(letters[i] + " - " + players.get(j) + ", " + pt2 + ":" + pt1);
                            Log.e("sanctions2", sanctions2.toString());
                        }
                    });

                    AlertDialog ad = adb.create();
                    ad.show();
                }
            }
        });

        AlertDialog ad = adb.create();
        ad.show();
    }
}
