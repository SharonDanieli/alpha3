package com.example.alpha3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthMultiFactorException;
import com.google.firebase.auth.MultiFactorAssertion;
import com.google.firebase.auth.MultiFactorResolver;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.PhoneMultiFactorGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SignInTwoFactors extends AppCompatActivity {

    EditText emailText, passwordText;
    private static final int RC_SIGN_IN = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_two_factors);
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
    }

    public void Twofactor(View view) {
        if (emailText.getText().toString().isEmpty() || passwordText.getText().toString().isEmpty()) {
            if (emailText.getText().toString().isEmpty()) {
                emailText.setError("Please enter your Email adress");
            }
            if (passwordText.getText().toString().isEmpty()) {
                passwordText.setError("Please enter your password");
            }
        }
        else {
            final String email = emailText.getText().toString();
            final String password = passwordText.getText().toString();
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Choose authentication providers
                        List<AuthUI.IdpConfig> providers = Arrays.asList(
                                new AuthUI.IdpConfig.PhoneBuilder().build());
                        // Create and launch sign-in intent
                        startActivityForResult(
                                AuthUI.getInstance()
                                        .createSignInIntentBuilder()
                                        .setAvailableProviders(providers)
                                        .build(),
                                RC_SIGN_IN);
                    } else {
                        Toast.makeText(SignInTwoFactors.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                Intent t6 = new Intent(SignInTwoFactors.this, GameInfo.class);
                startActivity(t6);
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
    public boolean allDetailsProvided() {
        boolean valid = true;
        if (TextUtils.isEmpty(emailText.getText().toString())) {
            emailText.setError("Please enter your Email adress");
            valid = false;
        }
        else {
            emailText.setError(null);
        }
        if (TextUtils.isEmpty(passwordText.getText().toString())) {
            passwordText.setError("Please enter your password");
            valid = false;
        }
        else {
            passwordText.setError(null);
        }
        return valid;
    }
}