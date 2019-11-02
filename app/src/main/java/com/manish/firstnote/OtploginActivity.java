package com.manish.firstnote;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
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

    PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationStateChangedCallbacks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_otplogin);
        ButterKnife.bind(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Verifying phone number...");
        verificationStateChangedCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                progressDialog.dismiss();
                Toast.makeText(OtploginActivity.this,"Mobile number verified successfully.",Toast.LENGTH_SHORT).show();
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
        String mobileno="+91"+editText_mobile.getText().toString();
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
}
