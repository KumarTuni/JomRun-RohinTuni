package com.rohin.jomrun.ui.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rohin.jomrun.R;
import com.rohin.jomrun.ui.utils.listners.RetryListner;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class PagedRecyclerView extends RelativeLayout {

    private Context ctx;
    private PagedListAdapter adapter;
    public RecyclerView recyclerView;
    private LoadView loadingMoreView;
    private TextView noResultTxtV;
    private LoadView loadingView;
    private RetryListner retryListner;

    public PagedRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;
        init();
    }

    public PagedRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.ctx = context;
        init();

    }

    private void init() {

        LayoutInflater.from(ctx).inflate(R.layout.paged_recyclerview, this);
        recyclerView = findViewById(R.id.dataRV);
        noResultTxtV = findViewById(R.id.noResult);
        loadingMoreView = findViewById(R.id.loadingMoreView);
        loadingView = findViewById(R.id.loadingView);
        loadingView.setRetryListner(retryListner);
        loadingMoreView.setRetryListner(retryListner);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        loadingView.setVisibility(GONE);
        loadingMoreView.setVisibility(GONE);
    }

    public void setAdapter(PagedListAdapter adapter) {
        this.adapter = adapter;
        recyclerView.setAdapter(adapter);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    public void error(String errorMsg) {
        noResultTxtV.setVisibility(GONE);
        recyclerView.setVisibility(VISIBLE);
        loadingView.setVisibility(VISIBLE);
        loadingView.error(errorMsg);

    }

    public void empty() {
        noResultTxtV.setVisibility(VISIBLE);
        recyclerView.setVisibility(GONE);
        loadingView.setVisibility(GONE);
        loadingMoreView.setVisibility(GONE);
    }

    public void success() {
        noResultTxtV.setVisibility(GONE);
        recyclerView.setVisibility(VISIBLE);
        loadingMoreView.setVisibility(GONE);
        loadingView.setVisibility(GONE);
        recyclerView.requestLayout();
    }

    public void loading() {
        noResultTxtV.setVisibility(GONE);
        recyclerView.setVisibility(VISIBLE);
        loadingMoreView.setVisibility(GONE);
        loadingView.setVisibility(VISIBLE);
        loadingView.loading();
    }

    public void loadingMore() {
        noResultTxtV.setVisibility(GONE);
        recyclerView.setVisibility(VISIBLE);
        loadingMoreView.setVisibility(VISIBLE);
        loadingMoreView.loading();
    }

    public void loadingMoreError(String errorMessage) {
        noResultTxtV.setVisibility(GONE);
        recyclerView.setVisibility(VISIBLE);
        loadingMoreView.setVisibility(VISIBLE);
        loadingMoreView.error(errorMessage);
    }

    public void setRetryListner(RetryListner retryListner) {
        this.retryListner = retryListner;
        loadingView.setRetryListner(retryListner);
        loadingMoreView.setRetryListner(retryListner);

    }

    public void setOnScrollListner(RecyclerView.OnScrollListener onScrollListner) {
        recyclerView.addOnScrollListener(onScrollListner);
    }

}


