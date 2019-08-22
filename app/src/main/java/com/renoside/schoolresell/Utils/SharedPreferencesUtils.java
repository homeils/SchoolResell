package com.renoside.schoolresell.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class SharedPreferencesUtils {

    public static SharedPreferences sharedPreferences; //当返回的时候保存在文件
    private static final String SHARED_FIRST_LOGIN = "first_login_info"; //登录信息文件名称
    private static final String SHARED_USER_LOGIN = "user_login_info"; //用户信息文件名称

    public static void saveIsFirstLogin(Context context, boolean isFirstLogin) {
        sharedPreferences = context.getSharedPreferences(SHARED_FIRST_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_first_login", isFirstLogin);
        editor.commit();
    }

    public static Map<String, Boolean> getIsFirstLogin(Context context) {
        Map<String, Boolean> isFirstLogin = new HashMap<>();
        sharedPreferences = context.getSharedPreferences(SHARED_FIRST_LOGIN, Context.MODE_PRIVATE);
        isFirstLogin.put("is_first_login", sharedPreferences.getBoolean("is_first_login", true));
        return isFirstLogin;
    }

    /**
     * 储存用户ID和令牌
     * @param context
     * @param userId
     * @param token
     */
    public static void saveUserLoginInfo(Context context, String userId, String token) {
        sharedPreferences = context.getSharedPreferences(SHARED_USER_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", userId);
        editor.putString("token", token);
        editor.commit();
    }

    /**
     * 读取用户ID和令牌
     * @param context
     * @return
     */
    public static Map<String, String> getUserLoginInfo(Context context) {
        Map<String, String> userLoginInfo = new HashMap<>();
        sharedPreferences = context.getSharedPreferences(SHARED_USER_LOGIN, Context.MODE_PRIVATE);
        userLoginInfo.put("userId", sharedPreferences.getString("userId", "error"));
        userLoginInfo.put("token", sharedPreferences.getString("token", "error"));
        return userLoginInfo;
    }
}
