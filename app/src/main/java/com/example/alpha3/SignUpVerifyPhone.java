package com.example.alpha3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class SignUpVerifyPhone extends AppCompatActivity {

    private static final int RC_SIGN_IN = 4;
    DatabaseReference ref;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_verify_phone);
        ref = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                createUser();
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
    public void createUser () {
        Intent t4;
        Intent intent = getIntent();
        String uID = user.getUid();
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String phoneNumber = user.getPhoneNumber();
        boolean ifAuthorized = intent.getExtras().getBoolean("ifAuthorized");
        User userDatabase = new User(uID, name, phoneNumber, email, ifAuthorized);
        ref.child(user.getUid()).setValue(userDatabase);
        if (ifAuthorized) { t4 = new Intent(this, MainActivity.class);
            startActivity(t4);}
        else { t4 = new Intent(this, GameInfo.class);
            startActivity(t4);}
    }
}
