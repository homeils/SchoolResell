package com.renoside.schoolresell.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.renoside.schoolresell.R;

public class PopupWindowUtil {

    public static TextView popOkOrder;
    public static TextView popChannelOrder;
    public static TextView popCompleted;

    /**
     * 显示Popup
     *
     * @param activity
     * @param context
     * @param view
     * @param status
     */
    public static void showPopupWindow(Activity activity, Context context, View view, int status) {
        // 加载PopupWindow的布局
        View inflate = View.inflate(context, R.layout.popwindow, null);
        PopupWindow popWindow = new PopupWindow(inflate, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        popWindow.setBackgroundDrawable(new ColorDrawable(0));
        popOkOrder = inflate.findViewById(R.id.pop_ok_order);
        View popView1 = inflate.findViewById(R.id.pop_view1);
        popChannelOrder = inflate.findViewById(R.id.pop_channel_order);
        popCompleted = inflate.findViewById(R.id.pop_completed);
        //设置PopupWindow的位置
        popWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        //设置背景半透明
        backgroundAlpha(activity, 0.6f);
        //根据订单状态显示不同布局
        if (status == 1001) {
            popOkOrder.setText("下架商品");
            popChannelOrder.setText("修改商品信息");
            popOkOrder.setVisibility(View.VISIBLE);
            popView1.setVisibility(View.VISIBLE);
            popChannelOrder.setVisibility(View.VISIBLE);
            popCompleted.setVisibility(View.GONE);
        } else if (status == 10021) {
            popOkOrder.setText("确认收货");
            popChannelOrder.setText("取消订单");
            popOkOrder.setVisibility(View.VISIBLE);
            popView1.setVisibility(View.VISIBLE);
            popChannelOrder.setVisibility(View.VISIBLE);
            popCompleted.setVisibility(View.GONE);
        } else if (status == 10022) {
            popOkOrder.setText("下架商品");
            popChannelOrder.setText("取消订单");
            popOkOrder.setVisibility(View.VISIBLE);
            popView1.setVisibility(View.VISIBLE);
            popChannelOrder.setVisibility(View.VISIBLE);
            popCompleted.setVisibility(View.GONE);
        } else if (status == 1003) {
            popCompleted.setText("订单已完成");
            popOkOrder.setVisibility(View.GONE);
            popView1.setVisibility(View.GONE);
            popChannelOrder.setVisibility(View.GONE);
            popCompleted.setVisibility(View.VISIBLE);
        }
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
