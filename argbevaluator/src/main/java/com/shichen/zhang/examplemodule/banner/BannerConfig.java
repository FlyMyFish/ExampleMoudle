package com.shichen.zhang.examplemodule.banner;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;

/**
 * Created by shichen on 2018/5/22.
 *
 * @author shichen 754314442@qq.com
 */
public class BannerConfig {
    /**
     * defaultDrawable未选中样式
     * selectedDrawable选中样式
     */
    Drawable defaultDrawable, selectedDrawable;
    /**
     * 轮播图的样式
     */
    RecyclerView.Adapter adapter;
    /**
     * 指示器的位置
     */
    int tagGravity = Gravity.CENTER_HORIZONTAL;

    /**
     * 指示器在整个banner中的位置
     */
    int tagParentGravity = Gravity.BOTTOM;

    /**
     * 指示器整体的padding
     */
    int tagParentPadding = 6;

    /**
     * 指示器的可见性
     */
    int tagVisibility = View.VISIBLE;

    /**
     * 轮播间隔，单位毫秒
     */
    int interval = 3000;

    private BannerConfig() {

    }

    public static class Builder {
        /**
         * defaultDrawable未选中样式
         * selectedDrawable选中样式
         */
        Drawable defaultDrawable, selectedDrawable;
        /**
         * 轮播图的样式
         */
        RecyclerView.Adapter adapter;
        /**
         * 指示器的位置
         */
        int tagGravity = Gravity.CENTER_HORIZONTAL;

        /**
         * 指示器在整个banner中的位置
         */
        int tagParentGravity = Gravity.BOTTOM;

        /**
         * 指示器整体的padding
         */
        int tagParentPadding = 6;

        /**
         * 指示器的可见性
         */
        int tagVisibility = View.VISIBLE;

        /**
         * 轮播间隔，单位毫秒
         */
        int interval = 3000;

        /**
         * 自定义未选中指示器
         *
         * @param defaultDrawable 未选中指示器
         * @return builder
         */
        public Builder setDefaultDrawable(Drawable defaultDrawable) {
            this.defaultDrawable = defaultDrawable;
            return this;
        }

        /**
         * 自定义选中的指示器
         *
         * @param selectedDrawable 选中的指示器
         * @return builder
         */
        public Builder setSelectedDrawable(Drawable selectedDrawable) {
            this.selectedDrawable = selectedDrawable;
            return this;
        }

        /**
         * 加载默认的圆形未选中指示器
         *
         * @param context context
         * @return builder
         */
        public Builder setDefaultDrawable(Context context) {
            int size = (int) (6 * context.getResources().getDisplayMetrics().density + 0.5f);
            GradientDrawable defDefaultDrawable = new GradientDrawable();
            defDefaultDrawable.setSize(size, size);
            defDefaultDrawable.setCornerRadius(size);
            defDefaultDrawable.setColor(0x88ffffff);
            this.defaultDrawable = defDefaultDrawable;
            return this;
        }

        /**
         * 加载默认的圆形选中的指示器
         *
         * @param context context
         * @return builder
         */
        public Builder setSelectedDrawable(Context context) {
            int size = (int) (6 * context.getResources().getDisplayMetrics().density + 0.5f);
            GradientDrawable defSelectedDrawable = new GradientDrawable();
            defSelectedDrawable.setSize(size, size);
            defSelectedDrawable.setCornerRadius(size);
            defSelectedDrawable.setColor(0xffffffff);
            this.selectedDrawable = defSelectedDrawable;
            return this;
        }


        /**
         * 定制圆形未选中指示器
         *
         * @param context
         * @param pointSize    大小
         * @param defaultColor 未选中颜色
         * @return builder
         */
        public Builder setDefaultDrawable(Context context, int pointSize, int defaultColor) {
            int size = (int) (pointSize * context.getResources().getDisplayMetrics().density + 0.5f);
            GradientDrawable defDefaultDrawable = new GradientDrawable();
            defDefaultDrawable.setSize(size, size);
            defDefaultDrawable.setCornerRadius(size);
            defDefaultDrawable.setColor(defaultColor);
            this.defaultDrawable = defDefaultDrawable;
            return this;
        }

        /**
         * 定制圆形选中指示器
         *
         * @param context
         * @param pointSize     大小
         * @param selectedColor 选中的颜色
         * @return builder
         */
        public Builder setSelectedDrawable(Context context, int pointSize, int selectedColor) {
            int size = (int) (pointSize * context.getResources().getDisplayMetrics().density + 0.5f);
            GradientDrawable defSelectedDrawable = new GradientDrawable();
            defSelectedDrawable.setSize(size, size);
            defSelectedDrawable.setCornerRadius(size);
            defSelectedDrawable.setColor(selectedColor);
            this.selectedDrawable = defSelectedDrawable;
            return this;
        }

        public Builder setAdapter(RecyclerView.Adapter adapter) {
            this.adapter = adapter;
            return this;
        }

        public Builder setTagGravity(int tagGravity) {
            this.tagGravity = tagGravity;
            return this;
        }

        public Builder setTagParentGravity(int tagParentGravity) {
            this.tagParentGravity = tagParentGravity;
            return this;
        }

        public Builder setTagParentPadding(int tagParentPadding) {
            this.tagParentPadding = tagParentPadding;
            return this;
        }

        public Builder setTagVisibility(int tagVisibility) {
            this.tagVisibility = tagVisibility;
            return this;
        }

        public Builder setInterval(int interval) {
            this.interval = interval;
            return this;
        }

        void applyConfig(BannerConfig bannerConfig) {
            bannerConfig.adapter = this.adapter;
            bannerConfig.defaultDrawable = this.defaultDrawable;
            bannerConfig.selectedDrawable = this.selectedDrawable;
            bannerConfig.tagGravity = this.tagGravity;
            bannerConfig.tagParentGravity = this.tagParentGravity;
            bannerConfig.tagParentPadding = this.tagParentPadding;
            bannerConfig.tagVisibility = this.tagVisibility;
            bannerConfig.interval=this.interval;
        }

        /**
         * 根据已经设置好的属性创建配置对象
         *
         * @return BannerConfig 配置对象
         */
        public BannerConfig create() {
            BannerConfig bannerConfig = new BannerConfig();
            applyConfig(bannerConfig);
            return bannerConfig;
        }
    }
}
