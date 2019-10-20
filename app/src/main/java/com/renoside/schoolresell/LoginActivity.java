package com.renoside.schoolresell;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.renoside.inputbox.InputBox;
import com.renoside.schoolresell.Utils.ApiUrl;
import com.renoside.schoolresell.Utils.EasemobUtils;
import com.renoside.schoolresell.Utils.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;

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
        EasemobUtils.initEasemob(LoginActivity.this);
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
                                        String easemobToken = jsonObject.getString("easemob_token");
                                        /**
                                         * 请求注册或登录环信IM用户
                                         */
                                        if (easemobToken != null && !easemobToken.equals("")) {
                                            JSONObject easemobJson = new JSONObject();
                                            String temp = loginUsername.getText().replace("@", "");
                                            String username = temp.replace(".", "");
                                            easemobJson.put("username", username);
                                            easemobJson.put("password", loginPassword.getText());
                                            if (jsonObject.getString("message").equals("新用户注册")) {
                                                /**
                                                 * 环信新用户注册
                                                 */
                                                OkGo.<String>post(ApiUrl.easemob + "/users")
                                                        .headers("Content-Type", "application/json")
                                                        .headers("Authorization", "Bearer " + easemobToken)
                                                        .upJson(String.valueOf(easemobJson))
                                                        .execute(new StringCallback() {
                                                            @Override
                                                            public void onSuccess(Response<String> response) {
                                                                if (response.code() == 200) {
                                                                    EMClient.getInstance().login(username, loginPassword.getText(), new EMCallBack() {//回调
                                                                        @Override
                                                                        public void onSuccess() {
                                                                            Log.d("easemob: ", "登录聊天服务器成功！");
                                                                            EMClient.getInstance().groupManager().loadAllGroups();
                                                                            EMClient.getInstance().chatManager().loadAllConversations();
                                                                            SharedPreferencesUtils.saveUserLoginInfo(LoginActivity.this, userId, token);
                                                                            SharedPreferencesUtils.saveEasemobToken(LoginActivity.this, easemobToken);
                                                                            SharedPreferencesUtils.saveEasemobUser(LoginActivity.this, username, loginPassword.getText());
                                                                            SharedPreferencesUtils.saveIsFirstLogin(LoginActivity.this, false);
                                                                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                                            startActivity(intent);
                                                                        }

                                                                        @Override
                                                                        public void onProgress(int progress, String status) {

                                                                        }

                                                                        @Override
                                                                        public void onError(int code, String message) {
                                                                            Log.d("easemob: ", "登录聊天服务器失败！");
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        });
                                            } else if (jsonObject.getString("message").equals("老用户登录")) {
                                                /**
                                                 * 环信老用户登陆
                                                 */
                                                EMClient.getInstance().login(username, loginPassword.getText(), new EMCallBack() {//回调
                                                    @Override
                                                    public void onSuccess() {
                                                        Log.d("easemob: ", "登录聊天服务器成功！");
                                                        EMClient.getInstance().groupManager().loadAllGroups();
                                                        EMClient.getInstance().chatManager().loadAllConversations();
                                                        SharedPreferencesUtils.saveUserLoginInfo(LoginActivity.this, userId, token);
                                                        SharedPreferencesUtils.saveEasemobToken(LoginActivity.this, easemobToken);
                                                        SharedPreferencesUtils.saveEasemobUser(LoginActivity.this, username, loginPassword.getText());
                                                        SharedPreferencesUtils.saveIsFirstLogin(LoginActivity.this, false);
                                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                    }

                                                    @Override
                                                    public void onProgress(int progress, String status) {

                                                    }

                                                    @Override
                                                    public void onError(int code, String message) {
                                                        Log.d("easemob: ", "登录聊天服务器失败！");
                                                    }
                                                });
                                            }
                                        }
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
