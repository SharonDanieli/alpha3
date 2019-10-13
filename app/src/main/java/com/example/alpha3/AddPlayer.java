package com.example.alpha3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddPlayer extends AppCompatActivity {

    EditText fname, lname, birth, idText, team, num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);

        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        birth = findViewById(R.id.birth);
        idText = findViewById(R.id.id);
        team = findViewById(R.id.team);
        num = findViewById(R.id.num);

    }

    public void submit(View view) {
        String fn = fname.getText().toString();
        String ln = lname.getText().toString();
        String b = birth.getText().toString();
        String id = idText.getText().toString();
        int t = Integer.parseInt(team.getText().toString());
        int n = Integer.parseInt(num.getText().toString());

        Player p = new Player(fn, ln, b, id, t, n, "");

        DatabaseReference r = FirebaseDatabase.getInstance().getReference("Game").child("Players");
        r.child(id).setValue(p);

        Toast.makeText(this, "Player has been added", Toast.LENGTH_LONG).show();
        finish();
    }
}
