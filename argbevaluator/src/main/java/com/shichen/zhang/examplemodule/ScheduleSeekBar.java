package com.shichen.zhang.examplemodule;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 进度框
 * Created by Administrator on 2017/9/11.
 */

public class ScheduleSeekBar extends View {
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
        btnSize = btnSize * btnSize;
        textSize = density * textSize;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ScheduleSeekBar, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.ScheduleSeekBar_scheduleTextColor:
                    scheduleColor = a.getColor(attr, Color.parseColor("#FF8080"));
                    break;
                case R.styleable.ScheduleSeekBar_foreColor:
                    foreColor = a.getColor(attr, Color.parseColor("#FF8080"));
                    break;
                case R.styleable.ScheduleSeekBar_backColor:
                    bgColor = a.getColor(attr, Color.parseColor("#999999"));
                    break;
                case R.styleable.ScheduleSeekBar_buttonColor:
                    btnColor = a.getColor(attr, Color.parseColor("#FF8686"));
                    break;

            }
        }
        a.recycle();
        initPaint();
    }

    private int bgColor = Color.parseColor("#999999");
    private int foreColor = Color.parseColor("#FF8080");
    private int btnColor = Color.parseColor("#FF8686");
    private int scheduleColor = Color.parseColor("#FF8080");
    private float bgSize = 4.0f;//进度条的高
    private float btnSize = 5.0f;//按钮的半径
    private float textSize = 12.0f;//文字的size
    private float density;
    private Paint bgPaint;//背景画笔
    private Paint forePaint;//进度颜色
    private Paint btnPaint;//进度按钮颜色
    private Paint schedulePaint;//上方文字颜色

    private void initPaint() {
        bgPaint = new Paint();
        bgPaint.setColor(bgColor);
        bgPaint.setAntiAlias(true);
        bgPaint.setStyle(Paint.Style.FILL);
        forePaint = new Paint();
        forePaint.setColor(foreColor);
        forePaint.setAntiAlias(true);
        forePaint.setStyle(Paint.Style.FILL);
        btnPaint = new Paint();
        btnPaint.setColor(btnColor);
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
        rectF.set(0.0f + btnSize, Float.valueOf(getMeasuredHeight()) - btnSize - bgSize / 2, Float.valueOf(getMeasuredWidth()) - btnSize, Float.valueOf(getMeasuredHeight()) - btnSize + bgSize / 2);
        canvas.drawRoundRect(rectF, bgSize / 2, bgSize / 2, bgPaint);
    }

    private void drawProgress(Canvas canvas) {
        RectF rectF = new RectF();
        rectF.set(0.0f + btnSize, Float.valueOf(getMeasuredHeight()) - btnSize - bgSize / 2, (Float.valueOf(getMeasuredWidth()) - btnSize) / 100 * progress, Float.valueOf(getMeasuredHeight()) - btnSize + bgSize / 2);
        canvas.drawRoundRect(rectF, bgSize / 2, bgSize / 2, forePaint);
    }

    private void drawBtn(Canvas canvas) {
        float rx = btnSize + (Float.valueOf(getMeasuredWidth()) - btnSize * 2) / 100 * progress;
        float ry = Float.valueOf(getMeasuredHeight()) - btnSize;
        canvas.drawCircle(rx, ry, btnSize, btnPaint);
    }

    private void drawSchedule(Canvas canvas) {
        Rect textBound = new Rect();
        String scheduleStr = progress + "%";
        schedulePaint.getTextBounds(scheduleStr, 0, scheduleStr.length() - 1, textBound);
        float tx = 0.0f;
        if (progress < 5) {
            tx = (Float.valueOf(getMeasuredWidth()) - btnSize * 2) / 100 * progress + textBound.width();
        } else if (progress > 95) {
            tx = (Float.valueOf(getMeasuredWidth()) - btnSize * 2) / 100 * progress - textBound.width();
        } else {
            tx = (Float.valueOf(getMeasuredWidth()) - btnSize * 2) / 100 * progress - textBound.width() / 2;
        }
        float ty = Float.valueOf(getMeasuredHeight()) - btnSize * 2 - textBound.height();
        canvas.drawText(scheduleStr, tx, ty, schedulePaint);
    }

    private int progress = 0;

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, (int) (btnSize * 2 + textSize * 1.5));
    }
}
