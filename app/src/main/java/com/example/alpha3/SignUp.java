package com.example.alpha3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    TextInputLayout scorerName1;
    TextInputEditText scorerName;

    DatabaseReference ref;
    FirebaseAuth auth;

    /**
     * Links the variables in Java to the components in xml and initializes the attributes.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        scorerName1 = findViewById(R.id.scorerName1);
        scorerName = findViewById(R.id.scorerName);

        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Users");
    }

    /**
     * Passes the user's details to the next Activity
     */
    public void toVerifyEmail(View view) {
        if (TextUtils.isEmpty(scorerName.getText().toString())) {
            scorerName1.setError("Please enter your name");
        }
        else {
            final String userName = scorerName.getText().toString();
            Intent t1 = new Intent(SignUp.this, WhichUser.class);
            t1.putExtra("name", userName);
            startActivity(t1);
        }
    }

    /**
     * Moves to the authentication module.
     */
    public void toSignInTwoFactors(View view) {
        Intent t = new Intent(this, SignInTwoFactors.class);
        startActivity(t);
    }
}
