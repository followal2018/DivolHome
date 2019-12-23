package com.div.home.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.databinding.DataBindingUtil;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.div.home.R;
import com.div.home.databinding.ActivityGoogleAlexaSetupBinding;
import com.div.home.databinding.DialogAlexaBinding;
import com.div.home.databinding.DialogGoogleBinding;
import com.div.home.ui.base.BaseActivity;

public class GoogleAlexaSetupActivity extends BaseActivity {

    ActivityGoogleAlexaSetupBinding binding;

    public static Intent getIntent(Context context) {
        return new Intent(context, GoogleAlexaSetupActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_google_alexa_setup);
        binding.setActivity(this);

        YoYo.with(Techniques.Landing)
                .duration(700)
                .playOn(binding.imgGoogleAssistant);

        YoYo.with(Techniques.Landing)
                .duration(700)
                .playOn(binding.imgAlexa);
    }

    public void onClickGoogleKnowMore() {
        DialogGoogleBinding dialogGoogleBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_google, null, false);
        final PopupWindow popupWindow = new PopupWindow(dialogGoogleBinding.getRoot(), LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(binding.btnGoogleKnowMore, Gravity.CENTER, 0, 0);
        popupWindow.setOutsideTouchable(false);
        dialogGoogleBinding.btnGoogleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }

    public void onClickAlexaKnowMore(){
        DialogAlexaBinding dialogAlexaBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_alexa, null, false);
        final PopupWindow popupWindow = new PopupWindow(dialogAlexaBinding.getRoot(), LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(binding.btnAlexaKnowMore, Gravity.CENTER, 0, 0);
        popupWindow.setOutsideTouchable(false);
        dialogAlexaBinding.btnBackAlexa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }
}
