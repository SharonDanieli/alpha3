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

import java.util.Arrays;
import java.util.List;

public class VerifyMail extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    final String TAG = "LOGIN";

    private static final int RC_SIGN_IN = 4;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_mail);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        sendMail();
    }

    private void sendMail() {
        final String email = user.getEmail();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        // Re-enable button

                        if (!task.isSuccessful()) {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(VerifyMail.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void sendAgain(View view) {
        sendMail();
    }

    public void next(View view) {
        user.reload();
        if (user.isEmailVerified())
        {
            /*Intent t = new Intent(this, Verification.class);
            t.putExtras(getIntent());
            startActivity(t);*/
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
        else
        {
            Toast.makeText(this, "Email not verified", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                Intent t = new Intent(this, GameInfo.class);
                t.putExtras(getIntent());
                startActivity(t);
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
}
