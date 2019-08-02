package com.renoside.schoolresell;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.renoside.schoolresell.Adapter.GoodsRcvAdapter;
import com.renoside.schoolresell.Entity.GoodsEntity;

import java.util.ArrayList;
import java.util.List;

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

    private List<GoodsEntity> dataList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        ButterKnife.bind(this);
        goodsLike.setSelected(false);
        setDataList();
        GoodsRcvAdapter rcvAdapter = new GoodsRcvAdapter(dataList);
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
    }

    private void setDataList() {
        dataList = new ArrayList<>();
        GoodsEntity banner = new GoodsEntity(GoodsEntity.GOODS_BANNER);
        dataList.add(banner);
        GoodsEntity namePrice = new GoodsEntity(GoodsEntity.GOODS_NAME_PRICE);
        namePrice.setGoodsName("商品名");
        namePrice.setGoodsPrice("999.9");
        dataList.add(namePrice);
        GoodsEntity description = new GoodsEntity(GoodsEntity.GOODS_DESCRIPTION);
        description.setGoodsDescription("描述\n描述\n描述\n描述\n描述\n描述\n描述\n描述\n描述\n描述\n" +
                                        "描述\n描述\n描述\n描述\n描述\n描述\n描述\n描述\n描述\n描述\n" +
                                        "描述\n描述\n描述\n描述\n描述\n描述\n描述\n描述\n描述\n描述\n");
        dataList.add(description);
        for (int i = 0; i < 16; i++) {
            GoodsEntity goods = new GoodsEntity(GoodsEntity.GOODS_LIST);
            goods.setGoodsName("这是商品列表");
            goods.setGoodsDescription("描述");
            goods.setGoodsPrice("999.9");
            goods.setGoodsLikes("999");
            dataList.add(goods);
        }
    }

    @OnClick(R.id.goods_like)
    public void goodsOnClick(View view) {
        if (view.getId() == R.id.goods_like) {
            if (!goodsLike.isSelected()) {
                goodsLike.setSelected(true);
            } else {
                goodsLike.setSelected(false);
            }
        }
    }
}
