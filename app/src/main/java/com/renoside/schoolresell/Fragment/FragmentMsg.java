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

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.renoside.schoolresell.Adapter.MsgRcvAdapter;
import com.renoside.schoolresell.Entity.MsgEntity;
import com.renoside.schoolresell.R;
import com.renoside.schoolresell.TalkActivity;
import com.renoside.schoolresell.Utils.EasemobUtils;
import com.renoside.searchbox.SearchBox;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    private List<MsgEntity> dataList = new ArrayList<>();
    private MsgRcvAdapter rcvAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_msg, container, false);
        bind = ButterKnife.bind(this, view);
        EasemobUtils.initEasemob(getContext());
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

    /**
     * 初始化用户接收的信息数据，显示用户收到的离线信息
     */
    private void setDataList() {
        dataList.clear();
        /**
         * 加载所有会话
         */
        EMClient.getInstance().chatManager().loadAllConversations();
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        /**
         * 逐个显示会话
         */
        for (Map.Entry<String, EMConversation> entry : conversations.entrySet()) {
            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(entry.getKey());
            //会话发送方
            String msgTitle = entry.getKey();
            //会话最后一条信息
            String msgInfo = conversation.getLastMessage().getBody().toString().substring(5, conversation.getLastMessage().getBody().toString().length() - 1);
            //会话最后一条信息时间
            long temp = conversation.getLastMessage().getMsgTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(temp);
            String msgTime = sdf.format(date);
            //会话未读信息数量
            String msgNum = String.valueOf(conversation.getUnreadMsgCount());
            //填装
            MsgEntity msgItem = new MsgEntity();
            msgItem.setMsgTitle(msgTitle);
            msgItem.setMsgInfo(msgInfo);
            msgItem.setMsgTime(msgTime);
            msgItem.setMsgNum(msgNum);
            dataList.add(msgItem);
        }
    }

    private void initListener() {
        rcvAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), TalkActivity.class);
                intent.putExtra("to_chat_username", dataList.get(position).getMsgTitle());
                startActivity(intent);
            }
        });
    }

}
