package com.shichen.zhang.examplemodule;

import android.animation.ArgbEvaluator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZSC
 *         Created by ZSC on 2017/3/29.
 */

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private int PAGE_COLOR_ONE;
    private int PAGE_COLOR_TWO;
    private int PAGE_COLOR_THREE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initColorDate();
        initToolbar();
        initPage();
    }

    private void initColorDate() {
        PAGE_COLOR_ONE = ContextCompat.getColor(this, R.color.colorPrimary);
        PAGE_COLOR_TWO = ContextCompat.getColor(this, R.color.colorAccent);
        PAGE_COLOR_THREE = ContextCompat.getColor(this, R.color.colorPrimaryDark);
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setTitle(getString(R.string.main_title));
        setSupportActionBar(toolbar);
    }

    /**
     * 初始化viewPager
     */
    private void initPage() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_content);
        List<TextView> itemList = new ArrayList<>();
        TextView tvPageOne = new TextView(this);
        tvPageOne.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tvPageOne.setGravity(Gravity.CENTER);
        tvPageOne.setText("第一页");
        tvPageOne.setBackgroundColor(PAGE_COLOR_ONE);
        itemList.add(tvPageOne);
        TextView tvPageTwo = new TextView(this);
        tvPageTwo.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tvPageTwo.setGravity(Gravity.CENTER);
        tvPageTwo.setText("第二页");
        tvPageTwo.setBackgroundColor(PAGE_COLOR_TWO);
        itemList.add(tvPageTwo);
        TextView tvPageThree = new TextView(this);
        tvPageThree.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tvPageThree.setGravity(Gravity.CENTER);
        tvPageThree.setText("第三页");
        tvPageThree.setBackgroundColor(PAGE_COLOR_THREE);
        itemList.add(tvPageThree);
        viewPager.setAdapter(new ContentPagerAdapter(itemList));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //viewPager滑动过程中会回调该接口
                changeTitleColor(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class ContentPagerAdapter extends PagerAdapter {
        List<TextView> itemList;

        public ContentPagerAdapter(List<TextView> itemList) {
            this.itemList = itemList;
        }

        @Override
        public int getCount() {
            if (itemList == null) {
                return 0;
            }
            return itemList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(itemList.get(position));
            return itemList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(itemList.get(position));
        }
    }

    /**
     * 修改整体颜色
     *
     * @param position
     * @param positionOffset
     */
    private void changeTitleColor(int position, float positionOffset) {
        switch (position) {
            case 0: {
                ArgbEvaluator argbEvaluator = new ArgbEvaluator();//渐变色计算类
                int currentLastColor = (int) (argbEvaluator.evaluate(positionOffset, PAGE_COLOR_ONE, PAGE_COLOR_TWO));
                //positionOffset:表示渐变度，取0.0F-1.0F之间某一值
                //PAGE_COLOR_ONE:表示起始颜色值
                //PAGE_COLOR_TWO:表示最终颜色值
                setToolbarColor(currentLastColor);
                setStatusBarColor(currentLastColor);
            }
            break;
            case 1: {
                ArgbEvaluator argbEvaluator = new ArgbEvaluator();
                int currentLastColor = (int) (argbEvaluator.evaluate(positionOffset, PAGE_COLOR_TWO, PAGE_COLOR_THREE));
                setToolbarColor(currentLastColor);
                setStatusBarColor(currentLastColor);
            }
            break;
            case 2: {
                setStatusBarColor(PAGE_COLOR_THREE);
            }
            break;
        }
    }

    /**
     * 给状态栏设置颜色
     * 安卓系统4.4以上可用
     * 不向下兼容
     *
     * @param color
     */
    private void setStatusBarColor(int color) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(color);
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setToolbarColor(int color) {
        if (toolbar != null) {
            toolbar.setBackgroundColor(color);
        }
    }
}
