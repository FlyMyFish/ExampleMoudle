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

/**
 * @author shichen 754314442@qq.com
 * Created by shichen on 2018/8/22.
 */
public class RefreshActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_auto_header);
        RecyclerView rvTest=findViewById(R.id.rv_list);
        rvTest.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvTest.setAdapter(new TestAdapter());
        ClassicsHeader classicsHeader;
        final SmartRefreshLayout srlRefresh=findViewById(R.id.srl_refresh);
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
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    private class TestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        String[] testString = {"111", "111", "111", "111", "111", "111", "111", "111", "111", "111",};

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TestView(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_auto_header, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof TestView) {
                TestView mHolder = (TestView) holder;
                mHolder.tvTest.setText(testString[position]);
            }
        }

        @Override
        public int getItemCount() {
            return testString.length;
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
