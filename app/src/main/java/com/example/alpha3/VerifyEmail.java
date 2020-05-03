package com.example.alpha3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.Arrays;
import java.util.List;

public class VerifyEmail extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    final String TAG = "LOGIN";

    /**
     * Initializes the attributes.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_mail);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    /**
     * Sends verification email to the email address the user entered.
     */
    public void sendAgain(View view) {
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(VerifyEmail.this, "Email verification sent", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(VerifyEmail.this, "Failed to send Email verification", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Passes the user's details to the next Activity
     */
    public void next(View view) {
        user.reload();
        if (user.isEmailVerified())
        {
            Intent t3 = new Intent(VerifyEmail.this, SignUpVerifyPhone.class);
            Intent intent = getIntent();
            String name = intent.getStringExtra("name");
            String email = intent.getStringExtra("email");
            boolean ifAuthorized = intent.getExtras().getBoolean("ifAuthorized");
            t3.putExtra("name", name);
            t3.putExtra("email", email);
            t3.putExtra("ifAuthorized", ifAuthorized);
            startActivity(t3);
            finish();
        }
        else
        {
            Toast.makeText(this, "Email not verified", Toast.LENGTH_LONG).show();
        }
    }
}
