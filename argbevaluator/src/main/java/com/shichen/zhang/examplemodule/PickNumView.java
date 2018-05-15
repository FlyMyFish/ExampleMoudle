/*
 * Copyright ©2014-2017 百金贷|京ICP备15008179号|北京荣盛信联信息技术有限公司
 */

package com.shichen.zhang.examplemodule;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.OverScroller;

/**
 * Created by shichen on 2018/4/10.
 *
 * @author shichen 754314442@qq.com
 */

public class PickNumView extends View implements NestedScrollingChild {
    private final OverScroller mOverScroller;
    private int textSize = 12;

    public PickNumView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        mOverScroller = new OverScroller(context);
        //mScroller = new Scroller(context);
        bottomLinePaint = new Paint();
        bottomLinePaint.setAntiAlias(true);
        bottomLinePaint.setColor(0xffF4F4F4);
        bottomLinePaint.setStrokeWidth(3.0f);
        bottomLinePaint.setTextSize(context.getResources().getDisplayMetrics().density * textSize);
        targetPaint = new Paint();
        targetPaint.setAntiAlias(true);
        targetPaint.setColor(Color.RED);
        targetPaint.setStrokeWidth(4.0f);
    }

    private Paint bottomLinePaint;
    /**
     * 最上层叠加画笔
     */
    private float maxLineHeight;
    private float minLineHeight;
    private Paint targetPaint;

    private int minNum = 1000;
    private int maxNum = 200000;
    private int lineMargin = 15;
    private float mSlop = 0.1f;
    private float maxTop = 30.0f;
    private boolean picked = false;

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        canvas.save();
        int targetPosition = 0;
        picked = false;
        bottomLinePaint.setColor(bottomLineColor);
        canvas.drawLine(-1000, maxLineHeight, 200000 / 1000 * lineMargin + screenWidth, maxLineHeight, bottomLinePaint);
        for (int i = 0; i <= 200000; i = i + 1000) {
            if (i % 10000 == 0) {
                bottomLinePaint.setColor(numLineColor);
                canvas.drawLine(i / 1000 * lineMargin, maxLineHeight, i / 1000 * lineMargin, maxTop, bottomLinePaint);
                float numWidth = bottomLinePaint.measureText(String.valueOf(i));
                bottomLinePaint.setColor(textColor);
                canvas.drawText(String.valueOf(i), i / 1000 * lineMargin - numWidth / 2, maxLineHeight + getContext().getResources().getDisplayMetrics().density * textSize, bottomLinePaint);
            } else {
                bottomLinePaint.setColor(numLineColor);
                canvas.drawLine(i / 1000 * lineMargin, maxLineHeight, i / 1000 * lineMargin, maxTop + maxLineHeight - minLineHeight, bottomLinePaint);
            }
            if (getScrollX() + screenWidth / 2 == i / 1000 * lineMargin) {
                if (onNumPickedListener != null) {
                    onNumPickedListener.numPicked(i);
                }
                targetPosition = i;
                picked = true;
            }
        }
        drawShader(targetPosition, canvas);
        targetPaint.setColor(pickLineColor);
        canvas.drawLine(getScrollX() + screenWidth / 2, maxLineHeight, getScrollX() + screenWidth / 2, maxTop, targetPaint);
        canvas.drawCircle(getScrollX() + screenWidth / 2, maxTop, 10.0f, targetPaint);
        canvas.restore();
    }

    private void drawShader(int targetPosition, Canvas canvas) {
        if (!picked) {
            return;
        }
        if (targetPosition - 1000 >= 0) {
            targetPaint.setColor(0xffFD7171);
            if ((targetPosition - 1000) % 10000 == 0) {
                canvas.drawLine((targetPosition - 1000) / 1000 * lineMargin, maxLineHeight, (targetPosition - 1000) / 1000 * lineMargin, maxTop, targetPaint);
            } else {
                canvas.drawLine((targetPosition - 1000) / 1000 * lineMargin, maxLineHeight, (targetPosition - 1000) / 1000 * lineMargin, maxTop + maxLineHeight - minLineHeight, targetPaint);
            }
        }
        if (targetPosition - 2000 >= 0) {
            targetPaint.setColor(0xffF8CFCF);
            if ((targetPosition - 2000) % 10000 == 0) {
                canvas.drawLine((targetPosition - 2000) / 1000 * lineMargin, maxLineHeight, (targetPosition - 2000) / 1000 * lineMargin, maxTop, targetPaint);
            } else {
                canvas.drawLine((targetPosition - 2000) / 1000 * lineMargin, maxLineHeight, (targetPosition - 2000) / 1000 * lineMargin, maxTop + maxLineHeight - minLineHeight, targetPaint);
            }
        }
        if (targetPosition + 1000 <= 200000) {
            targetPaint.setColor(0xffFD7171);
            if ((targetPosition + 1000) % 10000 == 0) {
                canvas.drawLine((targetPosition + 1000) / 1000 * lineMargin, maxLineHeight, (targetPosition + 1000) / 1000 * lineMargin, maxTop, targetPaint);
            } else {
                canvas.drawLine((targetPosition + 1000) / 1000 * lineMargin, maxLineHeight, (targetPosition + 1000) / 1000 * lineMargin, maxTop + maxLineHeight - minLineHeight, targetPaint);
            }
        }
        if (targetPosition + 1000 <= 200000) {
            targetPaint.setColor(0xffF8CFCF);
            if ((targetPosition + 2000) % 10000 == 0) {
                canvas.drawLine((targetPosition + 2000) / 1000 * lineMargin, maxLineHeight, (targetPosition + 2000) / 1000 * lineMargin, maxTop, targetPaint);
            } else {
                canvas.drawLine((targetPosition + 2000) / 1000 * lineMargin, maxLineHeight, (targetPosition + 2000) / 1000 * lineMargin, maxTop + maxLineHeight - minLineHeight, targetPaint);
            }
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mOverScroller.computeScrollOffset()) {
            int currX = mOverScroller.getCurrX();
            int lastX = ((int) currX / lineMargin) * lineMargin;
            int nextX = (((int) currX / lineMargin) + 1) * lineMargin;
            if (currX - lastX >= nextX - currX) {
                scrollTo(nextX, mOverScroller.getCurrY());
            } else {
                scrollTo(lastX, mOverScroller.getCurrY());
            }
            postInvalidate();
        }
    }

    private VelocityTracker mVelocityTracker;
    private static final int MIN_FING_VELOCITY = 4;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }

        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                restoreTouchPoint(event);
                if (!mOverScroller.isFinished()) {
                    mOverScroller.forceFinished(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) (event.getX() - mLastPointX);
                if (getScrollX() <= -screenWidth / 2 - 200 || getScrollX() >= 200000 / 1000 * lineMargin +screenWidth / 2 - screenWidth + 200) {

                } else {
                    if (Math.abs(deltaX) > mSlop) {
                        //取值的正负与手势的方向相反，这在前面的文章已经解释过了
                        scrollBy(-deltaX, 0);
                        restoreTouchPoint(event);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000, 2000.0f);
                int xVelocity = (int) mVelocityTracker.getXVelocity();
                int yVelocity = (int) mVelocityTracker.getYVelocity();
                mOverScroller.fling(getScrollX(), getScrollY(),
                        -xVelocity, -yVelocity, -screenWidth / 2, 200000 / 1000 * lineMargin  +screenWidth / 2 - screenWidth, 0, 0, 200, 0);
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }

    private float mLastPointX;

    private void restoreTouchPoint(MotionEvent event) {
        mLastPointX = event.getX();
    }

    private int screenWidth;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(screenWidth, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        maxLineHeight = getMeasuredHeight() / 4 * 3;
        minLineHeight = maxLineHeight / 2;
    }

    public void setOnNumPickedListener(OnNumPickedListener onNumPickedListener) {
        this.onNumPickedListener = onNumPickedListener;
    }

    private OnNumPickedListener onNumPickedListener;

    public interface OnNumPickedListener {
        void numPicked(int value);
    }

    private int numLineColor = 0xff666666;
    private int bottomLineColor = 0xff666666;
    private int textColor = Color.GRAY;
    private int pickLineColor = Color.RED;

    public int getNumLineColor() {
        return numLineColor;
    }

    public void setNumLineColor(int numLineColor) {
        this.numLineColor = numLineColor;
    }

    public int getBottomLineColor() {
        return bottomLineColor;
    }

    public void setBottomLineColor(int bottomLineColor) {
        this.bottomLineColor = bottomLineColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getPickLineColor() {
        return pickLineColor;
    }

    public void setPickLineColor(int pickLineColor) {
        this.pickLineColor = pickLineColor;
    }

    public void setAmount(int value) {
        int dx = value / 1000 * lineMargin-mOverScroller.getCurrX()-screenWidth/2;
        mOverScroller.startScroll(mOverScroller.getCurrX(), 0, dx, 0);
    }
}
