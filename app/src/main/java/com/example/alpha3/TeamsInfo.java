package com.example.alpha3;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TeamsInfo extends AppCompatActivity {

    DatabaseReference r;

    Spinner select1, select2;
    ListView team1, team2;
    Button clearButton, signButton;

    List<String> teamList;
    List<Player> players1;
    List<Player> players2;
    ArrayAdapter<String> teamAdp;
    SignaturePad signatureCaptain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams_info);

        signatureCaptain = findViewById(R.id.signatureCaptain);
        clearButton = findViewById(R.id.clearButton);
        signButton = findViewById(R.id.signButton);

        signButton.setVisibility(View.INVISIBLE);
        clearButton.setVisibility(View.INVISIBLE);

        signatureCaptain.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                signButton.setVisibility(View.VISIBLE);
                clearButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onClear() {
                signButton.setVisibility(View.INVISIBLE);
                clearButton.setVisibility(View.INVISIBLE);
            }
        });


        select1 = findViewById(R.id.select1);
        select2 = findViewById(R.id.select2);
        team1 = findViewById(R.id.team1);
        team2 = findViewById(R.id.team2);

        teamList = new ArrayList<>();
        players1 = new ArrayList<>();
        players2 = new ArrayList<>();
        r = FirebaseDatabase.getInstance().getReference("Game");
        r.child("Teams").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                teamList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Team t = ds.getValue(Team.class);
                    teamList.add(t.num + " - " + t.name);
                }
                teamAdp = new ArrayAdapter<>(TeamsInfo.this, R.layout.support_simple_spinner_dropdown_item, teamList);
                select1.setAdapter(teamAdp);
                select2.setAdapter(teamAdp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        select1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!teamList.isEmpty())
                {
                    int id = Integer.parseInt(teamList.get(i).split(" - ")[0]);
                    r.child("Players").orderByChild("team").equalTo(id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            players1.clear();
                            for (DataSnapshot ds : dataSnapshot.getChildren())
                                players1.add(ds.getValue(Player.class));
                            PlayerList adp = new PlayerList(TeamsInfo.this, players1);
                            team1.setAdapter(adp);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        select2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!teamList.isEmpty())
                {
                    int id = Integer.parseInt(teamList.get(i).split(" - ")[0]);
                    r.child("Players").orderByChild("team").equalTo(id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            players2.clear();
                            for (DataSnapshot ds : dataSnapshot.getChildren())
                                players2.add(ds.getValue(Player.class));
                            PlayerList adp = new PlayerList(TeamsInfo.this, players2);
                            team2.setAdapter(adp);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void clear(View view) {
        signatureCaptain.clear();
        Toast.makeText(TeamsInfo.this, ":(", Toast.LENGTH_SHORT).show();

    }

    public void sign(View view) {
        Toast.makeText(TeamsInfo.this, ":D", Toast.LENGTH_SHORT).show();
        Intent t = new Intent(this, Set.class);
        t.putExtra("name1", teamList.get(select1.getSelectedItemPosition()));
        t.putExtra("name2", teamList.get(select2.getSelectedItemPosition()));
        startActivity(t);
    }

}
