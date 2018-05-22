package com.shichen.zhang.examplemodule.banner;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 轮播图
 *
 * @author shichen 754314442@qq.com
 * Created by ZSC on 2017/7/21.
 */

public class RecyclerBanner extends FrameLayout {

    private Context context;
    RecyclerView recyclerView;
    LinearLayout linearLayout;

    int startX, startY, currentIndex;
    boolean isPlaying;
    private int dataSize = 0;

    private Handler handler = new Handler();

    private Runnable playTask = new Runnable() {

        @Override
        public void run() {
            recyclerView.smoothScrollToPosition(++currentIndex);
            changePoint();
            handler.postDelayed(this, mConfig.interval);
        }
    };

    public RecyclerBanner(Context context) {
        this(context, null);
    }

    public RecyclerBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        recyclerView = new RecyclerView(context);
        LayoutParams vpLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(recyclerView, vpLayoutParams);
        new PagerSnapHelper().attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        final int topAndBottomSize = 2;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int first = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                int last = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                if (currentIndex != (first + last) / topAndBottomSize) {
                    currentIndex = (first + last) / topAndBottomSize;
                    changePoint();
                }
            }
        });
    }

    public synchronized void setPlaying(boolean playing) {
        final int minSize = 2;
        if (!isPlaying && playing && adapter != null && adapter.getItemCount() > minSize) {
            handler.postDelayed(playTask, mConfig.interval);
            isPlaying = true;
        } else if (isPlaying && !playing) {
            handler.removeCallbacksAndMessages(null);
            isPlaying = false;
        }
    }

    private void dataChanged() {
        if (adapter instanceof IBannerAdapter) {
            dataSize = ((IBannerAdapter) adapter).realDataSize();
        }
        if (dataSize > 1) {
            currentIndex = dataSize * 10000;
            adapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(currentIndex);
            if (tagVisibility != View.GONE) {
                for (int i = 0; i < dataSize; i++) {
                    ImageView img = new ImageView(getContext());
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.leftMargin = size / 2;
                    lp.rightMargin = size / 2;
                    lp.gravity = Gravity.CENTER_VERTICAL;
                    img.setImageDrawable(i == 0 ? selectedDrawable : defaultDrawable);
                    linearLayout.addView(img, lp);
                }
            }
            setPlaying(true);
        } else {
            currentIndex = 0;
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                setPlaying(false);
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();
                int disX = moveX - startX;
                int disY = moveY - startY;
                getParent().requestDisallowInterceptTouchEvent(2 * Math.abs(disX) > Math.abs(disY));
                setPlaying(false);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setPlaying(true);
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setPlaying(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setPlaying(false);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        if (visibility == View.GONE) {
            // 停止轮播
            setPlaying(false);
        } else if (visibility == View.VISIBLE) {
            // 开始轮播
            setPlaying(true);
        }
        super.onWindowVisibilityChanged(visibility);
    }

    private class PagerSnapHelper extends LinearSnapHelper {

        @Override
        public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
            int targetPos = super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
            final View currentView = findSnapView(layoutManager);
            if (targetPos != RecyclerView.NO_POSITION && currentView != null) {
                int currentPostion = layoutManager.getPosition(currentView);
                int first = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                int last = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                currentPostion = targetPos < currentPostion ? last : (targetPos > currentPostion ? first : currentPostion);
                targetPos = targetPos < currentPostion ? currentPostion - 1 : (targetPos > currentPostion ? currentPostion + 1 : currentPostion);
            }
            return targetPos;
        }
    }

    private void changePoint() {
        if (linearLayout != null && linearLayout.getChildCount() > 0) {
            if (tagVisibility != View.GONE) {
                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    ((ImageView) linearLayout.getChildAt(i)).setImageDrawable(i == currentIndex % dataSize ? selectedDrawable : defaultDrawable);
                }
            }
        }
    }

    BannerConfig mConfig;
    private Drawable defaultDrawable, selectedDrawable;
    private int tagVisibility;
    private RecyclerView.Adapter adapter;
    int size;

    public void initBanner(BannerConfig config) {
        mConfig = config;
        tagVisibility = mConfig.tagVisibility;
        defaultDrawable = mConfig.defaultDrawable;
        selectedDrawable = mConfig.selectedDrawable;
        if (defaultDrawable == null || selectedDrawable == null) {
            tagVisibility = View.GONE;
        }

        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams linearLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setGravity(mConfig.tagGravity);
        size = (int) context.getResources().getDisplayMetrics().density * mConfig.tagParentPadding;
        linearLayout.setPadding(size * 2, size * 2, size * 2, size * 2);
        linearLayout.setVisibility(tagVisibility);
        linearLayoutParams.gravity = mConfig.tagParentGravity;
        if (mConfig.adapter == null) {
            throw new NullPointerException("Adapter can't be null,please init in BannerConfig.builder.");
        }
        adapter = mConfig.adapter;
        recyclerView.setAdapter(this.adapter);
        dataChanged();
        this.adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                dataChanged();
            }
        });
        addView(linearLayout, linearLayoutParams);
    }
}