package com.renoside.schoolresell;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.renoside.schoolresell.Adapter.PutRcvAdapter;
import com.renoside.schoolresell.Bean.Order;
import com.renoside.schoolresell.Entity.PutEntity;
import com.renoside.schoolresell.Utils.ApiUrl;
import com.renoside.schoolresell.Utils.IndentPopWindowUtil;
import com.renoside.schoolresell.Utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PutActivity extends AppCompatActivity {

    @BindView(R.id.put_header)
    LinearLayout putHeader;
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
        /**
         * 设置监听
         */
        initListener();
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
                                putEntity.setGoodsId(orderObject.getGoods().get(i).getGoodsId());
                                putEntity.setPutImg(orderObject.getGoods().get(i).getGoodsImgs().get(0).getGoodsImg());
                                putEntity.setPutTitle(orderObject.getGoods().get(i).getGoodsName());
                                putEntity.setPutDescription(orderObject.getGoods().get(i).getGoodsDescription());
                                putEntity.setPutStatus(orderObject.getGoods().get(i).getGoodsStatus() == 1001 ? "在售中" : (orderObject.getGoods().get(i).getGoodsStatus() == 1002 ? "交易中" : "已完成"));
                                dataList.add(putEntity);
                            }
                        }
                        rcvAdapter.notifyDataSetChanged();
                    }
                });
    }

    public void initListener() {
        /**
         * 下架商品和取消订单
         */
        rcvAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (dataList.get(position).getPutStatus().equals("在售中")) {
                    IndentPopWindowUtil.showPopupWindow(PutActivity.this, PutActivity.this, view, 1001);
                    /**
                     * 下架商品
                     */
                    IndentPopWindowUtil.popOkOrder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            OkGo.<String>delete(ApiUrl.url + "/goods/" + dataList.get(position).getGoodsId())
                                    .headers("token", SharedPreferencesUtils.getUserLoginInfo(PutActivity.this).get("token"))
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(Response<String> response) {
                                            Toast toast = Toast.makeText(PutActivity.this, null, Toast.LENGTH_SHORT);
                                            toast.setText("商品下架成功！");
                                            toast.show();
                                            IndentPopWindowUtil.popWindow.dismiss();
                                            setDataList();
                                        }
                                    });
                        }
                    });
                    /**
                     * 修改商品信息
                     */
                    IndentPopWindowUtil.popChannelOrder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(PutActivity.this, SellActivity.class);
                            intent.putExtra("goodsId", dataList.get(position).getGoodsId());
                            startActivity(intent);
                        }
                    });
                } else if (dataList.get(position).getPutStatus().equals("交易中")) {
                    IndentPopWindowUtil.showPopupWindow(PutActivity.this, PutActivity.this, view, 10022);
                    /**
                     * 下架商品
                     */
                    IndentPopWindowUtil.popOkOrder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            OkGo.<String>delete(ApiUrl.url + "/goods/" + dataList.get(position).getGoodsId())
                                    .headers("token", SharedPreferencesUtils.getUserLoginInfo(PutActivity.this).get("token"))
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(Response<String> response) {
                                            Toast toast = Toast.makeText(PutActivity.this, null, Toast.LENGTH_SHORT);
                                            toast.setText("商品下架成功！");
                                            toast.show();
                                            IndentPopWindowUtil.popWindow.dismiss();
                                            setDataList();
                                        }
                                    });
                        }
                    });
                    /**
                     * 取消订单
                     */
                    IndentPopWindowUtil.popChannelOrder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast toast = Toast.makeText(PutActivity.this, null, Toast.LENGTH_SHORT);
                            toast.setText("您可以联系买家取消订单，或者直接下架商品哦！");
                            toast.show();
                            IndentPopWindowUtil.popWindow.dismiss();
                            setDataList();
                        }
                    });
                }
            }
        });
    }
}
