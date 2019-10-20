package com.renoside.schoolresell;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.renoside.schoolresell.Utils.EasemobUtils;
import com.renoside.schoolresell.Utils.SharedPreferencesUtils;

public class StartActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBarWithBackground);
        super.onCreate(savedInstanceState);
        EasemobUtils.initEasemob(StartActivity.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(SharedPreferencesUtils.getIsFirstLogin(StartActivity.this).get("is_first_login")){
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }else{
                    EMClient.getInstance().login(SharedPreferencesUtils.getEasemobUser(StartActivity.this).get("easemob_username"),
                            SharedPreferencesUtils.getEasemobUser(StartActivity.this).get("easemob_password"), new EMCallBack() {//回调
                        @Override
                        public void onSuccess() {
                            EMClient.getInstance().groupManager().loadAllGroups();
                            EMClient.getInstance().chatManager().loadAllConversations();
                            Intent intent = new Intent(StartActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
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
        }, 1500);
    }
}
