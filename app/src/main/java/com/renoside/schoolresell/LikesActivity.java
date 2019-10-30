package com.renoside.schoolresell;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
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
import com.renoside.schoolresell.Adapter.LikesRcvAdapter;
import com.renoside.schoolresell.Bean.Goods;
import com.renoside.schoolresell.Entity.LikesEntity;
import com.renoside.schoolresell.Utils.ApiUrl;
import com.renoside.schoolresell.Utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LikesActivity extends AppCompatActivity {

    @BindView(R.id.likes_recyclerview)
    RecyclerView likesRecyclerview;

    private LikesRcvAdapter rcvAdapter;
    /**
     * 定义数据集合
     */
    private List<LikesEntity> dataList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);
        ButterKnife.bind(this);
        /**
         * 设置数据集合
         */
        setDataList();
        /**
         * 创建并设置适配器
         */
        rcvAdapter = new LikesRcvAdapter(R.layout.likes_item, dataList);
        likesRecyclerview.setAdapter(rcvAdapter);
        /**
         * 设置线性垂直布局管理器
         */
        LinearLayoutManager manager = new LinearLayoutManager(this);
        likesRecyclerview.setLayoutManager(manager);
        /**
         * 设置监听
         */
        initListener();
    }

    private void setDataList() {
        dataList.clear();
        OkGo.<String>get(ApiUrl.url + "/likes/" + SharedPreferencesUtils.getUserLoginInfo(this).get("userId"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.code() == 200) {
                            JSONObject jsonObject = JSON.parseObject(response.body());
                            Goods goodsObject = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<Goods>() {
                            });
                            for (int i = 0; i < goodsObject.getGoods().size(); i++) {
                                LikesEntity likes = new LikesEntity();
                                likes.setGoodsId(goodsObject.getGoods().get(i).getGoodsId());
                                likes.setLikesImg(goodsObject.getGoods().get(i).getGoodsImgs().get(0).getGoodsImg());
                                likes.setLikesName(goodsObject.getGoods().get(i).getGoodsName());
                                likes.setLikesInfo(goodsObject.getGoods().get(i).getGoodsDescription());
                                dataList.add(likes);
                            }
                            rcvAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void initListener() {
        /**
         * 点击事件，到达商品详情
         */
        rcvAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("goodsId", dataList.get(position).getGoodsId());
                Message msg = new Message();
                msg.what = 200;
                msg.setData(bundle);
                GoodsActivity.handler.sendMessage(msg);
                Intent intent = new Intent(LikesActivity.this, GoodsActivity.class);
                startActivity(intent);
            }
        });
        /**
         * 取消商品收藏
         */
        rcvAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                OkGo.<String>delete(ApiUrl.url + "/likes/" + dataList.get(position).getGoodsId())
                        .headers("token", SharedPreferencesUtils.getUserLoginInfo(LikesActivity.this).get("token"))
                        .params("userId", SharedPreferencesUtils.getUserLoginInfo(LikesActivity.this).get("userId"))
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                if (response.code() == 200) {
                                    dataList.remove(position);
                                    rcvAdapter.notifyDataSetChanged();
                                    Toast.makeText(LikesActivity.this, "取消商品收藏成功", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
