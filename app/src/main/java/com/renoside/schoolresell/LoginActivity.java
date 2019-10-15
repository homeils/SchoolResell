package com.renoside.schoolresell;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.renoside.inputbox.InputBox;
import com.renoside.schoolresell.Utils.ApiUrl;
import com.renoside.schoolresell.Utils.SharedPreferencesUtils;

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
        initListener();
    }

    private void initListener() {
        loginProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(LoginActivity.this, null, Toast.LENGTH_SHORT);
                toast.setText("你点击了用户协议");
                toast.show();
            }
        });
        loginGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginUsername.getText().equals("") || loginPassword.getText().equals("")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(LoginActivity.this, null, Toast.LENGTH_SHORT);
                            toast.setText("用户名和密码都不能为空哦");
                            toast.show();
                        }
                    });
                } else if (!loginUsername.getText().matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")) {
                    Toast toast = Toast.makeText(LoginActivity.this, null, Toast.LENGTH_SHORT);
                    toast.setText("用户名必须是一个正确的邮箱格式哦");
                    toast.show();
                } else {
                    OkGo.<String>post(ApiUrl.url + "/login")
                            .params("loginName", loginUsername.getText())
                            .params("loginPassword", loginPassword.getText())
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    if (response.code() == 200) {
                                        JSONObject jsonObject = JSONObject.parseObject(response.body());
                                        String userId = jsonObject.getString("userId");
                                        String token = jsonObject.getString("token");
                                        SharedPreferencesUtils.saveUserLoginInfo(LoginActivity.this, userId, token);
                                        SharedPreferencesUtils.saveIsFirstLogin(LoginActivity.this, false);
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    } else if (response.code() == 401) {
                                        Toast toast = Toast.makeText(LoginActivity.this, null, Toast.LENGTH_SHORT);
                                        toast.setText("用户名或密码错误");
                                        toast.show();
                                    }
                                }
                            });
                }
            }
        });
    }
}
