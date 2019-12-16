package com.div.home.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.div.home.R;
import com.div.home.ui.base.BaseActivity;

public class MagicRoomActivity extends BaseActivity {

    public static Intent getIntent(Context context, String message){
        Intent intent = new Intent(context, MagicRoomActivity.class);
        intent.putExtra("message", message);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magic_room);
    }
}
