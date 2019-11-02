package com.manish.firstnote;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.manish.firstnote.Data.Userinfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends AppCompatActivity {
    @BindView(R.id.edittext_signup_name)
    EditText edittextSignupName;
    @BindView(R.id.edittext_signup_email)
    EditText edittextSignupEmail;
    @BindView(R.id.edittext_Signup_password)
    EditText edittextSignupPassword;
    @BindView(R.id.button_signup)
    Button buttonSignup;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    DatabaseReference databaseUsers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseUsers = FirebaseDatabase.getInstance().getReference("USERS");
        progressDialog = new ProgressDialog(this);
    }

    @OnClick(R.id.button_signup)
    public void onViewClicked() {
        String name = edittextSignupName.getText().toString();
        String emailid = edittextSignupEmail.getText().toString();
        String password = edittextSignupPassword.getText().toString();

        if (!name.equalsIgnoreCase("")) {
            if (!emailid.equalsIgnoreCase("")) {
                if (!password.equalsIgnoreCase("")) {

                    registerUser(name, emailid, password);

                } else {
                    Toast.makeText(this, "please enter password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "please enter valid email", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "please enter name", Toast.LENGTH_SHORT).show();

        }

    }

    public void registerUser(String name, String email, String password) {
        progressDialog.setMessage("please wait...");
        progressDialog.show();
        //here we are creating a user for login
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    //save user information to data base
                    FirebaseUser currentuser = firebaseAuth.getCurrentUser(); //getting the user current login user information
                    String uid = currentuser.getUid(); //getting the Uid of the user which is present in firebase authentication section
                    Userinfo userinfo=new Userinfo(name,email,""); //created new class object to store the information


                    databaseUsers.child(uid).setValue(userinfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful()){
                                Toast.makeText(SignupActivity.this, "User is registered succesfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignupActivity.this, "Error registering user", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
