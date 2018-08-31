package com.shichen.zhang.examplemodule.refresh;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.shichen.zhang.examplemodule.R;
import com.shichen.zhang.examplemodule.bean.CityBean;
import com.shichen.zhang.examplemodule.utils.LocalUtil;

import java.util.List;

/**
 * @author shichen 754314442@qq.com
 * Created by shichen on 2018/8/22.
 */
public class RefreshActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_auto_header);
        RecyclerView rvTest = findViewById(R.id.rv_list);
        rvTest.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvTest.setAdapter(new TestAdapter(LocalUtil.getInstance(this).getAllCity()));
        rvTest.addItemDecoration(new ProvinceItemDecoration(LocalUtil.getInstance(this).getAllCity()));
        final SmartRefreshLayout srlRefresh = findViewById(R.id.srl_refresh);
        srlRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(15000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    srlRefresh.finishRefresh();
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    private class TestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<CityBean> allCity;
        int[] colorList = {0xffff7373, 0xff876ed7, 0xffffd300, 0xff00cc00};

        public TestAdapter(List<CityBean> allCity) {
            this.allCity = allCity;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TestView(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_auto_header, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof TestView) {
                TestView mHolder = (TestView) holder;
                mHolder.tvTest.setText(allCity.get(position).getCityName());
                mHolder.tvTest.setBackgroundColor(colorList[position % colorList.length]);
            }
        }

        @Override
        public int getItemCount() {
            return allCity.size();
        }

        class TestView extends RecyclerView.ViewHolder {
            TextView tvTest;

            public TestView(View itemView) {
                super(itemView);
                tvTest = itemView.findViewById(R.id.tv_test);
            }
        }
    }
}
