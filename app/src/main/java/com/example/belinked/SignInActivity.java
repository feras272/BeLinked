package com.example.belinked;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.belinked.models.Organization;
import com.example.belinked.organization.OrganizationMainActivity;
import com.example.belinked.student.StudentMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class SignInActivity extends AppCompatActivity {

    // Variables ------------------------------------

    // Constants
    private static final String TAG = "FirebaseAuth";
    private static final String TAG_ACCOUNT = "ACCOUNT_TYPE";

    // Views
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSingIn;
    private TextView textViewForgotPassword;
    private TextView textViewDoNotHaveAnAccount;

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private FirebaseUser currentUser;
    DatabaseReference dbUser;

    // Classes
    Intent intent;

    // Local Variables
    String email;
    String password;
    String accountType;

    // onCreate -----------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        connectViews();
        getExtrasIntent();
        Log.d(TAG_ACCOUNT, "account type is in SinInActivity: " + accountType);

        // Initialize Firebase Auth

        mRef = FirebaseDatabase.getInstance().getReference();
        //mAuth = FirebaseAuth.getInstance();
        //String customerId = currentUser.getUid();
//        if(mAuth.getCurrentUser() != null) {
//            finish();
//            return;
//        }



        buttonSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
            }
        });

        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something
            }
        });

        textViewDoNotHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to sign up page
                goToSignUpActivity();
            }
        });
    }

    private void connectViews() {
        editTextEmail = findViewById(R.id.et_email_activity_sign_in);
        editTextPassword = findViewById(R.id.et_password_activity_sign_in);
        buttonSingIn = findViewById(R.id.btn_sing_in_activity_sign_in);
        textViewForgotPassword = findViewById(R.id.tv_forgot_password_activity_sing_in);
        textViewDoNotHaveAnAccount = findViewById(R.id.tv_do_not_have_an_account_activity_sign_in);
    }

    private void signInUser() {
        if(editTextEmail.getText().toString().isEmpty()) {
            Toast.makeText(SignInActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            editTextEmail.setError("Email is required");
        }
        else if(editTextPassword.getText().toString().isEmpty()) {
            Toast.makeText(SignInActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
            editTextEmail.setError("Password is required");
        }
        else {

            // .child(currentUser.getUid())
//            mRef.child("users").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    String input1 = editTextEmail.getText().toString();
//                    String input2 = editTextPassword.getText().toString();
//                    //String userType = snapshot.child("accountType").getValue(String.class);
//
//                    if(snapshot.child("accountType").getValue(String.class).equals("organizations")) {
//                        startActivity(new Intent(SignInActivity.this,
//                        OrganizationMainActivity.class));
//                        finish();
//                    }
//                    else if(snapshot.child("accountType").getValue(String.class).equals("students")) {
//                        startActivity(new Intent(SignInActivity.this,
//                                StudentMainActivity.class));
//                        finish();
//                    }
//                    else {
//                        Toast.makeText(SignInActivity.this, "You don't have account type", Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });

            // -------------------------------------------------------------------------------------

//            mRef.child("users").child("students").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    String input1 = editTextEmail.getText().toString();
//                    String input2 = editTextPassword.getText().toString();
//                    String userType = "";
//                    try {
//                        userType = snapshot.child("accountType").getValue().toString();
//                    } catch (NullPointerException e) {
//                        Toast.makeText(SignInActivity.this, "You are not an organization", Toast.LENGTH_SHORT).show();
//                    }
//
//                    try {
//                        userType = snapshot.child("accountType").getValue().toString();
//                    } catch (NullPointerException e) {
//                        Toast.makeText(SignInActivity.this, "You are not an organization", Toast.LENGTH_SHORT).show();
//                    }
//
//
//
//                    if(userType.equals("organizations")) {
//                        startActivity(new Intent(SignInActivity.this,
//                                OrganizationMainActivity.class));
//                        finish();
//                    }
//                    else if(userType.equals("students")) {
//                        startActivity(new Intent(SignInActivity.this,
//                                StudentMainActivity.class));
//                        finish();
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });

            //                    if(snapshot.child("organizations").child(currentUser.getUid()).toString()
//                            == mRef.child("users").child("organization").child(currentUser.getUid()).toString()) {
//                        startActivity(new Intent(SignInActivity.this,
//                                OrganizationMainActivity.class));
//                        finish();
//                    }
//                    else if(snapshot.child("students").child(currentUser.getUid()).toString()
//                            == mRef.child("users").child("students").child(currentUser.getUid()).toString()) {
//                        startActivity(new Intent(SignInActivity.this,
//                                StudentMainActivity.class));
//                        finish();
//                    }

            // -----------------------------------------------------------------------------

            //  Check user type and redirect accordingly
//                    if (snapshot.child("organizations").exists()) {
            //Log.d("ABC", snapshot.child("organization").getValue().toString());
//                        Boolean org = snapshot.child(currentUser.getUid()).child("accountType")
//                                .getValue(String.class).toString().equals("organizations");
//                        if (org) {
//
//                        }
//                        else {
//                            startActivity(new Intent(SignInActivity.this,
//                                    StudentMainActivity.class));
//                            finish();
//                        }
//                    }
//                    else if(snapshot.child("students").exists()) {
//                        Boolean std = snapshot.child(currentUser.getUid()).child("accountType")
//                                        .getValue(String.class).toString().equals("students");
//                        if(std) {
//
//                        }
//
//                    }
//                    else {
//                        Toast.makeText(
//                                SignInActivity.this,
//                                "You have not an student or organization account",
//                                Toast.LENGTH_SHORT
//                        ).show();
//                    }

//                if(snapshot.child(input1).exists()) {
//
//                    if(snapshot.child(input2).exists()) {
//
//                        if(snapshot.child("organization").exists()) {
//
//                        }
//                    }
//                    else {
//                        Toast.makeText(SignInActivity.this, "Data not found !!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                else {
//                    Toast.makeText(SignInActivity.this, "Data not found !!", Toast.LENGTH_SHORT).show();
//                }


            //currentUser = mAuth.getCurrentUser();
            email = editTextEmail.getText().toString();
            password = editTextPassword.getText().toString();
            mAuth = FirebaseAuth.getInstance();
            //currentUser = mAuth.getCurrentUser();
            dbUser = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid());

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                //FirebaseUser user = mAuth.getCurrentUser();
                                //updateUI(user);

                                // Message to the user when he has already an account
                                Toast.makeText(
                                        SignInActivity.this,
                                        "You have already an account",
                                        Toast.LENGTH_SHORT
                                ).show();
                                //"users"

                                ValueEventListener valueEventListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.child("accountType").getValue(String.class).equals("organizations")) {
                                            startActivity(new Intent(SignInActivity.this,
                                                    OrganizationMainActivity.class));
                                            finish();
                                        }
                                        else if(snapshot.child("accountType").getValue(String.class).equals("students")) {
                                            startActivity(new Intent(SignInActivity.this,
                                                    StudentMainActivity.class));
                                            finish();
                                        }
                                        else {
                                            Toast.makeText(SignInActivity.this, "You don't have account type", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                };

                                dbUser.addListenerForSingleValueEvent(valueEventListener);

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(SignInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    });

        }
    }

    private void goToSignUpActivity() {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        intent.putExtra("ACCOUNT_TYPE", accountType);
        intent.putExtra("EMAIL", email);
        intent.putExtra("PASSWORD", password);
        startActivity(intent);
        finish();
    }

    private void getExtrasIntent() {
        intent = getIntent();
        if(intent != null) {
            accountType = intent.getStringExtra("ACCOUNT_TYPE");
        }
        else {
            accountType = "users";
        }
//        return accountType;
    }
}