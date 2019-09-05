package com.renoside.schoolresell.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.renoside.schoolresell.Adapter.OrderRcvAdapter;
import com.renoside.schoolresell.Bean.Order;
import com.renoside.schoolresell.Entity.OrderEntity;
import com.renoside.schoolresell.PutActivity;
import com.renoside.schoolresell.R;
import com.renoside.schoolresell.Utils.ApiUrl;
import com.renoside.schoolresell.Utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FragmentOrder extends Fragment {

    @BindView(R.id.order_search)
    TextView orderSearch;
    @BindView(R.id.order_recyclerview)
    RecyclerView orderRecyclerview;
    @BindView(R.id.order_sell)
    TextView orderSell;
    @BindView(R.id.order_complete)
    TextView orderComplete;
    public Unbinder bind;

    private OrderRcvAdapter rcvAdapter;
    /**
     * 定义数据集合
     */
    private List<OrderEntity> dataList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_order, container, false);
        bind = ButterKnife.bind(this, view);
        /**
         * 设置数据集合
         */
        setDataList();
        /**
         * 创建并设置适配器
         */
        rcvAdapter = new OrderRcvAdapter(R.layout.order_item, dataList);
        orderRecyclerview.setAdapter(rcvAdapter);
        /**
         * 设置线性垂直布局管理器
         */
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        orderRecyclerview.setLayoutManager(manager);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    @OnClick(R.id.order_sell)
    public void orderOnClick(View view) {
        if (view.getId() == R.id.order_sell) {
            Intent intent = new Intent(getActivity(), PutActivity.class);
            startActivity(intent);
        }
    }

    private void setDataList() {
        OkGo.<String>get(ApiUrl.url+"/order/"+ SharedPreferencesUtils.getUserLoginInfo(getContext()).get("userId"))
                .headers("token", String.valueOf(SharedPreferencesUtils.getIsFirstLogin(getContext()).get("token")))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        dataList.clear();
                        JSONObject jsonObject = JSON.parseObject(response.body());
                        Order orderObject = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<Order>(){});
                        if (orderObject != null){
                            for (int i = 0; i < orderObject.getGoods().size(); i++) {
                                OrderEntity orderEntity = new OrderEntity();
                                orderEntity.setOrderTitle(orderObject.getGoods().get(i).getGoodsName());
                                orderEntity.setOrderDescription(orderObject.getGoods().get(i).getGoodsDescription());
                                orderEntity.setOrderTime(orderObject.getGoods().get(i).getGoodsAddress());
                                dataList.add(orderEntity);
                            }
                        }
                        rcvAdapter.notifyDataSetChanged();
                    }
                });
    }

}
