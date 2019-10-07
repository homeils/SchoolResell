package com.renoside.schoolresell.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.renoside.schoolresell.Adapter.PIMRcvAdapter;
import com.renoside.schoolresell.Bean.UserInfo;
import com.renoside.schoolresell.Entity.PIMEntity;
import com.renoside.schoolresell.R;
import com.renoside.schoolresell.Utils.ApiUrl;
import com.renoside.schoolresell.Utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_pim, container, false);
        bind = ButterKnife.bind(this, view);
        /**
         * 设置数据集合
         */
        setDataList();
        /**
         * 创建并设置适配器
         */
        PIMRcvAdapter rcvAdapter = new PIMRcvAdapter(dataList);
        pimRecyclerview.setAdapter(rcvAdapter);
        /**
         * 设置线性垂直布局管理器
         */
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        pimRecyclerview.setLayoutManager(manager);
        return view;
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
        OkGo.<String>get(ApiUrl.url+"/user/"+ SharedPreferencesUtils.getUserLoginInfo(getContext()).get("userId"))
                .headers("token", SharedPreferencesUtils.getUserLoginInfo(getContext()).get("token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        JSONObject jsonObject = JSON.parseObject(response.body());
                        UserInfo userInfo = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<UserInfo>(){});
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

}
