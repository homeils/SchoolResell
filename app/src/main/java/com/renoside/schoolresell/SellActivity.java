package com.renoside.schoolresell;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.renoside.schoolresell.Adapter.SellRcvAdapter;
import com.renoside.schoolresell.Bean.GoodsInfo;
import com.renoside.schoolresell.Entity.SellEntity;
import com.renoside.schoolresell.Utils.ApiUrl;
import com.renoside.schoolresell.Utils.SharedPreferencesUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.sell_title)
    TextView sellTitle;

    private int flag;
    private List<SellEntity> dataList;
    private SellRcvAdapter rcvAdapter;
    private List<Uri> imgUris;
    private StringBuffer goodsImgs = new StringBuffer();
    final RxPermissions rxPermissions = new RxPermissions(this);

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
        isUpdate();
        initListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
            imgUris = Matisse.obtainResult(data);
        }
    }

    @OnClick(R.id.sell_back)
    public void sellOnClick(View view) {
        if (view.getId() == R.id.sell_back) {
            SellActivity.this.finish();
        }
    }

    private void setDataList() {
        dataList = new ArrayList<>();
        SellEntity name = new SellEntity(SellEntity.SELL_SMALL_ITEM);
        name.setSellHint("商品名称*：");
        dataList.add(name);
        SellEntity description = new SellEntity(SellEntity.SELL_BIG_ITEM);
        description.setSellHint("商品描述 ：");
        dataList.add(description);
        SellEntity price = new SellEntity(SellEntity.SELL_SMALL_ITEM);
        price.setSellHint("商品价格*：");
        dataList.add(price);
        SellEntity phone = new SellEntity(SellEntity.SELL_SMALL_ITEM);
        phone.setSellHint("联系电话*：");
        dataList.add(phone);
        SellEntity address = new SellEntity(SellEntity.SELL_BIG_ITEM);
        address.setSellHint("卖家地址*：");
        dataList.add(address);
        SellEntity pic = new SellEntity(SellEntity.SELL_PIC_ITEM);
        dataList.add(pic);
    }

    private void isUpdate() {
        if (getIntent().getStringExtra("goodsId") != null) {
            OkGo.<String>get(ApiUrl.url + "/goods/" + getIntent().getStringExtra("goodsId"))
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            JSONObject jsonObject = JSON.parseObject(response.body());
                            GoodsInfo goodsInfo = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<GoodsInfo>() {
                            });
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((EditText) rcvAdapter.getViewByPosition(sellRecyclerview, 0, R.id.sell_small_content)).setText(goodsInfo.getGoodsName());
                                    ((EditText) rcvAdapter.getViewByPosition(sellRecyclerview, 1, R.id.sell_big_content)).setText(goodsInfo.getGoodsDescription());
                                    ((EditText) rcvAdapter.getViewByPosition(sellRecyclerview, 2, R.id.sell_small_content)).setText(String.valueOf(goodsInfo.getGoodsPrice()));
                                    ((EditText) rcvAdapter.getViewByPosition(sellRecyclerview, 3, R.id.sell_small_content)).setText(goodsInfo.getGoodsPhone());
                                    ((EditText) rcvAdapter.getViewByPosition(sellRecyclerview, 4, R.id.sell_big_content)).setText(goodsInfo.getGoodsAddress());
                                }
                            });
                        }
                    });
        }
    }

    private void initListener() {
        /**
         * 添加图片按钮
         */
        rcvAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 5) {
                    rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(granted -> {
                                if (granted) {
                                    Matisse.from(SellActivity.this)
                                            .choose(MimeType.ofImage(), false) // 选择支持的文件类型
                                            .showSingleMediaType(true) //只展示支持的文件
                                            .theme(R.style.Matisse_Dracula) //黑色主题
                                            .countable(true) //是否有序
                                            .maxSelectable(4) // 图片选择的最多数量
                                            .spanCount(3) //一行三个
                                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) //选择方向
                                            .thumbnailScale(0.85f) // 缩略图的质量
                                            .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                                            .forResult(200); // 设置作为标记的请求码
                                } else {
                                    Toast toast = Toast.makeText(SellActivity.this, null, Toast.LENGTH_SHORT);
                                    toast.setText("你拒绝了权限请求，请前往设置开启权限才可以使用此功能");
                                    toast.show();
                                }
                            });
                }
            }
        });
        /**
         * 发布按钮事件监听处理
         */
        sellOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * 上架商品
                 */
                Handler upHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        Bundle bundle = msg.getData();
                        switch (msg.what) {
                            case 200:
                                OkGo.<String>post(ApiUrl.url + "/goods/" + SharedPreferencesUtils.getUserLoginInfo(SellActivity.this).get("userId"))
                                        .headers("token", SharedPreferencesUtils.getUserLoginInfo(SellActivity.this).get("token"))
                                        .params("goodsImgs", bundle.getString("goodsImgs"))
                                        .params("goodsName", String.valueOf(((EditText) rcvAdapter.getViewByPosition(sellRecyclerview, 0, R.id.sell_small_content)).getText()))
                                        .params("goodsDescription", String.valueOf(((EditText) rcvAdapter.getViewByPosition(sellRecyclerview, 1, R.id.sell_big_content)).getText()))
                                        .params("goodsPrice", String.valueOf(((EditText) rcvAdapter.getViewByPosition(sellRecyclerview, 2, R.id.sell_small_content)).getText()))
                                        .params("goodsPhone", String.valueOf(((EditText) rcvAdapter.getViewByPosition(sellRecyclerview, 3, R.id.sell_small_content)).getText()))
                                        .params("goodsAddress", String.valueOf(((EditText) rcvAdapter.getViewByPosition(sellRecyclerview, 4, R.id.sell_big_content)).getText()))
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onSuccess(Response<String> response) {
                                                Toast toast = Toast.makeText(SellActivity.this, null, Toast.LENGTH_SHORT);
                                                toast.setText("商品上架成功");
                                                toast.show();
                                                Intent intent = new Intent(SellActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                        }
                    }
                };
                /**
                 * 展示上传进度条
                 */
                ProgressDialog pd2 = new ProgressDialog(SellActivity.this);
                pd2.setTitle("上架商品");
                pd2.setIcon(R.mipmap.app_icon);
                pd2.setMessage("正在同步信息...");
                pd2.setCancelable(false);
                pd2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd2.show();
                /**
                 * 新开线程得到,分割的储存好的图片路径
                 */
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        flag = 0;
                        for (int i = 0; i < imgUris.size(); i++) {
                            /**
                             * uri转file
                             */
                            Uri uri = imgUris.get(i);
                            String[] proj = {MediaStore.Images.Media.DATA};
                            Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
                            int actual_image_column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                            cursor.moveToFirst();
                            String img_path = cursor.getString(actual_image_column_index);
                            File file = new File(img_path);
                            /**
                             * 请求储存图片至smms图床
                             */
                            OkGo.<String>post("https://sm.ms/api/v2/upload")
                                    .isMultipart(true)
                                    .headers("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0")
                                    .params("smfile", file)
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(Response<String> response) {
                                            JSONObject jsonObject = JSONObject.parseObject(response.body());
                                            String url = jsonObject.getJSONObject("data").getString("url");
                                            goodsImgs.append(url + ",");
                                            flag++;
                                            if (flag == (imgUris.size())) {
                                                goodsImgs.deleteCharAt(goodsImgs.length() - 1);
                                                Message msg = new Message();
                                                msg.what = 200;
                                                Bundle bundle = new Bundle();
                                                bundle.putString("goodsImgs", String.valueOf(goodsImgs));
                                                msg.setData(bundle);
                                                upHandler.sendMessage(msg);
                                            }
                                        }
                                    });
                            cursor.close();
                        }
                    }
                }).start();
            }
        });
        /**
         * 修改按钮事件监听处理
         */
        if (getIntent().getStringExtra("goodsId") != null) {
            sellTitle.setText("修改商品");
            sellOk.setText("修改");
            sellOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /**
                     * 修改商品信息
                     */
                    Handler updateHandler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            Bundle bundle = msg.getData();
                            switch (msg.what) {
                                case 200:
                                    OkGo.<String>put(ApiUrl.url + "/goods/" + getIntent().getStringExtra("goodsId"))
                                            .headers("token", SharedPreferencesUtils.getUserLoginInfo(SellActivity.this).get("token"))
                                            .params("goodsImgs", bundle.getString("goodsImgs"))
                                            .params("goodsName", String.valueOf(((EditText) rcvAdapter.getViewByPosition(sellRecyclerview, 0, R.id.sell_small_content)).getText()))
                                            .params("goodsDescription", String.valueOf(((EditText) rcvAdapter.getViewByPosition(sellRecyclerview, 1, R.id.sell_big_content)).getText()))
                                            .params("goodsPrice", String.valueOf(((EditText) rcvAdapter.getViewByPosition(sellRecyclerview, 2, R.id.sell_small_content)).getText()))
                                            .params("goodsPhone", String.valueOf(((EditText) rcvAdapter.getViewByPosition(sellRecyclerview, 3, R.id.sell_small_content)).getText()))
                                            .params("goodsAddress", String.valueOf(((EditText) rcvAdapter.getViewByPosition(sellRecyclerview, 4, R.id.sell_big_content)).getText()))
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(Response<String> response) {
                                                    Toast toast = Toast.makeText(SellActivity.this, null, Toast.LENGTH_SHORT);
                                                    toast.setText("商品修改成功");
                                                    toast.show();
                                                    Intent intent = new Intent(SellActivity.this, HomeActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                            }
                        }
                    };
                    /**
                     * 展示上传进度条
                     */
                    ProgressDialog pd2 = new ProgressDialog(SellActivity.this);
                    pd2.setTitle("修改商品");
                    pd2.setIcon(R.mipmap.ic_launcher_round);
                    pd2.setMessage("正在同步信息...");
                    pd2.setCancelable(false);
                    pd2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pd2.show();
                    /**
                     * 新开线程得到,分割的储存好的图片路径
                     */
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            flag = 0;
                            for (int i = 0; i < imgUris.size(); i++) {
                                /**
                                 * uri转file
                                 */
                                Uri uri = imgUris.get(i);
                                String[] proj = {MediaStore.Images.Media.DATA};
                                Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
                                int actual_image_column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                                cursor.moveToFirst();
                                String img_path = cursor.getString(actual_image_column_index);
                                File file = new File(img_path);
                                /**
                                 * 请求储存图片至smms图床
                                 */
                                OkGo.<String>post("https://sm.ms/api/v2/upload")
                                        .isMultipart(true)
                                        .headers("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0")
                                        .params("smfile", file)
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onSuccess(Response<String> response) {
                                                JSONObject jsonObject = JSONObject.parseObject(response.body());
                                                String url = jsonObject.getJSONObject("data").getString("url");
                                                goodsImgs.append(url + ",");
                                                flag++;
                                                if (flag == (imgUris.size())) {
                                                    goodsImgs.deleteCharAt(goodsImgs.length() - 1);
                                                    Message msg = new Message();
                                                    msg.what = 200;
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("goodsImgs", String.valueOf(goodsImgs));
                                                    msg.setData(bundle);
                                                    updateHandler.sendMessage(msg);
                                                }
                                            }
                                        });
                                cursor.close();
                            }
                        }
                    }).start();
                }
            });
        }
    }
}
