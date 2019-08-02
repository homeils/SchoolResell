package com.renoside.schoolresell;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.renoside.schoolresell.Fragment.FragmentOrder;
import com.renoside.schoolresell.Fragment.FragmentMsg;
import com.renoside.schoolresell.Fragment.FragmentPIM;
import com.renoside.schoolresell.Fragment.FragmentShop;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.home_fragment_container)
    ViewPager homeFragmentContainer;
    @BindView(R.id.home_tab_container)
    LinearLayout homeTabContainer;
    @BindView(R.id.home_tab_title)
    TextView homeTabTitle;
    @BindView(R.id.home_tab_points)
    LinearLayout homeTabPoints;

    /**
     * 定义Tab标题集合
     */
    private List<String> homeTabTitles;
    /**
     * 定义Fragment集合
     */
    private List<FragmentCreator> fragmentCreatorList;

    /**
     * 是否滑动完毕
     */
    private boolean isDragging = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        /**
         * 创建并向集合添加数据，绑定适配器等
         */
        start();
        /**
         * 2秒之后隐藏Tab
         */
        hideTab();
    }

    private void start() {
        /**
         * 添加Fragment集合数据
         */
        fragmentCreatorList = new ArrayList<>();
        fragmentCreatorList.add(createFragmentCreator(new FragmentShop()));
        fragmentCreatorList.add(createFragmentCreator(new FragmentMsg()));
        fragmentCreatorList.add(createFragmentCreator(new FragmentOrder()));
        fragmentCreatorList.add(createFragmentCreator(new FragmentPIM()));
        /**
         * 添加Tab集合数据
         */
        homeTabTitles = new ArrayList<>();
        homeTabTitles.add("首页");
        homeTabTitles.add("消息");
        homeTabTitles.add("订单");
        homeTabTitles.add("我的");
        /**
         * 实例化适配器
         */
        VpgFragmentAdapter vpgFragmentAdapter = new VpgFragmentAdapter(getSupportFragmentManager(), this);
        /**
         * 绑定适配器
         */
        homeFragmentContainer.setAdapter(vpgFragmentAdapter);
        /**
         * 添加内部类监听
         */
        homeFragmentContainer.addOnPageChangeListener(new PageChangeListener());
        /**
         * 初始设置第一个Tab标题
         */
        if (homeTabTitles != null && homeTabTitles.size() != 0)
            homeTabTitle.setText(homeTabTitles.get(0));
    }

    /**
     * 创建Fragment并返回以配合适配器完成页面创建
     *
     * @param fragment
     * @return
     */
    private FragmentCreator createFragmentCreator(final Fragment fragment) {
        FragmentCreator fragmentCreator = () -> fragment;
        return fragmentCreator;
    }

    /**
     * 2秒之后隐藏Tab方法体
     */
    private void hideTab() {
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                runOnUiThread(() -> {
                    homeTabContainer.setVisibility(View.GONE);
                    homeTabTitle.setVisibility(View.GONE);
                    homeTabPoints.setVisibility(View.GONE);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * ViewPager适配器内部类
     */
    class VpgFragmentAdapter extends FragmentPagerAdapter {

        /**
         * 适配器构造及初始化
         *
         * @param fm
         */
        public VpgFragmentAdapter(FragmentManager fm, Context context) {
            super(fm);
            /**
             * 根据页面数初始化导航点
             */
            for (int i = 0; i < fragmentCreatorList.size(); i++) {
                ImageView point = new ImageView(context);
                point.setBackgroundResource(R.drawable.tab_point_selector);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(25, 25);
                if (i == 0) {
                    point.setEnabled(true);
                } else {
                    point.setEnabled(false);
                    params.leftMargin = 8;
                }
                point.setLayoutParams(params);
                homeTabPoints.addView(point);
            }
        }

        /**
         * 加载页面
         *
         * @param position
         * @return
         */
        @Override
        public Fragment getItem(int position) {
            FragmentCreator fragmentCreator = fragmentCreatorList.get(position);
            return fragmentCreator.createFragment();
        }

        /**
         * 返回页面数量
         *
         * @return
         */
        @Override
        public int getCount() {
            return fragmentCreatorList.size();
        }

    }

    /**
     * 监听内部类（内部类避免的传参的麻烦）
     */
    class PageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * 记录上一个页面的位置
         */
        private int prePosition = 0;

        /**
         * 页面滚动时回调这个方法
         *
         * @param position             当前页面位置
         * @param positionOffset       滑动页面百分比
         * @param positionOffsetPixels 滑动像素
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 页面被选中时回调这个方法
         *
         * @param position 被选中位置
         */
        @Override
        public void onPageSelected(int position) {
            /**
             * 设置Tab标题
             */
            if (homeTabTitles != null && homeTabTitles.size() != 0) {
                homeTabTitle.setText(homeTabTitles.get(position));
            }
            /**
             * 设置Tab导航点
             */
            if (fragmentCreatorList != null && fragmentCreatorList.size() != 0) {
                homeTabPoints.getChildAt(prePosition).setEnabled(false);
                homeTabPoints.getChildAt(position).setEnabled(true);
                prePosition = position;
            }
        }

        /**
         * 页面滚动状态变化时回调这个方法
         * 静止 -》 滑动
         * 滑动 -》 静止
         * 静止 -》 拖拽
         *
         * @param state
         */
        @Override
        public void onPageScrollStateChanged(int state) {
            /**
             * 拖拽、滑动、静止
             */
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                /**
                 * 拖拽时显示Tab
                 */
                runOnUiThread(() -> {
                    homeTabContainer.setVisibility(View.VISIBLE);
                    homeTabTitle.setVisibility(View.VISIBLE);
                    homeTabPoints.setVisibility(View.VISIBLE);
                });
                isDragging = true;
            } else if (state == ViewPager.SCROLL_STATE_SETTLING) {
                /**
                 * 滑动时显示Tab
                 */
                runOnUiThread(() -> {
                    homeTabContainer.setVisibility(View.VISIBLE);
                    homeTabTitle.setVisibility(View.VISIBLE);
                    homeTabPoints.setVisibility(View.VISIBLE);
                });
                isDragging = true;
            } else if (state == ViewPager.SCROLL_STATE_IDLE && isDragging) {
                /**
                 * 静止2秒之后隐藏Tab
                 */
                hideTab();
            }
        }

    }

    /**
     * 创建Fragment的内部接口
     */
    interface FragmentCreator {

        Fragment createFragment();

    }

}
