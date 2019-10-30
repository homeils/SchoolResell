package com.renoside.schoolresell;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.renoside.inputbox.InputBox;
import com.renoside.schoolresell.Utils.ApiUrl;
import com.renoside.schoolresell.Utils.EasemobUtils;
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
        EasemobUtils.initEasemob(LoginActivity.this);
        initListener();
    }

    /**
     * 返回直接退出程序
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return false;
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
                                        /**
                                         * 解析数据
                                         */
                                        JSONObject jsonObject = JSONObject.parseObject(response.body());
                                        String userId = jsonObject.getString("userId");
                                        String token = jsonObject.getString("token");
                                        String easemobToken = jsonObject.getString("easemob_token");
                                        /**
                                         * 展示上传进度条
                                         */
                                        ProgressDialog pd2 = new ProgressDialog(LoginActivity.this);
                                        pd2.setTitle("注册/登录");
                                        pd2.setIcon(R.mipmap.app_icon);
                                        pd2.setMessage("正在进行注册/登录，请稍后...");
                                        pd2.setCancelable(false);
                                        pd2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                        pd2.show();
                                        /**
                                         * 请求注册或登录环信IM用户
                                         */
                                        if (easemobToken != null && !easemobToken.equals("")) {
                                            JSONObject easemobJson = new JSONObject();
                                            String username = loginUsername.getText().replace("@", "_");
                                            easemobJson.put("username", username);
                                            easemobJson.put("password", loginPassword.getText());
                                            if (jsonObject.getString("message").equals("新用户注册")) {
                                                AlertDialog.Builder security = new AlertDialog.Builder(LoginActivity.this)
                                                        .setIcon(R.mipmap.app_icon)
                                                        .setTitle("注册")
                                                        .setMessage("系统检测到您第一次使用这个账号，将为您自动注册，请牢记你的账号和密码，点击继续")
                                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
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
                                                                                            pd2.dismiss();
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
                                                                                            pd2.dismiss();
                                                                                            Toast.makeText(LoginActivity.this, "注册/登录出错", Toast.LENGTH_SHORT).show();
                                                                                            Log.d("easemob: ", "登录聊天服务器失败！");
                                                                                        }
                                                                                    });
                                                                                }
                                                                            }
                                                                        });
                                                            }
                                                        });
                                                security.create().show();
                                            } else if (jsonObject.getString("message").equals("老用户登录")) {
                                                /**
                                                 * 环信老用户登陆
                                                 */
                                                EMClient.getInstance().login(username, loginPassword.getText(), new EMCallBack() {//回调
                                                    @Override
                                                    public void onSuccess() {
                                                        pd2.dismiss();
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
                                                        pd2.dismiss();
                                                        Toast.makeText(LoginActivity.this, "注册/登录出错", Toast.LENGTH_SHORT).show();
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
