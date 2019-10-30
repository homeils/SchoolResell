package com.renoside.schoolresell.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.renoside.schoolresell.Adapter.MsgRcvAdapter;
import com.renoside.schoolresell.Entity.MsgEntity;
import com.renoside.schoolresell.Entity.TalkEntity;
import com.renoside.schoolresell.R;
import com.renoside.schoolresell.TalkActivity;
import com.renoside.schoolresell.Utils.ApiUrl;
import com.renoside.schoolresell.Utils.EasemobUtils;
import com.renoside.searchbox.SearchBox;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragmentMsg extends Fragment {

    @BindView(R.id.msg_recyclerview)
    RecyclerView msgRecyclerview;
    public Unbinder bind;
    /**
     * 定义数据集合
     */
    private List<MsgEntity> dataList = new ArrayList<>();
    private MsgRcvAdapter rcvAdapter;
    Map<String, EMConversation> conversations = new HashMap<>();
    private boolean isBack = true;
    /**
     * 更新界面显示新收到的信息数目
     */
    Handler notify = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 200:
                    rcvAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * 环信的信息监听
     */
    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            setDataList();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            //消息被撤回
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_msg, container, false);
        bind = ButterKnife.bind(this, view);
        isBack = true;
        EasemobUtils.initEasemob(getContext());
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
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
    public void onResume() {
        super.onResume();
        if (!isBack) {
            setDataList();
        }
        isBack = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
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
        conversations = EMClient.getInstance().chatManager().getAllConversations();
        /**
         * 逐个显示会话
         */
        for (Map.Entry<String, EMConversation> entry : conversations.entrySet()) {
            Handler handler = new Handler(){
                @Override
                public void handleMessage(@NonNull Message msg) {
                    Bundle data = msg.getData();
                    if (msg.what == 200) {
                        //填装
                        MsgEntity msgItem = new MsgEntity();
                        msgItem.setMsgKey(data.getString("msgKey"));
                        msgItem.setMsgImg(data.getString("msgImg"));
                        msgItem.setMsgId(data.getString("msgId"));
                        msgItem.setMsgTitle(data.getString("msgTitle"));
                        msgItem.setMsgInfo(data.getString("msgInfo"));
                        msgItem.setMsgTime(data.getString("msgTime"));
                        msgItem.setMsgNum(data.getString("msgNum"));
                        dataList.add(msgItem);
                        Message message = new Message();
                        message.what = 200;
                        notify.sendMessage(message);
                    }
                }
            };
            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(entry.getKey());
            //会话发送方
            String msgKey = entry.getKey();
            String loginName = msgKey.replace("_", "@");
            OkGo.<String>get(ApiUrl.url+"/user/"+loginName+"/getInfoByLoginName")
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            if (response.code() == 200) {
                                JSONObject jsonObject = JSONObject.parseObject(response.body());
                                String msgTitle = jsonObject.getString("userName");
                                String msgImg = jsonObject.getString("userImg");
                                //会话最后一条信息
                                String msgInfo = conversation.getLastMessage().getBody().toString().substring(5, conversation.getLastMessage().getBody().toString().length() - 1);
                                //会话最后一条信息时间
                                long temp = conversation.getLastMessage().getMsgTime();
                                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
                                Date date = new Date(temp);
                                String msgTime = sdf.format(date);
                                //会话未读信息数量
                                String msgNum = String.valueOf(conversation.getUnreadMsgCount());
                                Bundle bundle = new Bundle();
                                bundle.putString("msgKey", msgKey);
                                bundle.putString("msgId", conversation.conversationId());
                                bundle.putString("msgImg", msgImg);
                                bundle.putString("msgTitle", msgTitle);
                                bundle.putString("msgInfo", msgInfo);
                                bundle.putString("msgTime", msgTime);
                                bundle.putString("msgNum", msgNum);
                                Message msg = new Message();
                                msg.what = 200;
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            }
                        }
                    });
        }
    }

    private void initListener() {
        rcvAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EMClient.getInstance().chatManager().getConversation(dataList.get(position).getMsgKey()).markAllMessagesAsRead();
                Intent intent = new Intent(getActivity(), TalkActivity.class);
                intent.putExtra("to_chat_username", dataList.get(position).getMsgKey());
                intent.putExtra("talkName", dataList.get(position).getMsgTitle());
                startActivity(intent);
            }
        });
        rcvAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("删除会话");
                builder.setMessage("这将删除您和他的聊天记录，确定要删除这个对话？");
                builder.setIcon(R.mipmap.app_icon);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EMClient.getInstance().chatManager().deleteConversation(dataList.get(position).getMsgKey(), true);
                        dataList.remove(position);
                        Message msg = new Message();
                        msg.what = 200;
                        notify.sendMessage(msg);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder
                        .create();
                dialog.show();
                return false;
            }
        });
    }

}
