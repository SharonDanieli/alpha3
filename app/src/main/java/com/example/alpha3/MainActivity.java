package com.example.alpha3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private static final int RC_SIGN_IN = 4;
    FirebaseAuth mAuth;
    DatabaseReference ref;
    Query query;

    boolean inPList;

    ListView list;
    List<Team> teamsList;
    List<Player> playersList;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.list);
        list.setOnItemClickListener(this);

        teamsList = new ArrayList<>();
        playersList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();

        inPList = false;
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null)
            signIn();
        else
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

    private void signIn() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                initiate();
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Sign out");

        return super.onCreateOptionsMenu(menu);
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
                            signIn();
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
        if (currentUser != null)
        {
            Intent t = new Intent(this, AddPlayer.class);
            startActivity(t);
        }
    }

    public void addTeam(View view) {
        if (currentUser != null) {
            Intent t = new Intent(this, AddTeam.class);
            startActivity(t);
        }
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

    public void test(View view) {
        Intent t = new Intent(this, TeamsInfo.class);
        startActivity(t);
    }
}