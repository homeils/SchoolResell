package com.renoside.schoolresell;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.renoside.inputbox.InputBox;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_protocol)
    TextView loginProtocol;
    @BindView(R.id.login_username)
    InputBox loginUsername;
    @BindView(R.id.login_password)
    InputBox loginPassword;
    @BindView(R.id.login_go)
    TextView loginGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "你点击了用户协议", Toast.LENGTH_SHORT).show();
            }
        });
        loginGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, loginUsername.getText() + "  " + loginPassword.getText(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

}
