package com.renoside.schoolresell.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.renoside.schoolresell.R;

public class PIMPopWindowUtils {

    public static PopupWindow popWindow;
    public static TextView pimInfoImg;
    public static TextView pimInfoContent;
    public static TextView updateUserInfo;
    public static TextView pimAccountName;
    public static TextView pimAccountPassword;
    public static TextView updateUserAccount;
    public static EditText editUserName;
    public static EditText editUserDescription;
    public static EditText editUserPhone;
    public static EditText editUserAddress;
    public static EditText editUserAccount;

    /**
     * 显示Popup
     *
     * @param activity
     * @param context
     * @param view
     * @param type
     */
    public static void showPopupWindow(Activity activity, Context context, View view, int type) {
        // 加载PopupWindow的布局
        View inflate = null;
        if (type == 40010) {
            inflate = View.inflate(context, R.layout.popwindow_pim_info, null);
        } else if (type == 40011) {
            inflate = View.inflate(context, R.layout.popwindow_user_info, null);
        } else if (type == 40020) {
            inflate = View.inflate(context, R.layout.popwindow_pim_account, null);
        } else if (type == 40021) {
            inflate = View.inflate(context, R.layout.popwindow_user_account, null);
            TextView pimUserAccount = inflate.findViewById(R.id.pim_user_account);
            pimUserAccount.setText("更换账号：");
        } else if (type == 40022) {
            inflate = View.inflate(context, R.layout.popwindow_user_account, null);
            TextView pimUserAccount = inflate.findViewById(R.id.pim_user_account);
            pimUserAccount.setText("更换密码：");
        }
        popWindow = new PopupWindow(inflate, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        popWindow.setBackgroundDrawable(new ColorDrawable(0));
        //设置PopupWindow的位置
        if (type == 40010 || type == 40020) {
            popWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        } else if (type == 40011 || type == 40021 || type == 40022) {
            popWindow.setWidth((activity.getWindowManager().getDefaultDisplay().getWidth()) * 2 / 3);
            popWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        }
        //设置背景半透明
        backgroundAlpha(activity, 0.6f);

        pimInfoImg = inflate.findViewById(R.id.pim_info_img);
        pimInfoContent = inflate.findViewById(R.id.pim_info_content);
        updateUserInfo = inflate.findViewById(R.id.update_user_info);
        pimAccountName = inflate.findViewById(R.id.pim_account_name);
        pimAccountPassword = inflate.findViewById(R.id.pim_account_password);
        editUserName = inflate.findViewById(R.id.edit_user_name);
        editUserDescription = inflate.findViewById(R.id.edit_user_description);
        editUserPhone = inflate.findViewById(R.id.edit_user_phone);
        editUserAddress = inflate.findViewById(R.id.edit_user_address);
        editUserAccount = inflate.findViewById(R.id.edit_user_account);
        updateUserAccount = inflate.findViewById(R.id.update_user_account);


        //点击空白位置，PopupWindow消失的事件监听，这时候让背景恢复正常
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(activity, 1.0f);
            }
        });
    }

    /**
     * 设置屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public static void backgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        activity.getWindow().setAttributes(lp);
    }
}
