package com.shichen.zhang.examplemodule;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Animatable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * 进度框
 * Created by Administrator on 2017/9/11.
 */

public class ScheduleSeekBar extends View implements Animatable{
    public ScheduleSeekBar(Context context) {
        this(context, null);
    }

    public ScheduleSeekBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScheduleSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        density = context.getResources().getDisplayMetrics().density;
        bgSize = density * bgSize;
        btnHeight = density * btnHeight;
        textSize = density * textSize;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ScheduleSeekBar, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.ScheduleSeekBar_scheduleTextColor:
                    scheduleColor = a.getColor(attr, Color.parseColor("#FA2F2C"));
                    break;
                case R.styleable.ScheduleSeekBar_foreColor:
                    foreColor = a.getColor(attr, Color.parseColor("#FFFFFF"));
                    break;
                case R.styleable.ScheduleSeekBar_backColor:
                    bgColor = a.getColor(attr, Color.parseColor("#FF6D60"));
                    break;
                case R.styleable.ScheduleSeekBar_buttonColor:
                    btnTopColor = a.getColor(attr, Color.parseColor("#FF8686"));
                    break;
                default:
                    break;
            }
        }
        a.recycle();
        initPaint();
    }

    private int bgColor = Color.parseColor("#FF6D60");
    private int foreColor = Color.parseColor("#FFFFFF");
    private int btnTopColor = Color.parseColor("#F9E9AB");
    private int btnBottomColor = Color.parseColor("#FFDC40");
    private int scheduleColor = Color.parseColor("#FA2F2C");
    private float bgSize = 3.0f;
    /**
     * 进度条的高
     */
    private float btnHeight = 10.0f;
    private float btnWidth = 70.0f;
    /**
     * 按钮的半径
     */
    private float textSize = 8.0f;
    /**
     * 文字的size
     */
    private float density;
    private Paint bgPaint;
    /**
     * 背景画笔
     */
    private Paint forePaint;
    /**
     * 进度颜色
     */
    private Paint btnPaint;
    private Paint btnBackPaint;
    /**
     * 进度按钮颜色
     */
    private Paint schedulePaint;

    /**
     * 上方文字颜色
     */
    private void initPaint() {
        bgPaint = new Paint();
        bgPaint.setColor(bgColor);
        bgPaint.setAntiAlias(true);
        bgPaint.setStyle(Paint.Style.FILL);
        btnBackPaint = new Paint();
        btnBackPaint.setColor(0xffDC4849);
        btnBackPaint.setAntiAlias(true);
        btnBackPaint.setStyle(Paint.Style.FILL);
        forePaint = new Paint();
        forePaint.setColor(foreColor);
        forePaint.setAntiAlias(true);
        forePaint.setStyle(Paint.Style.FILL);
        btnPaint = new Paint();
        btnPaint.setColor(btnTopColor);
        btnPaint.setAntiAlias(true);
        btnPaint.setStyle(Paint.Style.FILL);
        schedulePaint = new Paint();
        schedulePaint.setColor(scheduleColor);
        schedulePaint.setAntiAlias(true);
        schedulePaint.setTextSize(textSize);
        schedulePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBg(canvas);
        drawProgress(canvas);
        drawBtn(canvas);
        drawSchedule(canvas);
    }

    private void drawBg(Canvas canvas) {
        RectF rectF = new RectF();
        rectF.set(0.0f, Float.valueOf(getMeasuredHeight()) - btnHeight - bgSize / 2, Float.valueOf(getMeasuredWidth()), Float.valueOf(getMeasuredHeight()) - btnHeight + bgSize / 2);
        canvas.drawRoundRect(rectF, bgSize / 2, bgSize / 2, bgPaint);
    }

    private void drawProgress(Canvas canvas) {
        RectF rectF = new RectF();
        rectF.set(0.0f, Float.valueOf(getMeasuredHeight()) - btnHeight - bgSize / 2, (Float.valueOf(getMeasuredWidth())) / 100 * progress, Float.valueOf(getMeasuredHeight()) - btnHeight + bgSize / 2);
        canvas.drawRoundRect(rectF, bgSize / 2, bgSize / 2, forePaint);
    }

    private void drawBtn(Canvas canvas) {
        float rx = (Float.valueOf(getMeasuredWidth())) / 100 * progress;
        float ry = Float.valueOf(getMeasuredHeight()) - btnHeight;
        RectF rectF = new RectF();
        rectF.set(rx - btnWidth / 2, ry - btnHeight / 2, rx + btnWidth / 2, ry + btnHeight / 2);
        LinearGradient linearGradient = new LinearGradient(rx, ry - btnHeight / 2, rx, ry + btnHeight / 2, btnTopColor, btnBottomColor, Shader.TileMode.MIRROR);
        btnPaint.setShader(linearGradient);
        RectF rectBg = new RectF();
        rectBg.set(rx - btnWidth / 2, ry - btnHeight / 2 + 6.0f, rx + btnWidth / 2, ry + btnHeight / 2 + 6.0f);
        setLayerType(LAYER_TYPE_SOFTWARE, btnBackPaint);
        btnBackPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.NORMAL));
        canvas.drawRoundRect(rectBg, btnHeight / 2, btnHeight / 2, btnBackPaint);
        canvas.drawRoundRect(rectF, btnHeight / 2, btnHeight / 2, btnPaint);
    }

    private void drawSchedule(Canvas canvas) {
        Rect textBound = new Rect();
        int progressInt = (int) progress;
        String scheduleStr = progressInt + "% ";
        schedulePaint.getTextBounds(scheduleStr, 0, scheduleStr.length() - 1, textBound);
        float rx = (Float.valueOf(getMeasuredWidth())) / 100 * progress;
        float ry = Float.valueOf(getMeasuredHeight()) - btnHeight;
        canvas.drawText(scheduleStr, rx - textBound.width() / 2, ry + textBound.height() / 2, schedulePaint);
    }

    private float progress = 0;

    public void setProgress(int setProgress) {
        setupAnimation(setProgress);
        start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, (int) btnHeight * 2);
    }

    private Animation updateAnim;

    private void setupAnimation(final int setProgress) {
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                progress = setProgress * interpolatedTime;
                invalidate();
            }
        };
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isRunning = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isRunning = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.setDuration(2500);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(0);
        updateAnim = animation;
    }

    @Override
    public void start() {
        updateAnim.reset();
        startAnimation(updateAnim);
    }

    @Override
    public void stop() {
        clearAnimation();
    }

    private boolean isRunning = false;

    @Override
    public boolean isRunning() {
        return isRunning;
    }
}
