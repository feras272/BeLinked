package com.example.belinked;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

        // Using handler with postDelayed called runnable run method

            @Override
            public void run() {

                Intent i = new Intent(SplashActivity.this, StartActivity.class);

                startActivity(i);

                // close this activity

                finish();

            }

        }, 5*1000); // wait for 5 seconds
    }
/*
private void moveToLoginScreen() {
        Handler(Looper.myLooper()!!).postDelayed({
                finish()
                val intent:Intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        }, 3000)

    }
 */

}