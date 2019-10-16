package com.renoside.schoolresell.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.renoside.schoolresell.Adapter.ShopRcvAdapter;
import com.renoside.schoolresell.Bean.Goods;
import com.renoside.schoolresell.Entity.ShopEntity;
import com.renoside.schoolresell.GoodsActivity;
import com.renoside.schoolresell.R;
import com.renoside.schoolresell.SellActivity;
import com.renoside.schoolresell.Utils.ApiUrl;
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
    private List<ShopEntity> dataList = new ArrayList<>();
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
        OkGo.<String>get(ApiUrl.url + "/goods")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        dataList.clear();
                        JSONObject jsonObject = JSON.parseObject(response.body());
                        Goods goodsObject = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<Goods>() {
                        });
                        if (goodsObject != null && goodsObject.getGoods() != null && goodsObject.getGoods().size() != 0) {
                            /**
                             * 轮播数据
                             */
                            ShopEntity banner = new ShopEntity(ShopEntity.SHOP_BANNER);
                            dataList.add(banner);
                            /**
                             * 导航通道数据
                             */
                            for (int i = 0; i < 5; i++) {
                                ShopEntity channel = new ShopEntity(ShopEntity.SHOP_CHANNEL);
                                channel.setShopImg(shopChannelImgs[i]);
                                channel.setShopTitle(shopChannelTitles[i]);
                                dataList.add(channel);
                            }
                            /**
                             * 热门推荐提示
                             */
                            ShopEntity recommendHint = new ShopEntity(ShopEntity.SHOP_RECOMMEND_HINT);
                            dataList.add(recommendHint);
                            /**
                             * 热度排序
                             */
                            Goods sortedDataList = sortByLikes(goodsObject);
                            /**
                             * 推荐数据
                             */
                            for (int i = 0; i < 4; i++) {
                                if (goodsObject.getGoods().size() >= 4) {
                                    ShopEntity recommend = new ShopEntity(ShopEntity.SHOP_RECOMMEND);
                                    recommend.setShopId(sortedDataList.getGoods().get(i).getGoodsId());
                                    recommend.setShopImg(sortedDataList.getGoods().get(i).getGoodsImgs().get(0).getGoodsImg());
                                    recommend.setShopTitle(sortedDataList.getGoods().get(i).getGoodsName());
                                    recommend.setShopDescription(sortedDataList.getGoods().get(i).getGoodsDescription());
                                    recommend.setShopPrice(String.valueOf(sortedDataList.getGoods().get(i).getGoodsPrice()));
                                    recommend.setShopLikes(String.valueOf(sortedDataList.getGoods().get(i).getGoodsLikes()));
                                    dataList.add(recommend);
                                } else {
                                    ShopEntity recommend = new ShopEntity(ShopEntity.SHOP_RECOMMEND);
                                    recommend.setShopId(sortedDataList.getGoods().get(0).getGoodsId());
                                    recommend.setShopImg(goodsObject.getGoods().get(0).getGoodsImgs().get(0).getGoodsImg());
                                    recommend.setShopTitle(goodsObject.getGoods().get(0).getGoodsName());
                                    recommend.setShopDescription(goodsObject.getGoods().get(0).getGoodsDescription());
                                    recommend.setShopPrice(String.valueOf(goodsObject.getGoods().get(0).getGoodsPrice()));
                                    recommend.setShopLikes(String.valueOf(goodsObject.getGoods().get(0).getGoodsLikes()));
                                    dataList.add(recommend);
                                }
                            }
                            /**
                             * 商品列表提示
                             */
                            ShopEntity goodsHint = new ShopEntity(ShopEntity.SHOP_GOODS_HINT);
                            dataList.add(goodsHint);
                            /**
                             * 商品列表数据
                             */
                            OkGo.<String>get(ApiUrl.url + "/goods")
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(Response<String> response) {
                                            JSONObject jsonObject = JSON.parseObject(response.body());
                                            Goods goodsObject = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<Goods>() {
                                            });
                                            for (int i = 0; i < goodsObject.getGoods().size(); i++) {
                                                ShopEntity goods = new ShopEntity(ShopEntity.SHOP_GOODS);
                                                goods.setShopId(goodsObject.getGoods().get(i).getGoodsId());
                                                goods.setShopImg(goodsObject.getGoods().get(i).getGoodsImgs().get(0).getGoodsImg());
                                                goods.setShopTitle(goodsObject.getGoods().get(i).getGoodsName());
                                                goods.setShopDescription(goodsObject.getGoods().get(i).getGoodsDescription());
                                                goods.setShopPrice(String.valueOf(goodsObject.getGoods().get(i).getGoodsPrice()));
                                                goods.setShopLikes(String.valueOf(goodsObject.getGoods().get(i).getGoodsLikes()));
                                                dataList.add(goods);
                                            }
                                            rcvAdapter.notifyDataSetChanged();
                                        }
                                    });
                        }
                    }
                });
    }

    /**
     * 监听器方法体
     */
    private void iniListener() {
        /**
         * 上架商品按钮
         */
        shopSearch.setOnRightIcoListener(new OnRightIcoListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(getActivity(), SellActivity.class);
                startActivity(intent);
            }
        });
        /**
         * 适配器item监听
         */
        rcvAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(getContext(), "onItemClick" + position, Toast.LENGTH_SHORT).show();
                if ((position >= 7 && position < 11) || position > 11) {
                    Bundle bundle = new Bundle();
                    bundle.putString("goodsId", dataList.get(position).getShopId());
                    Message msg = new Message();
                    msg.what = 200;
                    msg.setData(bundle);
                    GoodsActivity.handler.sendMessage(msg);
                    Intent intent = new Intent(getActivity(), GoodsActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 热度排序
     *
     * @param goods
     * @return
     */
    private Goods sortByLikes(Goods goods) {
        Goods result = goods;
        for (int i = 0; i < result.getGoods().size(); i++) {
            for (int j = 0; j < result.getGoods().size() - 1 - i; j++) {
                if (result.getGoods().get(j).getGoodsLikes() < result.getGoods().get(j + 1).getGoodsLikes()) {
                    Goods.GoodsBean temp = result.getGoods().get(j);
                    result.getGoods().set(j, result.getGoods().get(j + 1));
                    result.getGoods().set(j + 1, temp);
                }
            }
        }
        return result;
    }
}
