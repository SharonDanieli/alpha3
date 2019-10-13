package com.example.alpha3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddTeam extends AppCompatActivity {

    EditText nameText, numText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);

        nameText = findViewById(R.id.nameText);
        numText = findViewById(R.id.numText);
    }

    public void submit(View view) {
        String name = nameText.getText().toString();
        int num = Integer.parseInt(numText.getText().toString());
        DatabaseReference r = FirebaseDatabase.getInstance().getReference("Game").child("Teams");
        Team t = new Team(num, name);
        r.child("" + num).setValue(t);
        finish();
    }
}
