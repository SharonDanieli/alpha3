package com.example.alpha3;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Set extends AppCompatActivity {
    BroadcastReceiver br = new BatteryLevelReceiver();
    IntentFilter filter;

    TextView name1, name2, set1, set2, time, setNum;
    int setNumber = 0;
    Button points1, points2, startSet, undo, to1, to2, sanctions1, sanctions2;
    Button[] playersl, players2;
    List<Integer> playersList1, playersList2, playing1, playing2;
    RadioButton serve1, serve2;
    List<String> times1, times2, sanctionsList1, sanctionsList2;
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
    ArrayList<String> points = new ArrayList<>();
    ArrayList<String> swaps1 = new ArrayList<>();
    ArrayList<String> swaps2 = new ArrayList<>();

    ArrayList<String> pointsDiv = new ArrayList<>();
    StringBuilder p1 = new StringBuilder("");
    StringBuilder p2 = new StringBuilder("");
    ArrayList<String> savePointsDiv = new ArrayList<>(); //שומר את התפלגות הנקודות של כל קבוצה בכל מערכה
    ArrayList<String> saveTimeSets = new ArrayList<>(); //שומר את זמן תחילת וסיום כל מערכה

    /**
     * Links the java variables to their overlapping components in xml, initializes the attributes, scans which team makes an opening stroke with the help of the Random class, locks the appropriate buttons, updates the player lists according to the existing players in the repository, and calls the {@link #setButtons()} method.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        this.registerReceiver(br, filter);

        hasStarted = false;

        letters = getResources().getStringArray(R.array.sanctions);

        positions = getResources().getStringArray(R.array.positions);

        r = FirebaseDatabase.getInstance().getReference("Game").child("Players");
        setNum = findViewById(R.id.setNum);

        name1 = findViewById(R.id.name1);
        name2 = findViewById(R.id.name2);
        points1 = findViewById(R.id.points1);
        points2 = findViewById(R.id.points2);
        startSet = findViewById(R.id.startSet);
        undo = findViewById(R.id.undo);
        set1 = findViewById(R.id.set1);
        set2 = findViewById(R.id.set2);
        serve1 = findViewById(R.id.serve1);
        serve2 = findViewById(R.id.serve2);
        time = findViewById(R.id.time);
        to1 = findViewById(R.id.to1);
        to2 = findViewById(R.id.to2);
        sanctions1 = findViewById(R.id.sanctions1);
        sanctions2 = findViewById(R.id.sanctions2);

        times1 = new ArrayList<>();
        times2 = new ArrayList<>();

        serve1.setClickable(true);
        serve2.setClickable(true);
        startSet.setEnabled(false);
        undo.setEnabled(false);
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
        if ((int) (Math.random() * 2) == 0) {
            serve1.setChecked(true);
            prev1 = true;
        } else {
            serve2.setChecked(true);
            prev2 = true;
        }

        sanctionsList1 = new ArrayList<>();
        sanctionsList2 = new ArrayList<>();

        pt1 = 0;
        pt2 = 0;
        s1 = 0;
        s2 = 0;
        limit = LIMIT;

        points1.setEnabled(false);
        points2.setEnabled(false);
        to1.setEnabled(false);
        to2.setEnabled(false);
        sanctions1.setEnabled(false);
        sanctions2.setEnabled(false);

        Intent t = getIntent();
        if (t != null) {
            name1.setText(t.getStringExtra("team1"));
            name2.setText(t.getStringExtra("team2"));

            r.orderByChild("team").equalTo(Integer.parseInt(name1.getText().toString().split(" - ")[0])).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    size1 = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
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
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
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

        /**
         * Updates the score with each click, creates an array of points breakdown in the set, and changes the order of the players in the order of volleyball positions each time the team switches to a starting stroke. After a winning team locks the appropriate buttons and pops up the Alert Dialog that verifies with the user that he has actually finished scoring the set. If the user chooses "yes" - the method calls for method:
         */
        points1.setOnClickListener(view -> {
            pt1++;
            points1.setText("" + pt1);
            StringBuilder space = new StringBuilder("");

            for (int i=0; i < String.valueOf(pt1).length(); i++)
                space.append("_");
            pointsDiv.add(pt1 + " ," + space);
            actions.push(TAKEOUT_POINT1);//when point is taken

            if (pt1 - 1 >= limit - 1) {
                s1++;
                points1.setEnabled(false);
                points2.setEnabled(false);
                to1.setEnabled(false);
                to2.setEnabled(false);
                sanctions1.setEnabled(false);
                sanctions2.setEnabled(false);

                LayoutInflater layoutInflater = LayoutInflater.from(this);
                View dialogView = layoutInflater.inflate(R.layout.dialog_end_set, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(dialogView);
                builder.setMessage("Are you sure you want to end the set?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        upload();
                        checkWin();
                        // If this is the end of set 4, allow the user to pick the serving group again
                        if (setNumber == 4) {
                            serve1.setClickable(true);
                            serve2.setClickable(true);
                        }
                        setButtons();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                        points1.setEnabled(true);
                        points2.setEnabled(true);
                        to1.setEnabled(true);
                        to2.setEnabled(true);
                        sanctions1.setEnabled(true);
                        sanctions2.setEnabled(true);
                        undoActions(view);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
            if (pt1 == pt2 && pt2 >= LIMIT - 1)
                limit++;

            serve1.setChecked(true);
            serve2.setChecked(false);

            if (prev1 != serve1.isChecked() && serve1.isChecked())//מחליפה את השחקנים לפי כיוון השעון
            {
                Integer p = playing1.remove(0);
                playing1.add(p);
                for (int i = 0; i < playersl.length; i++)
                    playersl[i].setText("" + playing1.get(i));
            }
            prev1 = true;
            prev2 = false;
        });

        /**
         * Updates the score with each click, creates an array of points breakdown in the set, and changes the order of the players in the order of volleyball positions each time the team switches to a starting stroke. After a winning team locks the appropriate buttons and pops up the Alert Dialog that verifies with the user that he has actually finished scoring the set. If the user chooses "yes" - the method calls for method:
         */
        points2.setOnClickListener(view -> {
            pt2++;
            points2.setText("" + pt2);

            StringBuilder space = new StringBuilder("");
            for (int i=0; i < String.valueOf(pt2).length(); i++)
                space.append("_");
            pointsDiv.add(space + " ," + pt2);

            actions.push(TAKEOUT_POINT2);//when point is taken

            if (pt2 - 1 >= limit - 1) {
                s2++;
                points1.setEnabled(false);
                points2.setEnabled(false);
                to1.setEnabled(false);
                to2.setEnabled(false);
                sanctions1.setEnabled(false);
                sanctions2.setEnabled(false);

                LayoutInflater layoutInflater = LayoutInflater.from(this);
                View dialogView = layoutInflater.inflate(R.layout.dialog_end_set, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(dialogView);
                builder.setMessage("Are you sure you want to end the set?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        upload();
                        checkWin();
                        // If this is the end of set 4, allow the user to pick the serving group again
                        if (setNumber == 4) {
                            serve1.setClickable(true);
                            serve2.setClickable(true);
                        }
                        setButtons();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                        points1.setEnabled(true);
                        points2.setEnabled(true);
                        to1.setEnabled(true);
                        to2.setEnabled(true);
                        sanctions1.setEnabled(true);
                        sanctions2.setEnabled(true);
                        undoActions(view);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }

            if (pt1 == pt2 && pt2 >= LIMIT - 1)
                limit++;

            serve1.setChecked(false);
            serve2.setChecked(true);

            if (prev2 != serve2.isChecked() && serve2.isChecked())//מחליפה את סדר השחקנים לפי כיוון השעון
            {
                int p = playing2.remove(0);
                playing2.add(p);
                for (int i = 0; i < players2.length; i++)
                    players2[i].setText("" + playing2.get(i));
            }
            prev2 = true;
            prev1 = false;
        });
    }

    /**
     * The method is in charge of placing the participating players: the players who open the game and the players who replace the players on the court.
     */
    private void setButtons() {
        for (int i = 0; i < playersl.length; i++) {
            final Button player = playersl[i];
            final String pos = positions[i];

            player.setOnClickListener(new View.OnClickListener() {
                /**
                 * Places the opening six players
                 */
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
                                int num = playersList1.remove(i);
                                player.setText("" + num);
                                if (playersList1.size() == size1 - TEAM_SIZE && playersList2.size() == size2 - TEAM_SIZE)
                                    addPlayers();
                            }
                        });
                        AlertDialog ad = adb.create();
                        ad.show();
                    } else {
                        if (hasStarted) {
                            AlertDialog.Builder adb = new AlertDialog.Builder(Set.this);
                            ArrayAdapter adp = new ArrayAdapter(Set.this, R.layout.support_simple_spinner_dropdown_item, playersList1);
                            adb.setAdapter(adp, new DialogInterface.OnClickListener() {
                                /**
                                 * The method is in charge of player replacements
                                 * @param i The index of the replaced player
                                 */
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    int num = playersList1.remove(i);
                                    int out = Integer.valueOf(text);
                                    playing1.remove(playing1.indexOf(out));
                                    insert(playersList1, Integer.valueOf(out));
                                    insert(playing1, num);

                                    player.setText("" + num);
                                }
                            });
                            AlertDialog ad = adb.create();
                            ad.show();
                        } else {
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
                                int num = playersList2.remove(i);
                                player.setText("" + num);
                                if (playersList1.size() == size1 - TEAM_SIZE && playersList2.size() == size2 - TEAM_SIZE)
                                    addPlayers();
                            }
                        });
                        AlertDialog ad = adb.create();
                        ad.show();
                    } else {
                        if (hasStarted) {
                            AlertDialog.Builder adb = new AlertDialog.Builder(Set.this);
                            ArrayAdapter adp = new ArrayAdapter(Set.this, R.layout.support_simple_spinner_dropdown_item, playersList2);
                            adb.setAdapter(adp, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    int num = playersList2.remove(i);
                                    int out = Integer.valueOf(text);
                                    playing2.remove(playing2.indexOf(out));
                                    insert(playersList2, Integer.valueOf(out));
                                    insert(playing2, num);

                                    player.setText("" + num);
                                }
                            });
                            AlertDialog ad = adb.create();
                            ad.show();
                        } else {
                            insert(playersList2, Integer.parseInt(text));
                            player.setText(pos);
                        }
                    }
                }
            });
        }
    }

    /**
     * The method is called after 6 players have been placed and it keeps their numbers in lists.
     */
    private void addPlayers() {//הפעולה נקראת מיד אחרי ששמים 6 שחקנים
        first1 = new ArrayList<>();
        first2 = new ArrayList<>();
        startSet.setEnabled(true);
        for (int i = 0; i < TEAM_SIZE; i++) {
            first1.add(Integer.parseInt(playersl[i].getText().toString()));
            first2.add(Integer.parseInt(players2[i].getText().toString()));
            playing1.add(Integer.parseInt(playersl[i].getText().toString()));
            playing2.add(Integer.parseInt(players2[i].getText().toString()));
        }
    }

    /**
     * The method puts the player in a sorted array of players.
     * @param l A list of players
     * @param num Players shirt number
     */
    public void insert(List<Integer> l, int num) //מכניסה את השחקן במקום הנכון (מערך ממוין)
    {
        int i = 0;
        int s = l.size();
        while (i < s) {
            if (num < l.get(i))
                break;
            i++;
        }
        l.add(i, num);
    }

    /**
     * The method is called at the beginning of a set. It saves the set start time, updates the current set number, initializes the variables, enables / disables the appropriate buttons, and calls the {@link #checkWin()} method that checks which team won the set.
     */
    public void startSet(View view) {
        String startTime = saveTime();
        time.append(startTime + "-");
        setNumber++;
        setNum.setText("SET " + setNumber);
        hasStarted = true;
        startSet.setEnabled(false);
        // Set the serve radio buttons to not be clickable
        serve1.setClickable(false);
        serve2.setClickable(false);
        //לשמור את פסקי הזמן של המערכה
        times1.clear();
        times2.clear();
        sanctionsList1.clear();
        sanctionsList2.clear();
        pt1 = 0;
        pt2 = 0;
        set1.setText("" + s1);
        set2.setText("" + s2);
        points1.setText("" + pt1);
        points2.setText("" + pt2);
        limit = LIMIT;
        to1.setEnabled(true);
        to2.setEnabled(true);
        sanctions1.setEnabled(true);
        sanctions2.setEnabled(true);
        to2.setEnabled(true);
        //checkWin();
        points1.setEnabled(true);
        points2.setEnabled(true);
    }

    public void firstServeClicked(View view) {
        serve1.setChecked(true);
        serve2.setChecked(false);
    }


    public void secondServeClicked(View view) {
        serve1.setChecked(false);
        serve2.setChecked(true);
    }

    /**
     * The method finds out what the current time is.
     * @return The current time.
     */
    public String saveTime() {//שומרת את הזמן
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return format.format(new Date().getTime());
    }

    /**
     * The method checks which team won the game.
     */
    public void checkWin() {
        Intent t = new Intent(this, Summary.class);
        t.putExtra("sheet", updateScoreSheet());
        if (s1 == SETLIMIT) {
            t.putExtra("winner", name1.getText().toString());
            startActivity(t);
            finish();
        } else if (s2 == SETLIMIT) {
            t.putExtra("winner", name2.getText().toString());
            startActivity(t);
            finish();
        }
    }

    /**
     * The method is called at the end of a set. It saves the set end time, saves the point distribution and initializes the variables. The operation uploads the set results to the database and information about the results of each team, through the classes
     * @see SetInfo
     * @see TeamResults
     */
    private void upload() {//הפעולה נקראת בסיום מערכה
        String endTime = saveTime();
        time.append(endTime);
        saveTimeSets.add(time.getText().toString());

        String[] sPointsDiv = new String[pointsDiv.size()];
        String[] p;

        for(int j=0; j< pointsDiv.size() ; j++) {
            sPointsDiv[j] = pointsDiv.get(j);
            p = sPointsDiv[j].split(",");
            p1.append(p[0]);
            p2.append(p[1]);
        }
        savePointsDiv.add(p1.toString());
        savePointsDiv.add(p2.toString());
        p1.setLength(0);
        p2.setLength(0);
        pointsDiv.clear();

        time.setText("");
        startSet.setEnabled(false);
        hasStarted = false;

        String id = getIntent().getStringExtra("id");
        points.add(pt1 + ":" + pt2);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Games/" + id).child("Results");
        resultsA.add(new TeamResults(
                first1,
                swaps1,
                (ArrayList<String>) times1,
                (ArrayList<String>) sanctionsList1
        ));
        resultsB.add(new TeamResults(
                first2,
                swaps2,
                (ArrayList<String>) times2,
                (ArrayList<String>) sanctionsList2
        ));
        SetInfo setInfo = new SetInfo(
                new ArrayList<Boolean>(),
                points,
                resultsA,
                resultsB
        );
        ref.setValue(setInfo);
    }

    /**
     * The method is called when the team takes time out
     */
    public void timeout1(View view) {
        if (times1.size() < 2) {
            times1.add(points1.getText().toString() + ":" + points2.getText().toString());
            actions.push(TAKEOUT_TIME1);//when timeout is taken
            if (times1.size() == 2)
                to1.setEnabled(false);
        }
        Log.e("times1", times1.toString());
    }

    /**
     * The method is called when the team takes time out
     */
    public void timeout2(View view) {
        if (times2.size() < 2) {
            times2.add(points2.getText().toString() + ":" + points1.getText().toString());
            actions.push(TAKEOUT_TIME2);//when timeout is taken
            if (times2.size() == 2)
                to2.setEnabled(false);
        }
        Log.e("times2", times2.toString());
    }

    /**
     * The method is called when the team receives a sanction
     */
    public void addSanction1(View view) {
        ArrayAdapter adp = new ArrayAdapter(Set.this, R.layout.support_simple_spinner_dropdown_item, letters);
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setAdapter(adp, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, final int i) {
                if (i == 0) {
                    sanctionsList1.add(letters[i] + ", " + pt1 + ":" + pt2);
                    actions.push(TAKEOUT_SANCTION1);//when delay sanction is taken
                } else {
                    final List<Integer> players = new ArrayList<>();
                    players.addAll(playersList1);
                    players.addAll(playing1);
                    Collections.sort(players); // מיון השחקנים לפי המספרים
                    AlertDialog.Builder adb = new AlertDialog.Builder(Set.this);
                    ArrayAdapter adp = new ArrayAdapter(Set.this, R.layout.support_simple_spinner_dropdown_item, players);

                    adb.setAdapter(adp, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int j) {
                            sanctionsList1.add(letters[i] + " - " + players.get(j) + ", " + pt1 + ":" + pt2);
                            actions.push(TAKEOUT_SANCTION1);//when sanction is taken
                            Log.e("sanctionsList1", sanctionsList1.toString());
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

    /**
     * The method is called when the team receives a sanction
     */
    public void addSanction2(View view) {
        ArrayAdapter adp = new ArrayAdapter(Set.this, R.layout.support_simple_spinner_dropdown_item, letters);
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setAdapter(adp, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, final int i) {
                if (i == 0) {
                    sanctionsList2.add(letters[i] + ", " + pt2 + ":" + pt1);
                    actions.push(TAKEOUT_SANCTION2);//when sanction is taken
                } else {
                    final List<Integer> players = new ArrayList<>();
                    players.addAll(playersList2);
                    players.addAll(playing2);
                    Collections.sort(players); // מיון השחקנים לפי המספרים
                    AlertDialog.Builder adb = new AlertDialog.Builder(Set.this);
                    ArrayAdapter adp = new ArrayAdapter(Set.this, R.layout.support_simple_spinner_dropdown_item, players);

                    adb.setAdapter(adp, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int j) {
                            sanctionsList2.add(letters[i] + " - " + players.get(j) + ", " + pt2 + ":" + pt1);
                            actions.push(TAKEOUT_SANCTION2);//when sanction is taken
                            Log.e("sanctionsList2", sanctionsList2.toString());
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

    Stack<Character> actions = new Stack<>(); //The stack contains the actions history
    public static final char TAKEOUT_TIME1 = '*';
    public static final char TAKEOUT_TIME2 = '%';
    public static final char TAKEOUT_SANCTION1 = '(';
    public static final char TAKEOUT_SANCTION2 = ')';
    public static final char TAKEOUT_POINT1 = '#';
    public static final char TAKEOUT_POINT2 = '^';

    /**
     * The method cancels the last action that was taken
     */
    public void undoActions(View view) {
        if (!actions.isEmpty())
            Toast.makeText(this, "no actions", Toast.LENGTH_SHORT).show();
        else {
            char action = actions.pop();

            switch (action) {
                case TAKEOUT_TIME1:
                    takeOutTime1();
                    break;
                case TAKEOUT_TIME2:
                    takeOutTime2();
                    break;
                case TAKEOUT_SANCTION1:
                    takeOutSanction1();
                    break;
                case TAKEOUT_SANCTION2:
                    takeOutSanction2();
                    break;
                case TAKEOUT_POINT1:
                    takeOutPoint1();
                    break;
                case TAKEOUT_POINT2:
                    takeOutPoint2();
                    break;
            }
        }
    }

    /**
     * Cancels the timeout the team has taken
     */

    public void takeOutTime1() {
        // remove from 1
        if (times1.size() < 2) {
            times1.remove(times1.size() - 1);
        }
        if (times1.size() == 2) {
            times1.remove(times1.size() - 1);
            to1.setEnabled(true);
        }
    }

    /**
     * Cancels the timeout the team has taken
     */

    public void takeOutTime2() {
        // remove from 2
        if (times2.size() < 2) {
            times2.remove(times2.size() - 1);
        }
        if (times2.size() == 2) {
            times2.remove(times2.size() - 1);
            to2.setEnabled(true);
        }
    }

    /**
     * Cancels the sanction the team received
     */

    public void takeOutSanction1() {
        // remove from 1
        sanctionsList1.remove(sanctionsList1.size() - 1);
    }

    /**
     * Cancels the sanction the team received
     */
    public void takeOutSanction2() {
        // remove from 2
        sanctionsList2.remove(sanctionsList2.size() - 1);
    }

    /**
     * Cancels the point reached
     */
    public void takeOutPoint1() {
        // remove from 1
        pt1--;
        points1.setText("" + pt1);
    }

    /**
     * Cancels the point reached
     */
    public void takeOutPoint2() {
        // remove from 2
        pt2--;
        points2.setText("" + pt2);
    }

    /**
     * Updates the HTML file.
     * @return The updated HTML file.
     */
    public String updateScoreSheet() {
        //get html content
        Intent a = getIntent();
        String htmlAsString = a.getStringExtra("sheet");
        StringBuilder htmlManipulation = new StringBuilder(htmlAsString);
        int countP = 0;
        for(int i=1; i<setNumber; i++) {
            htmlManipulation = htmlManipulation.replace(htmlManipulation.indexOf("pointsDiv" + i + "1"), htmlManipulation.indexOf("pointsDiv" + i + "1") + "pointsDiv1".length() + 1, savePointsDiv.get(countP));
            htmlManipulation = htmlManipulation.replace(htmlManipulation.indexOf("pointsDiv" + i + "2"), htmlManipulation.indexOf("pointsDiv" + i + "2") + "pointsDiv2".length() + 1, savePointsDiv.get(countP+1));
            htmlManipulation = htmlManipulation.replace(htmlManipulation.indexOf("timeSet" + i), htmlManipulation.indexOf("timeSet" + i) + "timeSet1".length(), saveTimeSets.get(i-1));
            countP+=2;
        }

        return htmlManipulation.toString();
    }
}