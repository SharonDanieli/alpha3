package com.example.alpha3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WhichUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_which_user);
    }

    /**
     * Moves to the appropriate Activity depending on the type of user (scorer).
     */
    public void toScorer(View view) {
        Intent t2 = new Intent(WhichUser.this, SignUpVerifyEmail.class);
        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        t2.putExtra("name", name);
        t2.putExtra("ifAuthorized", false);
        startActivity(t2);
        finish();
    }

    /**
     * Moves to the appropriate screen depending on the type of user (authorized).
     */
    public void toAuthorized(View view) {
        Intent t2 = new Intent(WhichUser.this, SignUpVerifyEmail.class);
        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        t2.putExtra("name", name);
        t2.putExtra("ifAuthorized", true);
        startActivity(t2);
        finish();
    }
}
