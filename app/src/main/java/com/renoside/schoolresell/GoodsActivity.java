package com.renoside.schoolresell;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.renoside.schoolresell.Adapter.GoodsRcvAdapter;
import com.renoside.schoolresell.Bean.Goods;
import com.renoside.schoolresell.Bean.GoodsInfo;
import com.renoside.schoolresell.Entity.GoodsEntity;
import com.renoside.schoolresell.Utils.ApiUrl;
import com.renoside.schoolresell.Utils.EasemobUtils;
import com.renoside.schoolresell.Utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoodsActivity extends AppCompatActivity {

    @BindView(R.id.goods_back)
    ImageView goodsBack;
    @BindView(R.id.goods_like)
    ImageView goodsLike;
    @BindView(R.id.goods_recyclerview)
    RecyclerView goodsRecyclerview;
    @BindView(R.id.goods_phone)
    TextView goodsPhone;
    @BindView(R.id.goods_buy)
    TextView goodsBuy;

    private int imgNum = 0;
    private static String goodsId;
    private GoodsRcvAdapter rcvAdapter;
    private List<GoodsEntity> dataList = new ArrayList<>();
    /**
     * 获取商品ID
     */
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 200:
                    Bundle bundle = msg.getData();
                    goodsId = bundle.getString("goodsId");
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        ButterKnife.bind(this);
        goodsLike.setSelected(false);
        setDataList();
        rcvAdapter = new GoodsRcvAdapter(dataList);
        goodsRecyclerview.setAdapter(rcvAdapter);
        /**
         * 设置网格布局管理器
         */
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return dataList.get(position).getSpanSize();
            }
        });
        goodsRecyclerview.setLayoutManager(manager);
        initListener();
    }

    /**
     * 加载商品数据
     */
    private void setDataList() {
        dataList.clear();
        imgNum = 0;
        /**
         * 请求添加商品列表数据
         */
        Handler loadGoodsList = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 200:
                        OkGo.<String>get(ApiUrl.url + "/goods")
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        JSONObject jsonObject = JSON.parseObject(response.body());
                                        Goods goodsObject = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<Goods>() {
                                        });
                                        for (int i = 0; i < goodsObject.getGoods().size(); i++) {
                                            if (!goodsId.equals(goodsObject.getGoods().get(i).getGoodsId())) {
                                                GoodsEntity goods = new GoodsEntity(GoodsEntity.GOODS_LIST);
                                                goods.setGoodsId(goodsObject.getGoods().get(i).getGoodsId());
                                                goods.setGoodsImg(goodsObject.getGoods().get(i).getGoodsImgs().get(0).getGoodsImg());
                                                goods.setGoodsName(goodsObject.getGoods().get(i).getGoodsName());
                                                goods.setGoodsDescription(goodsObject.getGoods().get(i).getGoodsDescription());
                                                goods.setGoodsPrice(String.valueOf(goodsObject.getGoods().get(i).getGoodsPrice()));
                                                goods.setGoodsLikes(String.valueOf(goodsObject.getGoods().get(i).getGoodsLikes()));
                                                dataList.add(goods);
                                            }
                                        }
                                        rcvAdapter.notifyDataSetChanged();
                                    }
                                });
                        break;
                }
            }
        };
        /**
         * 加载商品详情数据
         */
        OkGo.<String>get(ApiUrl.url + "/goods/" + goodsId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        JSONObject jsonObject = JSON.parseObject(response.body());
                        GoodsInfo goodsInfo = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<GoodsInfo>() {
                        });
                        GoodsEntity namePrice = new GoodsEntity(GoodsEntity.GOODS_NAME_PRICE);
                        if (goodsInfo.getGoodsName() != null && !goodsInfo.getGoodsName().equals("")) {
                            namePrice.setSellerId(goodsInfo.getSellerId());
                            namePrice.setGoodsName(goodsInfo.getGoodsName());
                        }
                        if (goodsInfo.getGoodsPrice() != null) {
                            namePrice.setGoodsPrice(String.valueOf(goodsInfo.getGoodsPrice()));
                        }
                        dataList.add(namePrice);
                        GoodsEntity description = new GoodsEntity(GoodsEntity.GOODS_DESCRIPTION);
                        if (goodsInfo.getGoodsDescription() != null && !goodsInfo.getGoodsDescription().equals("")) {
                            description.setGoodsDescription(goodsInfo.getGoodsDescription());
                        }
                        dataList.add(description);
                        /**
                         * 添加图片
                         */
                        for (int i = 0; i < goodsInfo.getGoodsImgs().size(); i++) {
                            imgNum++;
                            GoodsEntity img = new GoodsEntity(GoodsEntity.GOODS_IMG);
                            img.setGoodsImg(goodsInfo.getGoodsImgs().get(i).getGoodsImg());
                            dataList.add(img);
                        }
                        Message msg = new Message();
                        msg.what = 200;
                        loadGoodsList.sendMessage(msg);
                        rcvAdapter.notifyDataSetChanged();
                    }
                });
    }

    /**
     * 按钮事件监听
     *
     * @param view
     */
    @OnClick({R.id.goods_back, R.id.goods_phone, R.id.goods_like, R.id.goods_buy})
    public void goodsOnClick(View view) {
        if (view.getId() == R.id.goods_back) {
            GoodsActivity.this.finish();
        } else if (view.getId() == R.id.goods_like) {
            /**
             * 收藏按钮监听
             */
            if (!goodsLike.isSelected()) {
                /**
                 * 收藏商品
                 */
                OkGo.<String>post(ApiUrl.url + "/likes/" + goodsId)
                        .headers("token", SharedPreferencesUtils.getUserLoginInfo(GoodsActivity.this).get("token"))
                        .params("userId", SharedPreferencesUtils.getUserLoginInfo(GoodsActivity.this).get("userId"))
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                if (response.code() == 200) {
                                    Toast.makeText(GoodsActivity.this, "收藏成功！", Toast.LENGTH_SHORT).show();
                                    goodsLike.setSelected(true);
                                } else if (response.code() == 403) {
                                    Toast.makeText(GoodsActivity.this, "你已经收藏过该商品啦，去收藏列表看看吧！", Toast.LENGTH_SHORT).show();
                                    goodsLike.setSelected(true);
                                }
                            }
                        });
            } else {
                /**
                 * 取消收藏
                 */
                OkGo.<String>delete(ApiUrl.url + "/likes/" + goodsId)
                        .headers("token", SharedPreferencesUtils.getUserLoginInfo(GoodsActivity.this).get("token"))
                        .params("userId", SharedPreferencesUtils.getUserLoginInfo(GoodsActivity.this).get("userId"))
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                if (response.code() == 200) {
                                    goodsLike.setSelected(false);
                                }
                            }
                        });
            }
        } else if (view.getId() == R.id.goods_buy) {
            /**
             * 下单按钮监听
             */
            /**
             * 展示上传进度条
             */
            ProgressDialog pd2 = new ProgressDialog(GoodsActivity.this);
            pd2.setTitle("正在购买");
            pd2.setIcon(R.mipmap.app_icon);
            pd2.setMessage("正在为您下单...");
            pd2.setCancelable(false);
            pd2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd2.show();
            /**
             * 新开线程用于下单
             */
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkGo.<String>post(ApiUrl.url + "/order/" + goodsId)
                            .headers("token", SharedPreferencesUtils.getUserLoginInfo(GoodsActivity.this).get("token"))
                            .params("buyerId", SharedPreferencesUtils.getUserLoginInfo(GoodsActivity.this).get("userId"))
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    if (response.code() == 200) {
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        pd2.cancel();
                                        Toast toast = Toast.makeText(GoodsActivity.this, null, Toast.LENGTH_SHORT);
                                        toast.setText("下单成功");
                                        toast.show();
                                        Intent intent = new Intent(GoodsActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    } else if (response.code() == 403) {
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        pd2.dismiss();
                                        Toast toast = Toast.makeText(GoodsActivity.this, null, Toast.LENGTH_SHORT);
                                        toast.setText("不能购买自己的商品哦或者您已经下单啦");
                                        toast.show();
                                    }
                                }
                            });
                }
            }).start();
        } else if (view.getId() == R.id.goods_phone) {
            /**
             * 发起聊天
             */
            if (dataList.get(0).getSellerId().equals(SharedPreferencesUtils.getUserLoginInfo(GoodsActivity.this).get("userId"))) {
                Toast toast = Toast.makeText(GoodsActivity.this, null, Toast.LENGTH_SHORT);
                toast.setText("不能向自己发起聊天啦");
                toast.show();
            } else {
                OkGo.<String>get(ApiUrl.url + "/user/" + dataList.get(0).getSellerId() + "/loginName")
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                if (response.code() == 200) {
                                    JSONObject jsonObject = JSONObject.parseObject(response.body());
                                    String toChatUsername = jsonObject.getString("loginName").replace("@", "_");
                                    Intent intent = new Intent(GoodsActivity.this, TalkActivity.class);
                                    intent.putExtra("to_chat_username", toChatUsername);
                                    startActivity(intent);
                                }
                            }
                        });
            }
        }
    }

    /**
     * 商品列表监听
     */
    private void initListener() {
        rcvAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position >= (2 + imgNum)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("goodsId", dataList.get(position).getGoodsId());
                    Message msg = new Message();
                    msg.what = 200;
                    msg.setData(bundle);
                    GoodsActivity.handler.sendMessage(msg);
                    Intent intent = new Intent(GoodsActivity.this, GoodsActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
