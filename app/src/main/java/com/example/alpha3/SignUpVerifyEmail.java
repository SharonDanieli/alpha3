package com.example.alpha3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.Arrays;
import java.util.List;

public class SignUpVerifyEmail extends AppCompatActivity {

    TextInputEditText emailText, passwordText;
    TextInputLayout emailText1, passwordText1;
    private static final String TAG = "Email verification";
    FirebaseAuth auth;

    /**
     * Links the variables in Java to the components in xml and initializes the attributes.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_verify_email);

        emailText1 = findViewById(R.id.emailText1);
        passwordText1 = findViewById(R.id.passwordText1);
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        auth = FirebaseAuth.getInstance();
    }

    /**
     * Creates a user with email and password in the database.
     */
    public void verifyEmail(View view) {
        if (emailText.getText().toString().isEmpty() || passwordText.getText().toString().isEmpty()) {
            if (emailText.getText().toString().isEmpty()) {
                emailText1.setError("Please enter your Email adress");
            }
            if (passwordText.getText().toString().isEmpty()) {
                passwordText1.setError("Please choose a password");
            }
        } else {
            final String email = emailText.getText().toString();
            final String password = passwordText.getText().toString();
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                /**
                 * Sends verification email to the email address the user entered.
                 */
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUpVerifyEmail.this, "Email verification sent", Toast.LENGTH_SHORT).show();
                                    Intent t2 = new Intent(SignUpVerifyEmail.this, VerifyEmail.class);
                                    Intent intent = getIntent();
                                    final String name = intent.getStringExtra("name");
                                    boolean ifAuthrized = intent.getExtras().getBoolean("ifAuthorized");
                                    t2.putExtra("name", name);
                                    t2.putExtra("email", email);
                                    t2.putExtra("ifAuthorized", ifAuthrized);
                                    startActivity(t2);
                                    finish();
                                } else {
                                    Log.e(TAG, "sendEmailVerification", task.getException());
                                    Toast.makeText(SignUpVerifyEmail.this, "Failed to send Email verification", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(SignUpVerifyEmail.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
