package com.shichen.zhang.examplemodule.banner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shichen.zhang.examplemodule.R;

import java.util.List;

/**
 * Created by shichen on 2018/5/22.
 *
 * @author shichen 754314442@qq.com
 */
public class ImageBannerAdapter extends RecyclerView.Adapter implements IBannerAdapter {
    private BannerPageClickListener bannerPageClickListener;
    private Context mContext;
    private List<String> imgUrlList;

    public ImageBannerAdapter(BannerPageClickListener bannerPageClickListener, Context mContext, @NonNull List<String> imgUrlList) {
        this.bannerPageClickListener = bannerPageClickListener;
        this.mContext = mContext;
        this.imgUrlList = imgUrlList;
    }

    @Override
    public int realDataSize() {
        return imgUrlList == null ? 0 : imgUrlList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView img = new ImageView(mContext);
        RecyclerView.LayoutParams l = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setLayoutParams(l);
        img.setId(R.id.icon);
        return new RecyclerView.ViewHolder(img) {
        };
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ImageView img = holder.itemView.findViewById(R.id.icon);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bannerPageClickListener != null) {
                    bannerPageClickListener.onClick(holder.getAdapterPosition() % imgUrlList.size());
                }
            }
        });
        Glide.with(img.getContext()).load(imgUrlList.get(holder.getAdapterPosition() % imgUrlList.size())).into(img);
    }

    @Override
    public int getItemCount() {
        return realDataSize() < 2 ? realDataSize() : Integer.MAX_VALUE;
    }
}
