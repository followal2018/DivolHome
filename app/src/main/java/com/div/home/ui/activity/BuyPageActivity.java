package com.div.home.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.div.home.R;
import com.div.home.databinding.ActivityBuyPageBinding;
import com.div.home.ui.base.BaseActivity;

public class BuyPageActivity extends BaseActivity {

    ActivityBuyPageBinding binding;

    public static Intent getIntent(Context context) {
        return new Intent(context, BuyPageActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_buy_page);
        binding.setActivity(this);
    }

    public void onClickAlreadyHave() {
        startActivity(AddRoomActivity.getIntent(this));
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }
}
