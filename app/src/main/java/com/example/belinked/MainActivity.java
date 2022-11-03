package com.example.belinked;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.belinked.data.Account;
import com.example.belinked.models.Organization;
import com.example.belinked.models.Student;
import com.example.belinked.models.User;
import com.example.belinked.organization.OrganizationMainActivity;
import com.example.belinked.student.StudentMainActivity;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    // Variables ----------------------------------

    // Constants
    private static final String TAG = "TEST_EXTRAS";
    private static final String students = "students";
    private static final String organizations = "organizations";

    // Views
    private TextView textViewUsername;
    private TextView textViewEmail;

    private Button buttonOrganization;
    private Button buttonStudent;
    private EditText editTextFirstName;
    private EditText editTextLastName;

    // Firebase
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private DatabaseReference accountRef;

    // Classes
    Intent intent;
    Account account;

    // Local Variables
    String accountType;
    String uid;

    // onCreate Method -----------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectViews();
        getExtrasIntent();
        Log.d(TAG, "AccountType after using getStringEXtra value in MainActivity is: " +accountType);

//        intent = getIntent();
//        if(intent != null) {
//            accountType = intent.getStringExtra("ACCOUNT_TYPE");
//            Account.accountType = accountType;
//        }

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        if(currentUser.getUid() != null) {
            uid = currentUser.getUid();
        }
        reference = FirebaseDatabase.getInstance().getReference("users");

        database = FirebaseDatabase.getInstance();

        buttonOrganization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields("organizations");
            }
        });

        buttonStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields("students");
            }
        });

        // read once
//        database.getReference().child("students").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    // , task.getException()
//                    Log.e(TAG, "students Error: " + accountType);
//                }
//                else {
//                    accountType = students;
//                    //  +String.valueOf(task.getResult().getValue())
//                    Log.d(TAG, "students success: "+accountType);
//                }
//            }
//        });
//
//        database.getReference().child("organizations").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    // , task.getException()
//                    Log.e(TAG, "organizations Error: " + accountType);
//                }
//                else {
//                    accountType = organizations;
//                    // +String.valueOf(task.getResult().getValue())
//                    Log.d(TAG, "organizations Success: "+accountType);
//                }
//            }
//        });

        //Log.d(TAG, "accountType at the end is: "+accountType);

//        database.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                if (snapshot.child("students").toString().equals(accountTypeDb.toString())) {
//                    // run some code
//                    accountType = students;
//                }
////                else if(snapshot.hasChild("organizations")) {
////                    accountType = organizations;
////                }
//                else {
//                    accountType = organizations;
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


//        accountRef = database.getReference().child("students").child(uid).child("accountType");
//        // (1)
//        Log.d(TAG, "Root Reference value in MainActivity is: " +accountRef);
//        // (2)
//        Log.d(TAG, "accountType from rootRef value in MainActivity is: " +accountType);
//
//        accountRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (DataSnapshot ds : task.getResult().getChildren()) {
//                        String key = ds.getKey();
//                        String value = ds.getValue(String.class);
//                        // (3)
//                        Log.d(TAG, "this is the key: "+key + ":" + value);
//                    }
//                } else {
//                    // (3)
//                    Log.d(TAG, task.getException().getMessage()); //Don't ignore potential errors!
//                }
//            }
//        });

        // -----------------------------------------------------------------------------------------


        //accountType = currentUser.getUid();

//        if(currentUser == null) {
//            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
//            startActivity(intent);
//            finish();
//            return;
//        }
//        Log.d(TAG, "Account using getAccount method: " +Account.getAccountType());
//        Log.d(TAG, "Account using Account variable: " +Account.accountType);

        //reference = database.getReference(Account.accountType).child(currentUser.getUid());




//        reference = FirebaseDatabase.getInstance().getReference(accountType).child(uid);
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//
//                        Student student = snapshot.getValue(Student.class);
////                        String email = snapshot.getValue(Student.class).email;
//                        if (student != null) {
////                            String firstName = snapshot.child("firstName").getValue().toString();
////                            String lastName = snapshot.child("lastName").getValue().toString();
////                            String email = snapshot.child("email").getValue().toString();
//                            //textViewUsername.setText("Username: " + student.firstName + " " + student.lastName);
//                            textViewUsername.setText("Username: " + student.email);
//                            //textViewEmail.setText("Email: " + student.email);
//                        }
//
//
//            }
//
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });



    }

    private void checkFields(String account) {
        if (editTextFirstName.getText().toString().isEmpty()) {
            Toast.makeText(MainActivity.this, "Enter your first name", Toast.LENGTH_SHORT).show();
            editTextFirstName.setError("First name is required");
        }
        else if(editTextLastName.getText().toString().isEmpty()) {
            Toast.makeText(MainActivity.this, "Enter your last name", Toast.LENGTH_SHORT).show();
            editTextLastName.setError("Last name is required");
        }
        else {
            if(account.equals("organizations")) {
                createUser("organizations");
                Intent intent = new Intent(MainActivity.this, OrganizationMainActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                createUser("students");
                Intent intent = new Intent(MainActivity.this, StudentMainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void createUser(String typeOfAccount) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();


        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String email = mAuth.getCurrentUser().getEmail();
        //Student user = new Student(firstName, lastName, email, 0);
        User newUser = new User(typeOfAccount,firstName, lastName, email, 0);

        //mRef.child("users").child(userId);
        //DatabaseReference db = FirebaseDatabase.getInstance().getReference("users");
        if(typeOfAccount != null) {
            // .child(accountType)
            // "users"
            // mRef.child("users")
            reference.child(mAuth.getCurrentUser().getUid()).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(MainActivity.this, "You have sign up successfully", Toast.LENGTH_SHORT).show();

                }
            });
        }
        else {
            Toast.makeText(
                    MainActivity.this,
                    "Sign up failed, please return to sign up page",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void connectViews() {
//        textViewUsername = findViewById(R.id.username_activity_main);
//        textViewEmail = findViewById(R.id.email_activity_main);
        buttonOrganization = findViewById(R.id.btn_organization_activity_main);
        buttonStudent = findViewById(R.id.btn_student_activity_main);
        editTextFirstName = findViewById(R.id.et_first_name_activity_main);
        editTextLastName = findViewById(R.id.et_last_name_activity_main);
    }

    private void getExtrasIntent() {
        intent = getIntent();
        if(intent.getStringExtra("ACCOUNT_TYPE") != null) {
            accountType = intent.getStringExtra("ACCOUNT_TYPE");
        }
//        else if(intent.getStringExtra("ACCOUNT_TYPE_SIGN_UP") != null) {
//            accountType = intent.getStringExtra("ACCOUNT_TYPE_SIGN_UP");
//        }
        else {
            accountType = "users";
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentId = user.getUid();

    }
}