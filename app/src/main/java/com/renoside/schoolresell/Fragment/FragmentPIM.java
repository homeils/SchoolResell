package com.renoside.schoolresell.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hyphenate.chat.EMClient;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.renoside.schoolresell.Adapter.PIMRcvAdapter;
import com.renoside.schoolresell.Bean.UserInfo;
import com.renoside.schoolresell.Entity.PIMEntity;
import com.renoside.schoolresell.LoginActivity;
import com.renoside.schoolresell.PutActivity;
import com.renoside.schoolresell.R;
import com.renoside.schoolresell.Utils.ApiUrl;
import com.renoside.schoolresell.Utils.EasemobUtils;
import com.renoside.schoolresell.Utils.PIMPopWindowUtils;
import com.renoside.schoolresell.Utils.SharedPreferencesUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class FragmentPIM extends Fragment {

    @BindView(R.id.pim_help)
    TextView pimHelp;
    @BindView(R.id.pim_container)
    LinearLayout pimContainer;
    @BindView(R.id.pim_img)
    ImageView pimImg;
    @BindView(R.id.pim_name)
    TextView pimName;
    @BindView(R.id.pim_description)
    TextView pimDescription;
    @BindView(R.id.pim_recyclerview)
    RecyclerView pimRecyclerview;
    public Unbinder bind;

    PIMRcvAdapter rcvAdapter;
    private List<Uri> imgUris;
    /**
     * 定义数据集合
     */
    private List<PIMEntity> dataList;
    /**
     * 定义item图标和标题集合
     */
    private Integer[] pimItemIcos1 = {R.drawable.pim_item_1, R.drawable.pim_item_2, R.drawable.pim_item_3};
    private String[] pimItemTitles1 = {"账号管理", "好友", "在卖"};
    private Integer[] pimItemIcos2 = {R.drawable.pim_item_5, R.drawable.pim_item_6, R.drawable.pim_item_7, R.drawable.pim_item_8};
    private String[] pimItemTitles2 = {"安全中心", "设置", "隐私", "关于"};
    final RxPermissions rxPermissions;

    public FragmentPIM(RxPermissions rxPermissions) {
        this.rxPermissions = rxPermissions;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_pim, container, false);
        bind = ButterKnife.bind(this, view);
        EasemobUtils.initEasemob(getContext());
        /**
         * 设置数据集合
         */
        setDataList();
        /**
         * 创建并设置适配器
         */
        rcvAdapter = new PIMRcvAdapter(dataList);
        pimRecyclerview.setAdapter(rcvAdapter);
        /**
         * 设置线性垂直布局管理器
         */
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        pimRecyclerview.setLayoutManager(manager);
        /**
         * 初始化事件监听
         */
        initListener();
        return view;
    }

    /**
     * 接受头像并上传
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
            imgUris = Matisse.obtainResult(data);
            /**
             * 展示上传进度条
             */
            ProgressDialog pd2 = new ProgressDialog(getContext());
            pd2.setTitle("上传头像");
            pd2.setIcon(R.mipmap.app_icon);
            pd2.setMessage("正在同步信息...");
            pd2.setCancelable(false);
            pd2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd2.show();
            /**
             * 上传头像
             */
            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    Bundle bundle = msg.getData();
                    switch (msg.what) {
                        case 200:
                            OkGo.<String>put(ApiUrl.url + "/user/" + SharedPreferencesUtils.getUserLoginInfo(getActivity()).get("userId") + "/img")
                                    .headers("token", SharedPreferencesUtils.getUserLoginInfo(getActivity()).get("token"))
                                    .params("userImg", bundle.getString("userImg"))
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(Response<String> response) {
                                            Toast toast = Toast.makeText(getContext(), null, Toast.LENGTH_SHORT);
                                            toast.setText("头像上传成功");
                                            toast.show();
                                            pd2.dismiss();
                                            PIMPopWindowUtils.popWindow.dismiss();
                                            Glide.with(getContext()).load(imgUris.get(0)).into(pimImg);
                                        }
                                    });
                    }
                }
            };
            /**
             * 新开线程得到储存好的图片路径
             */
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < imgUris.size(); i++) {
                        /**
                         * uri转file
                         */
                        Uri uri = imgUris.get(i);
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getActivity().getContentResolver().query(uri, proj, null, null, null);
                        int actual_image_column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        String img_path = cursor.getString(actual_image_column_index);
                        File file = new File(img_path);
                        /**
                         * 请求储存图片至smms图床
                         */
                        OkGo.<String>post("https://sm.ms/api/v2/upload")
                                .isMultipart(true)
                                .headers("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0")
                                .params("smfile", file)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        JSONObject jsonObject = JSONObject.parseObject(response.body());
                                        String url = jsonObject.getJSONObject("data").getString("url");
                                        Message msg = new Message();
                                        msg.what = 200;
                                        Bundle bundle = new Bundle();
                                        bundle.putString("userImg", String.valueOf(url));
                                        msg.setData(bundle);
                                        handler.sendMessage(msg);
                                    }
                                });
                        cursor.close();
                    }
                }
            }).start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    private void setDataList() {
        /**
         * 设置用户基本信息
         */
        OkGo.<String>get(ApiUrl.url + "/user/" + SharedPreferencesUtils.getUserLoginInfo(getContext()).get("userId"))
                .headers("token", SharedPreferencesUtils.getUserLoginInfo(getContext()).get("token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        JSONObject jsonObject = JSON.parseObject(response.body());
                        UserInfo userInfo = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<UserInfo>() {
                        });
                        Glide.with(getContext()).load(userInfo.getUserImg()).into(pimImg);
                        pimName.setText(userInfo.getUserName());
                        pimDescription.setText(userInfo.getUserDescription());
                    }
                });
        dataList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            PIMEntity pimItem = new PIMEntity(PIMEntity.PIM_ITEM);
            pimItem.setPimImg(pimItemIcos1[i]);
            pimItem.setPimTitle(pimItemTitles1[i]);
            dataList.add(pimItem);
        }
        PIMEntity pimLast1 = new PIMEntity(PIMEntity.PIM_LAST);
        pimLast1.setPimImg(R.drawable.pim_item_4);
        pimLast1.setPimTitle("我的收藏");
        dataList.add(pimLast1);
        PIMEntity pimSpan1 = new PIMEntity(PIMEntity.PIM_SPAN);
        dataList.add(pimSpan1);
        for (int i = 0; i < 4; i++) {
            PIMEntity pimItem = new PIMEntity(PIMEntity.PIM_ITEM);
            pimItem.setPimImg(pimItemIcos2[i]);
            pimItem.setPimTitle(pimItemTitles2[i]);
            dataList.add(pimItem);
        }
        PIMEntity pimLast2 = new PIMEntity(PIMEntity.PIM_LAST);
        pimLast2.setPimImg(R.drawable.pim_item_9);
        pimLast2.setPimTitle("通讯录");
        dataList.add(pimLast2);
        PIMEntity pimSpan2 = new PIMEntity(PIMEntity.PIM_SPAN);
        dataList.add(pimSpan2);
        PIMEntity pimQuit = new PIMEntity(PIMEntity.PIM_QUIT);
        dataList.add(pimQuit);
    }

    private void initListener() {
        /**
         * 修改用户头像或者基本信息
         */
        pimContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PIMPopWindowUtils.showPopupWindow(getActivity(), getContext(), v, 40010);
                /**
                 * 修改头像
                 */
                PIMPopWindowUtils.pimInfoImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .subscribe(granted -> {
                                    if (granted) {
                                        Matisse.from(getActivity())
                                                .choose(MimeType.ofImage(), false) // 选择支持的文件类型
                                                .showSingleMediaType(true) //只展示支持的文件
                                                .theme(R.style.Matisse_Dracula) //黑色主题
                                                .countable(true) //是否有序
                                                .maxSelectable(1) // 图片选择的最多数量
                                                .spanCount(3) //一行三个
                                                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) //选择方向
                                                .thumbnailScale(0.85f) // 缩略图的质量
                                                .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                                                .forResult(200); // 设置作为标记的请求码
                                    } else {
                                        Toast toast = Toast.makeText(getContext(), null, Toast.LENGTH_SHORT);
                                        toast.setText("你拒绝了权限请求，请前往设置开启权限才可以使用此功能");
                                        toast.show();
                                    }
                                });
                    }
                });
                /**
                 * 修改基本信息
                 */
                PIMPopWindowUtils.pimInfoContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PIMPopWindowUtils.popWindow.dismiss();
                        PIMPopWindowUtils.showPopupWindow(getActivity(), getContext(), v, 40011);
                        PIMPopWindowUtils.updateUserInfo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                OkGo.<String>put(ApiUrl.url + "/user/" + SharedPreferencesUtils.getUserLoginInfo(getContext()).get("userId"))
                                        .headers("token", SharedPreferencesUtils.getUserLoginInfo(getContext()).get("token"))
                                        .params("userName", PIMPopWindowUtils.editUserName.getText().toString())
                                        .params("userDescription", PIMPopWindowUtils.editUserDescription.getText().toString())
                                        .params("userPhone", PIMPopWindowUtils.editUserPhone.getText().toString())
                                        .params("userAddress", PIMPopWindowUtils.editUserAddress.getText().toString())
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onSuccess(Response<String> response) {
                                                Toast toast = Toast.makeText(getContext(), null, Toast.LENGTH_SHORT);
                                                toast.setText("用户基本信息修改成功");
                                                toast.show();
                                                PIMPopWindowUtils.popWindow.dismiss();
                                                setDataList();
                                            }
                                        });
                            }
                        });
                    }
                });
            }
        });
        rcvAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position) {
                    /**
                     * 修改用户账号或者密码
                     */
                    case 0:
                        PIMPopWindowUtils.showPopupWindow(getActivity(), getContext(), view, 40020);
                        /**
                         * 更改账号
                         */
                        PIMPopWindowUtils.pimAccountName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PIMPopWindowUtils.popWindow.dismiss();
                                PIMPopWindowUtils.showPopupWindow(getActivity(), getContext(), v, 40021);
                                PIMPopWindowUtils.updateUserAccount.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        OkGo.<String>put(ApiUrl.url + "/user/" + SharedPreferencesUtils.getUserLoginInfo(getActivity()).get("userId") + "/loginName")
                                                .headers("token", SharedPreferencesUtils.getUserLoginInfo(getActivity()).get("token"))
                                                .params("loginName", PIMPopWindowUtils.editUserAccount.getText().toString())
                                                .execute(new StringCallback() {
                                                    @Override
                                                    public void onSuccess(Response<String> response) {
                                                        Toast toast = Toast.makeText(getContext(), null, Toast.LENGTH_SHORT);
                                                        toast.setText("用户账号修改成功，请重新登陆");
                                                        toast.show();
                                                        PIMPopWindowUtils.popWindow.dismiss();
                                                        Intent start = new Intent(getActivity(), LoginActivity.class);
                                                        SharedPreferencesUtils.saveIsFirstLogin(getContext(), true);
                                                        startActivity(start);
                                                    }
                                                });
                                    }
                                });
                            }
                        });
                        /**
                         * 更改密码
                         */
                        PIMPopWindowUtils.pimAccountPassword.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PIMPopWindowUtils.popWindow.dismiss();
                                PIMPopWindowUtils.showPopupWindow(getActivity(), getContext(), v, 40022);
                                PIMPopWindowUtils.updateUserAccount.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        OkGo.<String>put(ApiUrl.url + "/user/" + SharedPreferencesUtils.getUserLoginInfo(getActivity()).get("userId") + "/loginPassword")
                                                .headers("token", SharedPreferencesUtils.getUserLoginInfo(getActivity()).get("token"))
                                                .params("loginPassword", PIMPopWindowUtils.editUserAccount.getText().toString())
                                                .execute(new StringCallback() {
                                                    @Override
                                                    public void onSuccess(Response<String> response) {
                                                        Toast toast = Toast.makeText(getContext(), null, Toast.LENGTH_SHORT);
                                                        toast.setText("用户密码修改成功，请重新登陆");
                                                        toast.show();
                                                        PIMPopWindowUtils.popWindow.dismiss();
                                                        Intent start = new Intent(getActivity(), LoginActivity.class);
                                                        SharedPreferencesUtils.saveIsFirstLogin(getContext(), true);
                                                        startActivity(start);
                                                    }
                                                });
                                    }
                                });
                            }
                        });
                        break;
                    /**
                     * 好友
                     */
                    case 1:
                        break;
                    /**
                     * 在卖
                     */
                    case 2:
                        Intent intent = new Intent(getActivity(), PutActivity.class);
                        startActivity(intent);
                        break;
                    /**
                     * 我的收藏
                     */
                    case 3:
                        break;
                    /**
                     * 安全中心
                     */
                    case 5:
                        break;
                    /**
                     * 设置
                     */
                    case 6:
                        break;
                    /**
                     * 隐私
                     */
                    case 7:
                        break;
                    /**
                     * 关于
                     */
                    case 8:
                        break;
                    /**
                     * 通讯录
                     */
                    case 9:
                        break;
                    /**
                     * 退出登录
                     */
                    case 11:
                        EMClient.getInstance().logout(true);
                        Intent start = new Intent(getActivity(), LoginActivity.class);
                        SharedPreferencesUtils.saveIsFirstLogin(getContext(), true);
                        startActivity(start);
                        break;
                }
            }
        });
    }
}
