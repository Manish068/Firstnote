package com.manish.firstnote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.edittext_email)
    EditText edittextEmail;
    @BindView(R.id.edittext_password)
    EditText edittextPassword;
    @BindView(R.id.button_login)
    Button buttonLogin;
    @BindView(R.id.text_signup)
    TextView textSignup;

    FirebaseAuth firebaseAuth;
    @BindView(R.id.text_forget_password)
    TextView textForgetPassword;
    @BindView(R.id.text_otp_login)
    TextView textOtpLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance(); //for authentication with firebase

    }

    @OnClick({R.id.button_login, R.id.text_signup, R.id.text_forget_password,R.id.text_otp_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                String emailId = edittextEmail.getText().toString();
                String password = edittextPassword.getText().toString();
                if (!emailId.equalsIgnoreCase("")) {
                    if (!password.equalsIgnoreCase("")) {
                        loginUser(emailId, password);
                    } else {
                        Toast.makeText(this, "please enter password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "please enter valid emailID", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.text_signup:
                Intent intentsignup = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intentsignup);
                break;

            case R.id.text_forget_password:
                String email = edittextEmail.getText().toString();
                if (!email.equalsIgnoreCase("")) {
                    // sending the link for reseting the password in user email id
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(LoginActivity.this, "Please check your email for resetting password", Toast.LENGTH_SHORT);
                            } else {
                                Toast.makeText(LoginActivity.this, "Error resending password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.text_otp_login:
                Intent intentotp=new Intent(LoginActivity.this,OtploginActivity.class);
                startActivity(intentotp);
                finish();
                break;
        }
    }

    public void loginUser(String email, String password) {

        //for sign in we need sign in with email password previously we used create with emailid and password

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Loged in Successful", Toast.LENGTH_SHORT).show();
                    Intent intentmain = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intentmain);
                    finish();
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
