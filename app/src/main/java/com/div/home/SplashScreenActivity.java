package com.div.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import com.div.home.databinding.ActivitySplashScreenBinding;
import com.div.home.databinding.DialogNetworkErrorBinding;
import com.div.home.ui.activity.AuthActivity;
import com.div.home.ui.activity.HomeScreenActivity;
import com.div.home.ui.base.BaseActivity;
import com.div.home.util.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class SplashScreenActivity extends BaseActivity {

    ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);
        binding.setActivity(this);
        binding.txtVersion.setText(String.format("v.%s", BuildConfig.VERSION_NAME));

        int SPLASH_DISPLAY_LENGTH = 1200;
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                Check();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void Check() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (Utils.isNetworkConnected(this)) {
            if (user != null) {
                Intent intent = HomeScreenActivity.getIntent(this)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                finish();
            } else {
                Intent intent = AuthActivity.getIntent(this)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_CLEAR_TASK);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                startActivity(intent);
                finish();
            }
        } else {
            checkNet();
        }
    }

    private void checkNet() {

        DialogNetworkErrorBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_network_error, null, false);

        dialogBinding.btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

        dialogBinding.imgBtnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

        final PopupWindow popupWindow = new PopupWindow(dialogBinding.getRoot(), LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
        popupWindow.showAtLocation(dialogBinding.getRoot(), Gravity.CENTER, 0, 0);
    }
}
