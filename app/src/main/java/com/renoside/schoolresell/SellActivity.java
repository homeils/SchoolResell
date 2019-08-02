package com.renoside.schoolresell;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.renoside.schoolresell.Adapter.SellRcvAdapter;
import com.renoside.schoolresell.Entity.SellEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SellActivity extends AppCompatActivity {

    @BindView(R.id.sell_header)
    LinearLayout sellHeader;
    @BindView(R.id.sell_back)
    ImageView sellBack;
    @BindView(R.id.sell_recyclerview)
    RecyclerView sellRecyclerview;
    @BindView(R.id.sell_footer)
    LinearLayout sellFooter;
    @BindView(R.id.sell_help)
    ImageView sellHelp;
    @BindView(R.id.sell_ok)
    TextView sellOk;

    private List<SellEntity> dataList;
    private SellRcvAdapter rcvAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        ButterKnife.bind(this);
        setDataList();
        rcvAdapter = new SellRcvAdapter(dataList);
        sellRecyclerview.setAdapter(rcvAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        sellRecyclerview.setLayoutManager(manager);
    }

    private void setDataList() {
        dataList = new ArrayList<>();
        SellEntity name = new SellEntity(SellEntity.SELL_SMALL_ITEM);
        name.setSellhint("商品名称：");
        dataList.add(name);
        SellEntity description = new SellEntity(SellEntity.SELL_BIG_ITEM);
        description.setSellhint("商品描述：");
        dataList.add(description);
        SellEntity price = new SellEntity(SellEntity.SELL_SMALL_ITEM);
        price.setSellhint("商品价格：");
        dataList.add(price);
        SellEntity phone = new SellEntity(SellEntity.SELL_SMALL_ITEM);
        phone.setSellhint("联系电话：");
        dataList.add(phone);
        SellEntity address = new SellEntity(SellEntity.SELL_BIG_ITEM);
        address.setSellhint("卖家地址：");
        dataList.add(address);
        SellEntity pic = new SellEntity(SellEntity.SELL_PIC_ITEM);
        dataList.add(pic);
    }


}
