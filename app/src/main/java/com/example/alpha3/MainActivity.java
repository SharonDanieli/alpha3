package com.example.alpha3;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    FirebaseAuth mAuth;
    DatabaseReference ref;
    Query query;

    boolean inPList;

    ListView list;
    List<Team> teamsList;
    List<Player> playersList;

    MaterialToolbar top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        top = findViewById(R.id.topAppBar);
        setSupportActionBar(top);

        list = findViewById(R.id.list);
        list.setOnItemClickListener(this);

        teamsList = new ArrayList<>();
        playersList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();

        inPList = false;
        initiate();

    }

    void initiate()
    {
        ref = FirebaseDatabase.getInstance().getReference("Game");
        refreshList();

        list.setOnCreateContextMenuListener(this);
    }

    private void refreshList() {
        inPList = false;
        query = ref.child("Teams");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                teamsList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                    teamsList.add(ds.getValue(Team.class));
                TeamList adp = new TeamList(MainActivity.this, teamsList);
                list.setAdapter(adp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sign_out_game_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String title = item.getTitle().toString();
        if (title.equals("Sign out"))
        {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // ...
                            Toast.makeText(MainActivity.this, "You have signed out", Toast.LENGTH_LONG).show();
                            Intent to = new Intent(MainActivity.this, SignUp.class);
                            startActivity(to);
                        }
                    });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add("Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String title = item.getTitle().toString();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;

        if (title.equals("Delete"))
        {
            if (inPList) {
                String id = playersList.get(index).id;
                ref.child("Players").child(id).removeValue();
            }
            else
            {
                int id = teamsList.get(index).num;
                ref.child("Teams").child("" + id).removeValue();
            }
        }

        return super.onContextItemSelected(item);
    }

    public void addPlayer(View view) {
        Intent t = new Intent(this, AddPlayer.class);
        startActivity(t);
    }

    public void addTeam(View view) {
        Intent t = new Intent(this, AddTeam.class);
        startActivity(t);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (!inPList) {
            inPList = true;
            int id = teamsList.get(i).num;
            Toast.makeText(this, "" + id, Toast.LENGTH_LONG).show();
            query = ref.child("Players").orderByChild("team").equalTo(id);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    playersList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren())//רץ על כל האיברים
                        playersList.add(ds.getValue(Player.class));
                    PlayerList adp = new PlayerList(MainActivity.this, playersList);
                    list.setAdapter(adp);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (inPList)
            refreshList();
        else
            super.onBackPressed();
    }
}