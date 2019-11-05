package com.manish.firstnote;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OtploginActivity extends AppCompatActivity {
    @BindView(R.id.editText_mobile)
    EditText editText_mobile;
    @BindView(R.id.button_verify)
    Button button;

    ProgressDialog progressDialog;
    String mobileno;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationStateChangedCallbacks;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_otplogin);
        ButterKnife.bind(this);
        firebaseAuth= FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Verifying phone number...");
        verificationStateChangedCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
           Signinwithmobile(phoneAuthCredential);
            }


            @Override
            public void onVerificationFailed(FirebaseException e) {
                progressDialog.dismiss();
                Toast.makeText(OtploginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        } ;
    }

    @OnClick(R.id.button_verify)
    public void onViewClicked() {
        progressDialog.show();
        mobileno="+91"+editText_mobile.getText().toString();
        if(!mobileno.equalsIgnoreCase("")){
            verifyMobileNumber(mobileno);
        }else{
            Toast.makeText(this,"Please enter valid mobile number",Toast.LENGTH_SHORT).show();
        }
    }
    //verifying mobile number
    public void verifyMobileNumber(String mobile){
        //firebase function phoneAuthProvider
        //verificationStateChangedCallbacks: so that we get to know the task was completed and mobile number was verified correctly or not
        PhoneAuthProvider.getInstance().verifyPhoneNumber(mobile,60, TimeUnit.SECONDS,this,verificationStateChangedCallbacks);


    }
    //
    public void Signinwithmobile(PhoneAuthCredential phoneAuthCredential){
        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if(task.isSuccessful()) {
                  progressDialog.dismiss();
                  Toast.makeText(OtploginActivity.this, "Mobile number verified successfully.", Toast.LENGTH_SHORT).show();
                  FirebaseUser currentuser=task.getResult().getUser();
                  String uid=currentuser.getUid();
                  Intent intent=new Intent(OtploginActivity.this,SignupActivity.class);
                  intent.putExtra("MOBILE",mobileno);
                  intent.putExtra("UID",uid);
                  startActivity(intent);
                  finish();
              }else{
                  Toast.makeText(OtploginActivity.this, "Error using Otp Login", Toast.LENGTH_SHORT).show();
              }
            }
        });
    }
}
