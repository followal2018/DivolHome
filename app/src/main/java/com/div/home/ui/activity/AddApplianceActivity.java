package com.div.home.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.div.home.R;
import com.div.home.databinding.ActivityAddApplianceBinding;
import com.div.home.ui.base.BaseActivity;

public class AddApplianceActivity extends BaseActivity {

    ActivityAddApplianceBinding binding;

    public static Intent getIntent(Context context){
        return new Intent(context, AddApplianceActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_appliance);
        binding.setActivity(this);
    }
}
