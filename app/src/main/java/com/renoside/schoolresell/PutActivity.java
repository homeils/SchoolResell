package com.renoside.schoolresell;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.renoside.schoolresell.Adapter.OrderRcvAdapter;
import com.renoside.schoolresell.Adapter.PutRcvAdapter;
import com.renoside.schoolresell.Bean.Order;
import com.renoside.schoolresell.Entity.OrderEntity;
import com.renoside.schoolresell.Entity.PutEntity;
import com.renoside.schoolresell.Utils.ApiUrl;
import com.renoside.schoolresell.Utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PutActivity extends AppCompatActivity {

    @BindView(R.id.put_header)
    LinearLayout putHeader;
    @BindView(R.id.put_search)
    TextView putSearch;
    @BindView(R.id.put_cancel)
    TextView putCancel;
    @BindView(R.id.put_recyclerview)
    RecyclerView putRecyclerview;

    private PutRcvAdapter rcvAdapter;

    /**
     * 定义数据集合
     */
    private List<PutEntity> dataList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put);
        ButterKnife.bind(this);
        /**
         * 设置数据集合
         */
        setDataList();
        /**
         * 创建并设置适配器
         */
        rcvAdapter = new PutRcvAdapter(R.layout.put_item, dataList);
        putRecyclerview.setAdapter(rcvAdapter);
        /**
         * 设置线性垂直布局管理器
         */
        LinearLayoutManager manager = new LinearLayoutManager(this);
        putRecyclerview.setLayoutManager(manager);
    }

    private void setDataList() {
        OkGo.<String>get(ApiUrl.url + "/shelves/" + SharedPreferencesUtils.getUserLoginInfo(this).get("userId"))
                .headers("token", SharedPreferencesUtils.getUserLoginInfo(this).get("token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        dataList.clear();
                        JSONObject jsonObject = JSON.parseObject(response.body());
                        Order orderObject = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<Order>() {
                        });
                        if (orderObject != null) {
                            for (int i = 0; i < orderObject.getGoods().size(); i++) {
                                PutEntity putEntity = new PutEntity();
                                putEntity.setPutTitle(orderObject.getGoods().get(i).getGoodsName());
                                putEntity.setPutDescription(orderObject.getGoods().get(i).getGoodsDescription());
                                putEntity.setPutTime(orderObject.getGoods().get(i).getGoodsAddress());
                                dataList.add(putEntity);
                            }
                        }
                        rcvAdapter.notifyDataSetChanged();
                    }
                });
    }

}
