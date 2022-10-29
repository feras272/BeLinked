package com.example.belinked;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Variables ------------------------------

    // Views
    private Button buttonSignIn;
    private Button buttonSignUp;
    private ImageView imageViewSignInWithGoogle;
    private ImageView imageViewSignInWithTwitter;

    // onCreate Class ------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectViews();

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });

        imageViewSignInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUserWithGoogle();
            }
        });

        imageViewSignInWithTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUserWithTwitter();
            }
        });
    }

    // attach xml views with java variables
    private void connectViews() {
        buttonSignIn = findViewById(R.id.btn_sign_in_activity_main);
        buttonSignUp = findViewById(R.id.btn_sign_up_activity_main);
        imageViewSignInWithGoogle = findViewById(R.id.img_view_google_activity_main);
        imageViewSignInWithTwitter = findViewById(R.id.img_view_twitter_activity_main);
    }

    private void signInUser() {
        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(intent);
    }

    private void signUpUser() {
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    private void signInUserWithGoogle() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
    }

    private void signInUserWithTwitter() {

    }

}