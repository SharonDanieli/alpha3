package com.example.alpha3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddTeam extends AppCompatActivity {

    EditText nameText, numText, emailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);

        nameText = findViewById(R.id.nameText);
        numText = findViewById(R.id.numText);
        emailText = findViewById(R.id.emailTeam);
    }

    public void submit(View view) {
        final String name = nameText.getText().toString();
        final int num = Integer.parseInt(numText.getText().toString());
        final String email = emailText.getText().toString();
        final DatabaseReference r = FirebaseDatabase.getInstance().getReference("Game").child("Teams");

        r.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("" + num).getValue() == null)
                {
                    Team t = new Team(num, name, email);
                    r.child("" + num).setValue(t);
                    finish();
                    Toast.makeText(AddTeam.this, "Team added successfully", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(AddTeam.this, "Team already exists", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
