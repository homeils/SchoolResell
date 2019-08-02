package com.renoside.schoolresell.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.renoside.schoolresell.Adapter.MsgRcvAdapter;
import com.renoside.schoolresell.Entity.MsgEntity;
import com.renoside.schoolresell.R;
import com.renoside.schoolresell.TalkActivity;
import com.renoside.searchbox.SearchBox;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FragmentMsg extends Fragment {

    @BindView(R.id.msg_search)
    SearchBox msgSearch;
    @BindView(R.id.msg_ok)
    TextView msgOk;
    @BindView(R.id.msg_friends)
    ImageView msgFriends;
    @BindView(R.id.msg_recyclerview)
    RecyclerView msgRecyclerview;
    public Unbinder bind;

    /**
     * 定义数据集合
     */
    private List<MsgEntity> dataList;
    private MsgRcvAdapter rcvAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_msg, container, false);
        bind = ButterKnife.bind(this, view);
        /**
         * 设置数据集合
         */
        setDataList();
        /**
         * 创建并设置适配器
         */
        rcvAdapter = new MsgRcvAdapter(R.layout.msg_item, dataList);
        msgRecyclerview.setAdapter(rcvAdapter);
        /**
         * 设置线性垂直布局管理器
         */
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        msgRecyclerview.setLayoutManager(manager);
        initListener();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    private void setDataList() {
        dataList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            MsgEntity msgItem = new MsgEntity();
            msgItem.setMsgTitle("标题");
            msgItem.setMsgInfo("信息内容");
            msgItem.setMsgTime("2019-7-28");
            msgItem.setMsgNum("99");
            dataList.add(msgItem);
        }
    }

    private void initListener() {
        rcvAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(getContext(), "You clicked msg item " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), TalkActivity.class);
                startActivity(intent);
            }
        });
    }

}
