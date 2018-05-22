package com.shichen.zhang.examplemodule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.shichen.zhang.examplemodule.banner.BannerConfig;
import com.shichen.zhang.examplemodule.banner.BannerPageClickListener;
import com.shichen.zhang.examplemodule.banner.ImageBannerAdapter;
import com.shichen.zhang.examplemodule.banner.RecyclerBanner;

import java.util.ArrayList;
import java.util.List;

/**
 * 展示seekBar
 * Created by Administrator on 2017/9/12.
 */

public class SeeScheduleSeekBarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_schedule_seek_bar);
        scheduleSeekBar = (ScheduleSeekBar) findViewById(R.id.progress);
        scheduleSeekBar.post(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 1; i <= 100; i++) {
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {

                            } finally {
                                final int currentPro = i;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        scheduleSeekBar.setProgress(currentPro);
                                    }
                                });
                            }
                        }
                    }
                }).start();

            }
        });
        final TextView tvPickedNum=(TextView)findViewById(R.id.tv_picked_num);
        pickNumView=(PickNumView) findViewById(R.id.pnv);
        pickNumView.setOnNumPickedListener(new PickNumView.OnNumPickedListener() {
            @Override
            public void numPicked(int value) {
                tvPickedNum.setText(String.valueOf(value));
            }
        });
        //banner
        RecyclerBanner banner=findViewById(R.id.rb_image);
        List<String> imgUrlList=new ArrayList<>();
        imgUrlList.add("http://stc.zjol.com.cn/g1/M00006AwGRGQlOiI5yAH4l1AADLsbdthjM422.jpg?width=720&height=360");
        imgUrlList.add("http://img2.imgtn.bdimg.com/it/u=531648727,2872138995&fm=214&gp=0.jpg");
        imgUrlList.add("http://img4.imgtn.bdimg.com/it/u=819918919,3846133166&fm=214&gp=0.jpg");
        imgUrlList.add("http://img1.qunarzz.com/travel/d3/1601/b7/358cf48778a6b8f7.jpg_r_720x400x95_053addc6.jpg");
        imgUrlList.add("http://a.hiphotos.baidu.com/nuomi/eWH=720,436/sign=2112af3fff03738dcc20712f8429807d/b8389b504fc2d5621d260072ee1190ef76c66c07.jpg");
        ImageBannerAdapter bannerAdapter=new ImageBannerAdapter(new BannerPageClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getApplicationContext(), String.valueOf(position),Toast.LENGTH_SHORT).show();
            }
        },this,imgUrlList);
        BannerConfig config=new BannerConfig.Builder()
                .setAdapter(bannerAdapter)
                .setDefaultDrawable(this)
                .setSelectedDrawable(this)
                .create();
        banner.initBanner(config);
    }

    private ScheduleSeekBar scheduleSeekBar;
    private PickNumView pickNumView;
}
