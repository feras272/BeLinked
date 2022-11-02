package com.example.belinked;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class StartActivity extends AppCompatActivity {

    // Variables ------------------------------

    // Views
    private Button buttonSignIn;
    private Button buttonSignUp;
    private ImageView imageViewSignInWithGoogle;
    private ImageView imageViewSignInWithTwitter;

    // Firebase
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "GOOGLEAUTH";
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    // Google Sign In
    BeginSignInRequest signInRequest;
    GoogleSignInOptions gso;
    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;

    // Dialog
    Dialog dialog;

    ScriptGroup.Binding mBinding;

    // onCreate Method ------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        connectViews();

        mAuth = FirebaseAuth.getInstance();
        // Initialize Firebase Auth
//        if(mAuth.getCurrentUser() != null) {
//            finish();
//            return;
//        }
//        signInRequest = BeginSignInRequest.builder()
//                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                        .setSupported(true)
//                        // Your server's client ID, not your Android client ID.
//                        .setServerClientId(getString(R.string.default_web_client_id))
//                        // Only show accounts previously used to sign in.
//                        .setFilterByAuthorizedAccounts(true)
//                        .build())
//                .build();

        // deprecated
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//        dialog = new Dialog(MainActivity.this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.dialog_wait);
//        dialog.setCanceledOnTouchOutside(false);

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

    // onStart Method ------------------------------

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//    }

    // attach xml views with java variables
    private void connectViews() {
        buttonSignIn = findViewById(R.id.btn_sign_in_activity_start);
        buttonSignUp = findViewById(R.id.btn_sign_up_activity_start);
        imageViewSignInWithGoogle = findViewById(R.id.img_view_google_activity_start);
        imageViewSignInWithTwitter = findViewById(R.id.img_view_twitter_activity_start);
    }

    private void signInUser() {
        Intent intent = new Intent(StartActivity.this, SignInActivity.class);
        intent.putExtra("ACCOUNT_TYPE_SIGN_IN", "SignIn");
        startActivity(intent);
    }

    private void signUpUser() {
        Intent intent = new Intent(StartActivity.this, ChooseAccountActivity.class);
        intent.putExtra("ACCOUNT_TYPE_SIGN_UP", "SignUp");
        startActivity(intent);
    }

    private void signInUserWithGoogle() {
//         Configure sign-in to request the user's ID, email address, and basic
//         profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN); // deprecated

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//            case REQ_ONE_TAP:
//                try {
//                    //SignInClient oneTapClient = null;
//                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
//                    String idToken = credential.getGoogleIdToken();
//                    if (idToken !=  null) {
//                        // Got an ID token from Google. Use it to authenticate
//                        // with Firebase.
//                        Log.d(TAG, "Got ID token.");
//                    }
//                } catch (ApiException e) {
//                    // ...
//                }
//                break;
//        }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Request returned from launching the Intent from GoogleSingInApi.getSingInIntent(...);
        if(requestCode == RC_SIGN_IN) {
//            dialog.show();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "FiebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sing In failed, update UI appropriately
                Log.w(TAG, "Google Sign In failed", e);
                //e.printStackTrace();

//                dialog.dismiss();
            }
        }

    }

//    SignInCredential googleCredential = oneTapClient.getSignInCredentialFromIntent(data);
//    String idToken = googleCredential.getGoogleIdToken();
//    if (idToken !=  null) {
//        // Got an ID token from Google. Use it to authenticate
//        // with Firebase.
//        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
//        mAuth.signInWithCredential(firebaseCredential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            updateUI(null);
//                        }
//                    }
//                });
//    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // should go to the home activity
                            Intent i = new Intent(StartActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
//                            dialog.dismiss();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(mBinding.mainLayout);
                            Toast.makeText(StartActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                            //updateUI(null);

//                            dialog.dismiss();
                        }
                    }
                });
    }

    private void signInUserWithTwitter() {
//        Intent i = new Intent(StartActivity.this, ChooseAccountActivity.class);
//        startActivity(i);
        Toast.makeText(StartActivity.this, "Not yet implement it yet", Toast.LENGTH_SHORT).show();
    }


}