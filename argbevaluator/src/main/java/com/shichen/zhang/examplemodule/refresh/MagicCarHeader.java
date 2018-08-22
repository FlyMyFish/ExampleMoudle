package com.shichen.zhang.examplemodule.refresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.util.DensityUtil;

/**
 * @author shichen 754314442@qq.com
 * Created by shichen on 2018/8/22.
 */
public class MagicCarHeader extends RelativeLayout implements RefreshHeader {
    private final String TAG="MagicCarHeader";
    private ImageView ivProgress;
    private CarProgressDrawable progressDrawable;
    public MagicCarHeader(Context context) {
        this(context,null);
    }

    public MagicCarHeader(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MagicCarHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        DensityUtil density = new DensityUtil();
        ivProgress=new ImageView(context);
        progressDrawable=new CarProgressDrawable(ivProgress);
        ivProgress.setImageDrawable(progressDrawable);
        LayoutParams lpIvProgress=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,density.dip2px(160));
        addView(ivProgress,lpIvProgress);
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void onPulling(float percent, int offset, int height, int extendHeight) {
        //Log.e(TAG,"onPulling: percent="+percent);
        progressDrawable.setPullPercent(percent);
    }

    @Override
    public void onReleasing(float percent, int offset, int height, int extendHeight) {
        Log.e(TAG,"onReleasing: percent="+percent);
        progressDrawable.setPullPercent(percent);
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        return 0;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    private RefreshState nowState;

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        nowState=oldState;
        switch (newState) {
            case None:
            case PullDownToRefresh:
                Log.e(TAG,"PullDownToRefresh");
                break;
            case Refreshing:
                Log.e(TAG,"Refreshing");
            case RefreshReleased:
                Log.e(TAG,"RefreshReleased");
                break;
            case ReleaseToRefresh:
                Log.e(TAG,"ReleaseToRefresh");
                break;
            case ReleaseToTwoLevel:
                Log.e(TAG,"ReleaseToTwoLevel");
                break;
            case Loading:
                Log.e(TAG,"Loading");
                break;
        }
    }
}
