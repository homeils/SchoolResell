package com.renoside.schoolresell;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.renoside.schoolresell.Adapter.TalkRcvAdapter;
import com.renoside.schoolresell.Entity.TalkEntity;

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
    private List<TalkEntity> dataList;
    private TalkRcvAdapter rcvAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);
        ButterKnife.bind(this);
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

    private void setDataList() {
        dataList = new ArrayList<>();
        TalkEntity receive = new TalkEntity(TalkEntity.TALK_RECEIVED);
        receive.setTalkMsg("Hello, nice to meet you!");
        dataList.add(receive);
        TalkEntity send = new TalkEntity(TalkEntity.TALK_SEND);
        send.setTalkMsg("Hello!");
        dataList.add(send);
    }

    @OnClick(R.id.talk_ok)
    public void talkOnClick(View view) {
        if (view.getId() == R.id.talk_ok) {
            Toast.makeText(this, "Send successfully " + talkInput.getText(), Toast.LENGTH_SHORT).show();
            String msg = talkInput.getText().toString();
            if (!msg.equals("")) {
                TalkEntity talkEntity = new TalkEntity(TalkEntity.TALK_SEND);
                talkEntity.setTalkMsg(msg);
                dataList.add(talkEntity);
                rcvAdapter.notifyItemInserted(dataList.size() - 1);
                talkRecyclerview.scrollToPosition(dataList.size() - 1);
                talkInput.setText("");
            }
        }
    }

}
