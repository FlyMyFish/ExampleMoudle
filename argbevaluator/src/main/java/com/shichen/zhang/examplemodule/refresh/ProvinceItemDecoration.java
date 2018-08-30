package com.shichen.zhang.examplemodule.refresh;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.service.autofill.FillCallback;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.shichen.zhang.examplemodule.bean.CityBean;

import java.util.List;

/**
 * @author shichen 754314442@qq.com
 * Created by shichen on 2018/8/30.
 */
public class ProvinceItemDecoration extends RecyclerView.ItemDecoration {
    private int mHeight = 60;//一般的分割线高度
    private int lineColor = 0xffaf3bd4;
    private int textColor = 0xffffffff;
    private Paint linePaint;
    List<CityBean> cityBeanList;

    public ProvinceItemDecoration(List<CityBean> cityBeanList) {
        this.cityBeanList = cityBeanList;
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.FILL);
    }

    /**
     * 通过该方法，在Canvas上绘制内容，在绘制Item之前调用。（如果没有通过getItemOffsets设置偏移的话，Item的内容会将其覆盖）
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        /*final int left=parent.getLeft();
        final int right=parent.getRight();
        final int childCount=parent.getChildCount();
        for (int i=0;i<childCount;i++){
            final View childView=parent.getChildAt(i);
            final int bottom=childView.getTop();
            final int top=bottom-mHeight;
            linePaint.setColor(lineColor);
            c.drawRect(left,top,right,bottom,linePaint);
        }*/
    }

    /**
     * 通过该方法，在Canvas上绘制内容,在Item之后调用。(画的内容会覆盖在item的上层)
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        final int itemCount = state.getItemCount();
        final int childCount = parent.getChildCount();
        final int left = parent.getLeft() + parent.getPaddingLeft();
        final int right = parent.getRight() - parent.getPaddingRight();
        String preGroupName;
        String currentGroupName = null;
        float density = parent.getContext().getResources().getDisplayMetrics().density;
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            preGroupName = currentGroupName;
            currentGroupName = cityBeanList.get(position).getProvinceName();
            if (currentGroupName == null || TextUtils.equals(currentGroupName, preGroupName)) {
                continue;
            }
            int viewBottom = view.getBottom();
            float top = Math.max(mHeight * density, view.getTop());
            if (position + 1 < itemCount) {
                String nextGroupName = cityBeanList.get(position + 1).getProvinceName();
                if (!currentGroupName.equals(nextGroupName) && viewBottom < top) {
                    top = viewBottom;
                }
            }
            linePaint.setColor(lineColor);
            c.drawRect(left, top - mHeight * density, right, top, linePaint);
            linePaint.setTextSize(density * 16);
            linePaint.setColor(textColor);
            Paint.FontMetrics fm = linePaint.getFontMetrics();

            float baseLine = top - (mHeight * density - (fm.bottom - fm.top)) / 2 - fm.bottom;
            c.drawText(currentGroupName, left + 40, baseLine, linePaint);
        }
    }

    /**
     * 通过Rect为每个Item设置偏移，用于绘制Decoration。
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        String groupId = cityBeanList.get(position).getProvinceName();
        if (groupId == null) {
            return;
        }
        float density = parent.getContext().getResources().getDisplayMetrics().density;
        if (position == 0 || isFirstInGroup(position)) {
            //第一个item预留空间
            outRect.top =(int) (mHeight * density);
        }
    }

    private boolean isFirstInGroup(int pos) {
        if (pos == 0) {
            return true;
        } else {
            String prevGroupId = cityBeanList.get(pos - 1).getProvinceName();
            String groupId = cityBeanList.get(pos).getProvinceName();
            return !TextUtils.equals(prevGroupId, groupId);
        }
    }
}
