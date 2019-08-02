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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.renoside.schoolresell.Adapter.ShopRcvAdapter;
import com.renoside.schoolresell.Entity.ShopEntity;
import com.renoside.schoolresell.GoodsActivity;
import com.renoside.schoolresell.R;
import com.renoside.schoolresell.SellActivity;
import com.renoside.searchbox.OnRightIcoListener;
import com.renoside.searchbox.SearchBox;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragmentShop extends Fragment {

    @BindView(R.id.shop_search)
    SearchBox shopSearch;
    @BindView(R.id.shop_ok)
    TextView shopOk;
    @BindView(R.id.shop_recyclerview)
    RecyclerView shopRecyclerview;
    public Unbinder bind;

    /**
     * 定义数据集合
     */
    private List<ShopEntity> dataList;
    /**
     * 定义适配器
     */
    private ShopRcvAdapter rcvAdapter;
    /**
     * 定义检索图标和标题集合
     */
    private Integer[] shopChannelImgs = {R.drawable.shop_channel_1, R.drawable.shop_channel_2, R.drawable.shop_channel_3,
            R.drawable.shop_channel_4, R.drawable.shop_channel_5,};
    private String[] shopChannelTitles = {"电子专区", "书籍笔记", "校园代劳", "人工服务", "急需转让"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_shop, container, false);
        bind = ButterKnife.bind(this, view);
        /**
         * 设置数据集合
         */
        setDataList();
        /**
         * 创建并设置适配器
         */
        rcvAdapter = new ShopRcvAdapter(dataList);
        shopRecyclerview.setAdapter(rcvAdapter);
        /**
         * 设置网格布局管理器
         */
        GridLayoutManager manager = new GridLayoutManager(getContext(), 10);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return dataList.get(position).getSpanSize();
            }
        });
        shopRecyclerview.setLayoutManager(manager);
        /**
         * 初始化监听器
         */
        iniListener();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /**
         * 解除绑定
         */
        bind.unbind();
    }

    /**
     * 设置数据集合方法体
     */
    private void setDataList() {
        dataList = new ArrayList<>();
        ShopEntity banner = new ShopEntity(ShopEntity.SHOP_BANNER);
        dataList.add(banner);
        for (int i = 0; i < 5; i++) {
            ShopEntity channel = new ShopEntity(ShopEntity.SHOP_CHANNEL);
            channel.setShopImg(shopChannelImgs[i]);
            channel.setShopTitle(shopChannelTitles[i]);
            dataList.add(channel);
        }
        ShopEntity recommendHint = new ShopEntity(ShopEntity.SHOP_RECOMMEND_HINT);
        dataList.add(recommendHint);
        for (int i = 0; i < 4; i++) {
            ShopEntity recommend = new ShopEntity(ShopEntity.SHOP_RECOMMEND);
            dataList.add(recommend);
        }
        ShopEntity goodsHint = new ShopEntity(ShopEntity.SHOP_GOODS_HINT);
        dataList.add(goodsHint);
        for (int i = 0; i < 20; i++) {
            ShopEntity goods = new ShopEntity(ShopEntity.SHOP_GOODS);
            dataList.add(goods);
        }
    }

    /**
     * 监听器方法体
     */
    private void iniListener() {
        shopSearch.setOnRightIcoListener(new OnRightIcoListener() {
            @Override
            public void onClick() {
                Toast.makeText(getContext(), "You clicked right ico", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), SellActivity.class);
                startActivity(intent);
            }
        });
        rcvAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(getContext(), "onItemClick" + position, Toast.LENGTH_SHORT).show();
                if (position > 6) {
                    Intent intent = new Intent(getActivity(), GoodsActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

}
