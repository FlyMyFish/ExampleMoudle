package com.shichen.zhang.examplemodule.refresh;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.shichen.zhang.examplemodule.R;

/**
 * @author shichen 754314442@qq.com
 * drawBitmap(Bitmap bitmap, Rect src, Rect dst, Paint paint)
 * 根据指定区域绘制bitmap
 * bitmap：需要绘制的bitmap
 * src：bitmap需要绘制的区域，若src的面积小于bitmap时会对bitmap进行裁剪，一般来说需要绘制整个bitmap时可以为null
 * dst：在画布中指定绘制bitmap的区域，当这个区域的面积与bitmap要显示的面积不匹配时，会进行拉伸，不可为null
 * paint：画笔，可为null
 * Created by shichen on 2018/8/22.
 */
public class CarProgressDrawable extends Drawable implements Animatable {
    private ImageView parent;
    //汽车车身
    private Bitmap bpCarBody;
    //汽车左边轮子，右边轮子
    private Bitmap bpCarWheelLeft, bpCarWheelRight;
    //路边小树，路边大树
    private Bitmap smallTree, bigTree;
    //尾气1，尾气2，尾气3
    private Bitmap gas1, gas2, gas3;
    private float density;

    private Paint roadPaint;//路的画笔

    private int roadColor = 0xffbfbfbf;

    /**
     * 下来的百分比
     */
    private float pullPercent;

    private ValueAnimator mAnimator;

    /**
     * 车身与轮子的距离
     */
    private float offsetCarBodyToWheel = 0.0f;

    /**
     * 大树运动的时间
     */
    private float timeLongBTree=0.0f;
    /**
     * 小树运动的时间
     */
    private float timeLongSTree=0.0f;

    public CarProgressDrawable(ImageView parent) {
        this.parent = parent;
        density = parent.getResources().getDisplayMetrics().density;
        bpCarBody = BitmapFactory.decodeResource(parent.getResources(), R.drawable.src_car_body);
        bpCarWheelLeft = BitmapFactory.decodeResource(parent.getResources(), R.drawable.src_car_wheel);
        bpCarWheelRight = BitmapFactory.decodeResource(parent.getResources(), R.drawable.src_car_wheel);
        bigTree = BitmapFactory.decodeResource(parent.getResources(), R.drawable.src_tree);
        Matrix matrix = new Matrix();
        matrix.postScale(0.6f, 0.6f);
        smallTree = Bitmap.createBitmap(bigTree, 0, 0, bigTree.getWidth(), bigTree.getHeight(), matrix, true);
        gas1 = BitmapFactory.decodeResource(parent.getResources(), R.drawable.src_car_gas);
        gas2 = BitmapFactory.decodeResource(parent.getResources(), R.drawable.src_car_gas);
        gas3 = BitmapFactory.decodeResource(parent.getResources(), R.drawable.src_car_gas);
        roadPaint = new Paint();
        roadPaint.setAntiAlias(true);
        initAnimator();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect bounds = getBounds();
        int width = bounds.width();
        int height = bounds.height();
        float roadHeight = density * 3;//路的厚度
        float offsetBottom = density * 15;//路的最底部距parent的距离
        float roadBottom = height - offsetBottom;
        float roadTop = roadBottom - roadHeight;
        float roadLeft = 0;
        float roadRight = width;
        roadPaint.setColor(roadColor);
        roadPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(roadLeft, roadTop, roadRight, roadBottom, roadPaint);

        drawTree(canvas, width, height, roadTop);
        drawCar(canvas, width, height, roadTop);
    }

    private void drawTree(Canvas canvas, int width, int height, float roadTop) {
        float bigTreeL = (width + bigTree.getWidth()) * (pullPercent / 2 + timeLongBTree) - bigTree.getWidth();
        float bigTreeR = (width + bigTree.getWidth()) * (pullPercent / 2 + timeLongBTree);
        float bigTreeT = roadTop - height / 6 - bigTree.getHeight();
        float bigTreeB = bigTreeT + bigTree.getHeight();

        float smallTreeL = (width + smallTree.getWidth()) * (pullPercent / 2 + timeLongSTree) - smallTree.getWidth();
        float smallTreeR = (width + smallTree.getWidth()) * (pullPercent / 2 + timeLongSTree);
        float smallTreeT = roadTop - height / 4 - smallTree.getHeight();
        float smallTreeB = smallTreeT + smallTree.getHeight();
        canvas.drawBitmap(smallTree, null, new RectF(smallTreeL, smallTreeT, smallTreeR, smallTreeB), roadPaint);
        canvas.drawBitmap(bigTree, null, new RectF(bigTreeL, bigTreeT, bigTreeR, bigTreeB), roadPaint);
    }

    private void drawCar(Canvas canvas, int width, int height, float roadTop) {
        float bodyLeft = width - (width / 2 + bpCarBody.getWidth() / 2) * pullPercent;
        float leftL = bodyLeft + bpCarBody.getWidth() / 19;
        float topL = roadTop - bpCarWheelLeft.getHeight();
        float rightL = leftL + bpCarWheelLeft.getWidth();
        float bottomL = topL + bpCarWheelLeft.getHeight();
        canvas.drawBitmap(bpCarWheelLeft, null, new RectF(leftL, topL, rightL, bottomL), roadPaint);
        float bodyRight = width + bpCarBody.getWidth() - (width / 2 + bpCarBody.getWidth() / 2) * pullPercent;
        float leftR = bodyRight - bpCarBody.getWidth() / 11 - bpCarWheelRight.getWidth();
        float topR = roadTop - bpCarWheelRight.getHeight();
        float rightR = leftR + bpCarWheelRight.getWidth();
        float bottomR = topR + bpCarWheelRight.getHeight();
        canvas.drawBitmap(bpCarWheelRight, null, new RectF(leftR, topR, rightR, bottomR), roadPaint);
        float bodyTop = roadTop - bpCarWheelLeft.getHeight() / 5 - bpCarBody.getHeight() - offsetCarBodyToWheel;
        float bodyBottom = roadTop - bpCarWheelLeft.getHeight() / 5 - offsetCarBodyToWheel;
        canvas.drawBitmap(bpCarBody, null, new RectF(bodyLeft, bodyTop, bodyRight, bodyBottom), roadPaint);
    }

    public void setPullPercent(float pullPercent) {
        this.pullPercent = pullPercent;
        invalidateSelf();
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    private void initAnimator() {
        mAnimator = ValueAnimator.ofFloat(1.0f, 120.0f);
        mAnimator.setDuration(4800);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                if ((value % 40.0f) <= 20f) {
                    offsetCarBodyToWheel = (value % 40.0f) / 4;
                } else {
                    offsetCarBodyToWheel = (20.0f - (value % 40.0f)) / 4;
                }
                if ((value % 30.0f) <= 15f) {
                    timeLongBTree = (value % 30.0f) / 30f;
                } else {
                    timeLongBTree = (value % 30.0f) / 30f - 1.0f;
                }
                if ((value % 40.0f) <= 20f) {
                    timeLongSTree = (value % 40.0f) / 40f;
                } else {
                    timeLongSTree = (value / 40.0f) / 40.0f - 1.0f;
                }
                invalidateSelf();
            }
        });
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
    }

    @Override
    public void start() {
        if (!mAnimator.isRunning()) {
            mAnimator.start();
        }
    }

    @Override
    public void stop() {
        if (mAnimator.isRunning()) {
            mAnimator.cancel();
        }
    }

    @Override
    public boolean isRunning() {
        return mAnimator.isRunning();
    }
}
