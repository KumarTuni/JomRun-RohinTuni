package com.rohin.jomrun.ui.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rohin.jomrun.R;
import com.rohin.jomrun.ui.utils.listners.RetryListner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class LoadView extends FrameLayout {

    private Context ctx;
    private ProgressBar progressBar;
    private LinearLayout loadmore_errorlayout;
    private ImageButton loadmore_retry;
    private TextView loadmore_errortxt;
    private RetryListner retryListner;

    public LoadView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ctx = context;
        init();
    }

    public LoadView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ctx = context;
        init();
    }

    private void init() {
        LayoutInflater.from(ctx).inflate(R.layout.progress_layout, this);
        progressBar = findViewById(R.id.loadmore_progress);
        loadmore_errorlayout = findViewById(R.id.loadmore_errorlayout);
        loadmore_errortxt = findViewById(R.id.loadmore_errortxt);
        loadmore_retry = findViewById(R.id.loadmore_retry);

        loadmore_retry.setOnClickListener(v -> {
            if (retryListner != null)
                retryListner.onRetryClick();
        });
        loadmore_errorlayout.setOnClickListener(v -> {
            if (retryListner != null)
                retryListner.onRetryClick();
        });

    }

    public void loading() {
        progressBar.setVisibility(VISIBLE);
        loadmore_errorlayout.setVisibility(GONE);
    }

    public void error(String errorMsg) {
        progressBar.setVisibility(GONE);
        loadmore_errorlayout.setVisibility(VISIBLE);
        if (!TextUtils.isEmpty(errorMsg))
            loadmore_errortxt.setText(errorMsg);
    }

    public void setRetryListner(RetryListner retryListner) {
        this.retryListner = retryListner;
    }
}
