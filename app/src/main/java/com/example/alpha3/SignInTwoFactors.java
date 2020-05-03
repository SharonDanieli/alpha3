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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthMultiFactorException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.MultiFactorAssertion;
import com.google.firebase.auth.MultiFactorResolver;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.PhoneMultiFactorGenerator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SignInTwoFactors extends AppCompatActivity {

    TextInputEditText emailText, passwordText;
    TextInputLayout emailText1, passwordText1;
    private static final int RC_SIGN_IN = 4;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    DatabaseReference ref;

    /**
     * Links the variables in Java to the components in xml and initializes the attributes.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_two_factors);
        emailText1 = findViewById(R.id.emailText1);
        passwordText1 = findViewById(R.id.passwordText1);
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);

        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Users");
    }

    /**
     * Authenticates the user with his email and password.
     */
    public void Twofactor(View view) {
        if (emailText.getText().toString().isEmpty() || passwordText.getText().toString().isEmpty()) {
            if (emailText.getText().toString().isEmpty()) {
                emailText1.setError("Please enter your Email adress");
            }
            if (passwordText.getText().toString().isEmpty()) {
                passwordText1.setError("Please enter your password");
            }
        }
        else {
            final String email = emailText.getText().toString();
            final String password = passwordText.getText().toString();
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                /**
                 * If the user's details have been verified - and displays a screen for entering a phone number, and activates Activity for result to enter a verification code sent in a text message to the phone entered by the user.
                 */
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        currentUser = auth.getCurrentUser();
                        // Choose authentication providers
                        List<AuthUI.IdpConfig> providers = Collections.singletonList(
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

    /**
     * If the phone number is verified, the method calls for the method {@link #whichUser()} that checks the user type.
     * @param requestCode The code the user receives
     * @param resultCode The code the user enters
     * @param data Not used.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                whichUser();
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

    /**
     * The method checks in the database what the user type is and calls the method {@link #whichActivity(Boolean isAuthorized)} that directs the user to the appropriate activity.
     */
    private void whichUser() {
        Query query = ref.orderByChild("uID").equalTo(currentUser.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User tmpUser = ds.getValue(User.class);
                    Boolean isAuthorized = tmpUser.getAuthorized();
                    whichActivity(isAuthorized);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /**
     * Directs the user to the appropriate screen
     * @param isAuthorized The user type (scorer or authorized).
     */
    public void whichActivity(Boolean isAuthorized) {
        Intent t6;
        if (isAuthorized) {
            t6 = new Intent(SignInTwoFactors.this, MainActivity.class);
            startActivity(t6);
            finish();
        }
        else {
            t6 = new Intent(SignInTwoFactors.this, GameInfo.class);
            startActivity(t6);
            finish();
        }
    }
}