package com.example.belinked;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.belinked.organization.OrganizationMainActivity;
import com.example.belinked.student.StudentMainActivity;
import com.example.belinked.data.Account;
import com.example.belinked.models.Organization;
import com.example.belinked.models.Student;
import com.example.belinked.models.User;
import com.example.belinked.organization.OrganizationMainActivity;
import com.example.belinked.student.StudentMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {

    // Variables ------------------------------------

    // Constants
    private static final String TAG = "FirebaseAuth";
    private static final String TAG_ACCOUNT = "ACCOUNT_TYPE";

    // Views
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private Button buttonSignUp;
    private TextView textViewTermsAndConditions;

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    DatabaseReference database;
    private String userId;

    // Local Variables
    String firstName;
    String lastName;
    String email;
    String password;
    String accountType;

    // Classes
    Intent mIntent;
    //Account account;

    // onCreate Method --------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        connectViews();

        getExtrasIntent();
        //Account.accountType = accountType;
        Log.d(TAG_ACCOUNT, "accountType value in sign up activity is:" + accountType);
        Log.d(TAG_ACCOUNT, "Account class value in sign up activity is:" + Account.accountType);

        //account = new Account();
        //getExtrasIntent();
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://belinked-76446-default-rtdb.firebaseio.com/");

//        // Initialize Firebase Auth
//        if(mAuth.getCurrentUser() != null) {
//            finish();
//            return;
//        }

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields();
            }
        });
    }

    // attach view to java variables
    private void connectViews() {
        editTextFirstName = findViewById(R.id.et_first_name_activity_sign_up);
        editTextLastName = findViewById(R.id.et_last_name_activity_sign_up);
        editTextEmail = findViewById(R.id.et_email_activity_sign_up);
        editTextPassword = findViewById(R.id.et_password_activity_sign_up);
        editTextConfirmPassword = findViewById(R.id.et_confirm_password_activity_sign_up);
        buttonSignUp = findViewById(R.id.btn_sign_up_activity_sign_up);
        textViewTermsAndConditions = findViewById(R.id.tv_terms_and_condition_activity_sign_up);
    }
    //(TextUtils.isEmpty(editTextFirstName.getText().toString().trim() <= " ")):
    private void checkFields() {
        if (editTextFirstName.getText().toString().isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Enter your first name", Toast.LENGTH_SHORT).show();
            editTextFirstName.setError("First name is required");
        }
        else if(editTextLastName.getText().toString().isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Enter your last name", Toast.LENGTH_SHORT).show();
            editTextLastName.setError("Last name is required");
        }
        else if(editTextEmail.getText().toString().isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Enter your email", Toast.LENGTH_SHORT).show();
            editTextEmail.setError("Email is required");
        }
        else if(editTextPassword.getText().toString().isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Enter your password", Toast.LENGTH_SHORT).show();
            editTextPassword.setError("Password is required");
        }
        else if(editTextConfirmPassword.getText().toString().isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Enter your conform password", Toast.LENGTH_SHORT).show();
            editTextConfirmPassword.setError("Confirm password is required");
        }
        else if(!editTextPassword.getText().toString().equals(editTextConfirmPassword.getText().toString())) {
            Toast.makeText(SignUpActivity.this, "Your confirm password is not equal", Toast.LENGTH_SHORT).show();
            editTextPassword.setError("Confirm password must be equal");
        }
        else {
            // Here will sign up with firebase

            firstName = editTextFirstName.getText().toString();
            lastName = editTextLastName.getText().toString();
            email = editTextEmail.getText().toString();
            password = editTextPassword.getText().toString();

            //mRef.child("users").child(userId);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FirebaseUser firebaseUser = task.getResult().getUser();
                                userId = firebaseUser.getUid();

                                database = FirebaseDatabase.getInstance().getReference("users");


                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                //updateUI(user);

                                if(accountType.equals("organizations")) {

                                    // Changed user type from Organization Class to User Class -----

//                                    Organization organization = new Organization(firstName, lastName, email);
//                                    createNewUser(organization);

                                    User userOrg = new User(firstName, lastName, email, "organizations");
                                    createNewUser(userOrg);

                                    // -------------------------------------------------------------

                                    //goToMainActivity();
//                                    Intent intent = new Intent(SignUpActivity.this, OrganizationMainActivity.class);
//                                    intent.putExtra("ACCOUNT_TYPE", accountType);
//                                    startActivity(intent);
                                    startActivity(new Intent(SignUpActivity.this,
                                            OrganizationMainActivity.class));
                                    finish();
                                }
                                else if(accountType.equals("students")) {

                                    // Changed user type from Organization Class to User Class -----

//                                    Student student = new Student(firstName, lastName, email);
//                                    createNewUser(student);

                                    User userStd = new User(firstName, lastName, email, "students");
                                    createNewUser(userStd);

                                    // -------------------------------------------------------------

                                    //goToMainActivity();
//                                    Intent intent = new Intent(SignUpActivity.this, StudentMainActivity.class);
//                                    intent.putExtra("ACCOUNT_TYPE", accountType);
//                                    startActivity(intent);
//                                    finish();
                                    startActivity(new Intent(SignUpActivity.this,
                                            StudentMainActivity.class));
                                    finish();
                                }
//                                else {
//                                    User newUser = new User(firstName, lastName, email);
//                                    createNewUser(newUser);
//                                    //goToMainActivity();
//                                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
//                                    intent.putExtra("ACCOUNT_TYPE", accountType);
//                                    startActivity(intent);
//                                    finish();
//                                }


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                                Toast.makeText(SignUpActivity.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

//            mAuth.createUserWithEmailAndPassword(email, password)
//                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                            Toast.makeText(
//                                    SignUpActivity.this,
//                                    "You have sign up successfully",
//                                    Toast.LENGTH_SHORT
//                            ).show();
//                            goToMainActivity();
//                            } else {
//                                Toast.makeText(SignUpActivity.this, "Sign up failed", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });


        }
    }

    private void goToMainActivity() {

    }

    private void getExtrasIntent() {
        mIntent = getIntent();
        if(mIntent != null) {
            accountType = mIntent.getStringExtra("ACCOUNT_TYPE");
        }
        else {
            accountType = "users";
        }
//        return accountType;
    }

    private void createNewUser(Object newUser) {
        if(accountType != null) {
            // .child(accountType)
            // "users"
            // mRef.child("users")
            database.child(userId).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(SignUpActivity.this, "You have sign up successfully", Toast.LENGTH_SHORT).show();

                        }
                    });
        }
        else {
            Toast.makeText(
                    SignUpActivity.this,
                    "Sign up failed, please return to sign up page",
                    Toast.LENGTH_SHORT
            ).show();
        }

    }

}