package com.div.home.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.div.home.R;
import com.div.home.ui.base.BaseActivity;

public class AddScheduleActivity extends BaseActivity {

    public static Intent getIntent(Context context) {
        return new Intent(context, AddScheduleActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
    }
}