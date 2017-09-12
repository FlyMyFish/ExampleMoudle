package com.shichen.zhang.examplemodule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

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
    }

    private ScheduleSeekBar scheduleSeekBar;
}
