package com.renoside.schoolresell.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.renoside.schoolresell.Adapter.OrderRcvAdapter;
import com.renoside.schoolresell.Bean.Order;
import com.renoside.schoolresell.Entity.OrderEntity;
import com.renoside.schoolresell.PutActivity;
import com.renoside.schoolresell.R;
import com.renoside.schoolresell.Utils.ApiUrl;
import com.renoside.schoolresell.Utils.IndentPopWindowUtil;
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
//    @BindView(R.id.order_complete)
//    TextView orderComplete;
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
        initListener();
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
        /**
         * 拉取订单信息
         */
        OkGo.<String>get(ApiUrl.url + "/order/" + SharedPreferencesUtils.getUserLoginInfo(getContext()).get("userId"))
                .headers("token", SharedPreferencesUtils.getUserLoginInfo(getContext()).get("token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        dataList.clear();
                        JSONObject jsonObject = JSON.parseObject(response.body());
                        Order orderObject = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<Order>() {
                        });
                        if (orderObject != null && orderObject.getGoods() != null && orderObject.getGoods().size() != 0) {
                            for (int i = 0; i < orderObject.getGoods().size(); i++) {
                                OrderEntity orderEntity = new OrderEntity();
                                orderEntity.setGoodsId(orderObject.getGoods().get(i).getGoodsId());
                                orderEntity.setOrderImg(orderObject.getGoods().get(i).getGoodsImgs().get(0).getGoodsImg());
                                orderEntity.setOrderTitle(orderObject.getGoods().get(i).getGoodsName());
                                orderEntity.setOrderDescription(orderObject.getGoods().get(i).getGoodsDescription());
                                if (orderObject.getGoods().get(i).getGoodsStatus() == 1002) {
                                    orderEntity.setOrderStatus("交易中");
                                } else if (orderObject.getGoods().get(i).getGoodsStatus() == 1003) {
                                    orderEntity.setOrderStatus("已完成");
                                }
                                dataList.add(orderEntity);
                            }
                            rcvAdapter.notifyDataSetChanged();
                        }
                        if (response.code() == 200) {
                            rcvAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void initListener() {
        /**
         * 取消订单和确认收货
         */
        rcvAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                IndentPopWindowUtil.showPopupWindow(getActivity(), getContext(), view, (dataList.get(position).getOrderStatus().equals("交易中") ? 10021 : 1003));
                /**
                 * 确认收货按钮
                 */
                IndentPopWindowUtil.popOkOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OkGo.<String>put(ApiUrl.url + "/order/" + dataList.get(position).getGoodsId())
                                .headers("token", SharedPreferencesUtils.getUserLoginInfo(getContext()).get("token"))
                                .params("buyerId", SharedPreferencesUtils.getUserLoginInfo(getContext()).get("userId"))
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        Toast toast = Toast.makeText(getContext(), null, Toast.LENGTH_SHORT);
                                        toast.setText("确认收货成功，感谢使用！");
                                        toast.show();
                                        IndentPopWindowUtil.popWindow.dismiss();
                                        setDataList();
                                    }
                                });
                    }
                });
                /**
                 * 取消订单按钮
                 */
                IndentPopWindowUtil.popChannelOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OkGo.<String>delete(ApiUrl.url + "/order/" + dataList.get(position).getGoodsId())
                                .headers("token", SharedPreferencesUtils.getUserLoginInfo(getContext()).get("token"))
                                .params("buyerId", SharedPreferencesUtils.getUserLoginInfo(getContext()).get("userId"))
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        Toast toast = Toast.makeText(getContext(), null, Toast.LENGTH_SHORT);
                                        toast.setText("你已成功取消订单！");
                                        toast.show();
                                        IndentPopWindowUtil.popWindow.dismiss();
                                        setDataList();
                                    }
                                });
                    }
                });
            }
        });
    }
}
