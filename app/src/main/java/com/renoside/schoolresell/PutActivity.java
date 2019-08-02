package com.renoside.schoolresell;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.renoside.schoolresell.Adapter.OrderRcvAdapter;
import com.renoside.schoolresell.Adapter.PutRcvAdapter;
import com.renoside.schoolresell.Entity.OrderEntity;
import com.renoside.schoolresell.Entity.PutEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PutActivity extends AppCompatActivity {

    @BindView(R.id.put_header)
    LinearLayout putHeader;
    @BindView(R.id.put_search)
    TextView putSearch;
    @BindView(R.id.put_cancel)
    TextView putCancel;
    @BindView(R.id.put_recyclerview)
    RecyclerView putRecyclerview;

    /**
     * 定义数据集合
     */
    private List<PutEntity> dataList;

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
        PutRcvAdapter rcvAdapter = new PutRcvAdapter(R.layout.put_item, dataList);
        putRecyclerview.setAdapter(rcvAdapter);
        /**
         * 设置线性垂直布局管理器
         */
        LinearLayoutManager manager = new LinearLayoutManager(this);
        putRecyclerview.setLayoutManager(manager);
    }

    private void setDataList() {
        dataList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            PutEntity putEntity = new PutEntity();
            putEntity.setPutTitle("标题");
            putEntity.setPutDescription("描述");
            putEntity.setPutTime("2019-7-28");
            dataList.add(putEntity);
        }
    }

}
