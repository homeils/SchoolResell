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

import com.renoside.schoolresell.Adapter.OrderRcvAdapter;
import com.renoside.schoolresell.Entity.OrderEntity;
import com.renoside.schoolresell.PutActivity;
import com.renoside.schoolresell.R;

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

    /**
     * 定义数据集合
     */
    private List<OrderEntity> dataList;

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
        OrderRcvAdapter rcvAdapter = new OrderRcvAdapter(R.layout.order_item, dataList);
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
        dataList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setOrderTitle("标题");
            orderEntity.setOrderDescription("描述");
            orderEntity.setOrderTime("2019-7-28");
            dataList.add(orderEntity);
        }
    }

}
