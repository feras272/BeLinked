package com.example.belinked;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseAccountActivity extends AppCompatActivity {

    // Variables -------------------------------------------

    // Constants
    private static final String organizations = "organizations";
    private static final String students = "students";

    // Views
    private Button buttonOrganization;
    private Button buttonStudent;

    // Classes
    private Intent intent;

    // Local Variables
    private String accountType;

    // onCreate Method --------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_account);

        connectViews();
        //getExtrasIntent();

        buttonOrganization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVariables(organizations);
            }
        });

        buttonStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVariables(students);
            }
        });

    }

    private void connectViews() {
        buttonOrganization = findViewById(R.id.btn_organization_activity_choose_account);
        buttonStudent = findViewById(R.id.btn_student_choose_account);
    }

    private void sendVariables(String account) {
//        getExtrasIntent();
//        if(accountType.toString().equals("SignIn")) {
//            Intent i = new Intent(ChooseAccountActivity.this, SignInActivity.class);
//            i.putExtra("ACCOUNT_TYPE", account);
//            startActivity(i);
//        }
        //else if(accountType.toString().equals("SignUp")) {
        Intent i = new Intent(ChooseAccountActivity.this, SignUpActivity.class);
        i.putExtra("ACCOUNT_TYPE", account);
        startActivity(i);
        //}

    }

    private void getExtrasIntent() {
        intent = getIntent();
        if(intent.getStringExtra("ACCOUNT_TYPE_SIGN_IN").toString().equals("SignIn")) {
            accountType = intent.getStringExtra("ACCOUNT_TYPE_SIGN_IN");
        }
        else if(intent.getStringExtra("ACCOUNT_TYPE_SIGN_UP").toString().equals("SignUp")) {
            accountType = intent.getStringExtra("ACCOUNT_TYPE_SIGN_UP");
        }
        else {
            accountType = "users";
        }
//        return accountType;
    }

}