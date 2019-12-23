package com.div.home.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebViewClient;

import androidx.databinding.DataBindingUtil;

import com.div.home.R;
import com.div.home.databinding.ActivityWebUrlViewBinding;
import com.div.home.ui.base.BaseActivity;

public class WebUrlViewActivity extends BaseActivity {

    ActivityWebUrlViewBinding binding;

    public static Intent getIntent(Context context) {
        return new Intent(context, WebUrlViewActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_url_view);

        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.setWebViewClient(new WebViewClient());
        binding.webView.loadUrl("http://192.168.4.1/update");
    }

}
