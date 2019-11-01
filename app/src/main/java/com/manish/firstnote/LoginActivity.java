package com.manish.firstnote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.button_login, R.id.text_signup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                break;
            case R.id.text_signup:
                Intent intentsignup=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intentsignup);
                break;
        }
    }
}
