package com.div.home.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.div.home.R;
import com.div.home.databinding.ActivityFamilyBinding;
import com.div.home.ui.base.BaseActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class FamilyActivity extends BaseActivity {

    ActivityFamilyBinding binding;
    private IntentIntegrator qrScan;

    public static Intent getIntent(Context context) {
        return new Intent(context, FamilyActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_family);
        binding.setActivity(this);
        qrScan = new IntentIntegrator(this);
    }

    public void onClickAddFamily() {
        qrScan.initiateScan();
    }

    public void onClickBecomeFamily() {
        startActivity(AddMemberActivity.getIntent(this));
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                try {
                    startActivity(SelectRoomsActivity.getIntent(FamilyActivity.this, result.getContents()));
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }
}
