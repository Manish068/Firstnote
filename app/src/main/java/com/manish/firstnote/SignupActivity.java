package com.manish.firstnote;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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
    //here now saving the UID because for each user there is some particular data he/she gonna store
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        sharedPreferences=getSharedPreferences("FIRENOTESDATA", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        firebaseAuth = FirebaseAuth.getInstance();
        databaseUsers = FirebaseDatabase.getInstance().getReference("USERS");
        progressDialog = new ProgressDialog(this);

        // if login is happening with mobile then password field should be hidden
        if(getIntent().hasExtra("MOBILE")){
            edittextSignupPassword.setVisibility(View.GONE);
        }else{
            edittextSignupPassword.setVisibility(View.VISIBLE);
        }

    }

    @OnClick(R.id.button_signup)
    public void onViewClicked() {
        String name = edittextSignupName.getText().toString();
        String emailid = edittextSignupEmail.getText().toString();
        String password = "";
        if(getIntent().hasExtra("MOBILE")){
            if(!name.equalsIgnoreCase("")){
                if(!emailid.equalsIgnoreCase("")){
                    progressDialog.setMessage("please wait...");
                    progressDialog.show();
                    String mobile=getIntent().getExtras().getString("MOBILE");
                    String uid=getIntent().getExtras().getString("UID");
                    Userinfo userinfo=new Userinfo(name,emailid,mobile);
                    databaseUsers.child(uid).setValue(userinfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful()){
                                editor.putString("UID",uid);
                                Toast.makeText(SignupActivity.this, "User is registered succesfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(SignupActivity.this, "Error registering user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(this,"please enter email Id",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this,"Please enter name",Toast.LENGTH_SHORT).show();
            }
        }else{
            password=edittextSignupPassword.getText().toString();
            registerUser(name,emailid,password);
        }

    }

    public void registerUser(String name, String email, String password) {
        if (!name.equalsIgnoreCase("")) {
            if (!email.equalsIgnoreCase("")) {
                if (!password.equalsIgnoreCase("")) {
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
                                            editor.putString("UID",uid);
                                            editor.commit();
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
}
