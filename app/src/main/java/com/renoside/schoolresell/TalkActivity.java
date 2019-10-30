package com.renoside.schoolresell;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.renoside.schoolresell.Adapter.TalkRcvAdapter;
import com.renoside.schoolresell.Entity.TalkEntity;
import com.renoside.schoolresell.Fragment.FragmentMsg;
import com.renoside.schoolresell.Utils.EasemobUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TalkActivity extends AppCompatActivity {

    @BindView(R.id.talk_back)
    ImageView talkBack;
    @BindView(R.id.talk_name)
    TextView talkName;
    @BindView(R.id.talk_recyclerview)
    RecyclerView talkRecyclerview;
    @BindView(R.id.talk_input)
    EditText talkInput;
    @BindView(R.id.talk_ok)
    TextView talkOk;
    @BindView(R.id.talk_constraint)
    ConstraintLayout talkConstraint;

    /**
     * 定义数据集合
     */
    private List<TalkEntity> dataList = new ArrayList<>();
    private TalkRcvAdapter rcvAdapter;
    /**
     * 接收到信息的handler，用来刷新界面显示最后一条信息
     */
    Handler notify = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 200:
                    rcvAdapter.notifyItemInserted(dataList.size() - 1);
                    talkRecyclerview.scrollToPosition(dataList.size() - 1);
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
            for (int i = 0; i < messages.size(); i++) {
                TalkEntity receive = new TalkEntity(TalkEntity.TALK_RECEIVED);
                receive.setTalkMsg(messages.get(i).getBody().toString().substring(5, messages.get(i).getBody().toString().length() - 1));
                dataList.add(receive);
                Message msg = new Message();
                msg.what = 200;
                notify.sendMessage(msg);
            }
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);
        ButterKnife.bind(this);
        EasemobUtils.initEasemob(TalkActivity.this);
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
        /**
         * 设置数据集合
         */
        setDataList();
        /**
         * 创建并设置适配器
         */
        rcvAdapter = new TalkRcvAdapter(dataList);
        talkRecyclerview.setAdapter(rcvAdapter);
        /**
         * 设置线性垂直布局管理器
         */
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        talkRecyclerview.setLayoutManager(manager);
        /**
         * 监听软键盘弹起
         * 将recyclerview划到最后一行
         */
        talkRecyclerview.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                /**
                 * i3  bottom
                 * i7  oldBottom
                 */
                if (i3 < i7) {
                    talkRecyclerview.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            talkRecyclerview.scrollToPosition(dataList.size() - 1);
                        }
                    }, 100);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDataList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

    private void setDataList() {
        dataList.clear();
        talkName.setText(getIntent().getStringExtra("talkName"));
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(getIntent().getStringExtra("to_chat_username"));
        if (conversation != null) {
            //获取此会话的所有消息
            List<EMMessage> message = conversation.getAllMessages();
            //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
            //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
            List<EMMessage> messages = conversation.loadMoreMsgFromDB(message.get(0).getMsgId(), 20);
            for (int i = 0; i < messages.size(); i++) {
                if (messages.get(i).getTo().equals(getIntent().getStringExtra("to_chat_username"))) {
                    TalkEntity send = new TalkEntity(TalkEntity.TALK_SEND);
                    send.setTalkMsg(messages.get(i).getBody().toString().substring(5, messages.get(i).getBody().toString().length() - 1));
                    dataList.add(send);
                } else {
                    TalkEntity receive = new TalkEntity(TalkEntity.TALK_RECEIVED);
                    receive.setTalkMsg(messages.get(i).getBody().toString().substring(5, messages.get(i).getBody().toString().length() - 1));
                    dataList.add(receive);
                }
            }
            for (int i = 0; i < message.size(); i++) {
                if (message.get(i).getTo().equals(getIntent().getStringExtra("to_chat_username"))) {
                    TalkEntity send = new TalkEntity(TalkEntity.TALK_SEND);
                    send.setTalkMsg(message.get(i).getBody().toString().substring(5, message.get(i).getBody().toString().length() - 1));
                    dataList.add(send);
                } else {
                    TalkEntity receive = new TalkEntity(TalkEntity.TALK_RECEIVED);
                    receive.setTalkMsg(message.get(i).getBody().toString().substring(5, message.get(i).getBody().toString().length() - 1));
                    dataList.add(receive);
                }
                talkRecyclerview.scrollToPosition(dataList.size() - 1);
            }
        } else {
            TalkEntity receive = new TalkEntity(TalkEntity.TALK_RECEIVED);
            receive.setTalkMsg("你好，有什么问题吗？");
            dataList.add(receive);
            talkRecyclerview.scrollToPosition(dataList.size() - 1);
        }
    }

    @OnClick({R.id.talk_ok, R.id.talk_back})
    public void talkOnClick(View view) {
        if (view.getId() == R.id.talk_ok) {
            String msg = talkInput.getText().toString();
            if (!msg.equals("")) {
                String toChatUsername = getIntent().getStringExtra("to_chat_username");
                Log.d("send_message", "onSuccess: " + toChatUsername);
                //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
                EMMessage message = EMMessage.createTxtSendMessage(msg, toChatUsername);
                //发送消息
                EMClient.getInstance().chatManager().sendMessage(message);
                TalkEntity talkEntity = new TalkEntity(TalkEntity.TALK_SEND);
                talkEntity.setTalkMsg(msg);
                dataList.add(talkEntity);
                rcvAdapter.notifyItemInserted(dataList.size() - 1);
                talkRecyclerview.scrollToPosition(dataList.size() - 1);
                talkInput.setText("");
            }
        } else if (view.getId() == R.id.talk_back) {
            TalkActivity.this.finish();
        }
    }

}
