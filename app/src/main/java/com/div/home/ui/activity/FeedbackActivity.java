package com.div.home.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.div.home.R;
import com.div.home.databinding.ActivityFeedbackBinding;
import com.div.home.ui.base.BaseActivity;

public class FeedbackActivity extends BaseActivity {

    ActivityFeedbackBinding binding;

    public static Intent getIntent(Context context) {
        return new Intent(context, FeedbackActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feedback);
        binding.setActivity(this);
    }

    public void onClickSendFeed() {

        String subjectSTR = binding.edtSubject.getText().toString().trim();
        String bodySTR = binding.edtBody.getText().toString().trim();

        if (subjectSTR.isEmpty()) {
            binding.edtSubject.setError("Enter a Subject Please");
            binding.edtSubject.requestFocus();
        } else if (bodySTR.isEmpty()) {
            binding.edtBody.setError("Enter the Message Please");
            binding.edtBody.requestFocus();
        } else {
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{"divhome.inbox@gmail.com"});
            email.putExtra(Intent.EXTRA_SUBJECT, subjectSTR);
            email.putExtra(Intent.EXTRA_TEXT, bodySTR);
            email.setType("message/rfc822");
            startActivity(Intent.createChooser(email, "Choose an Email client :"));
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }
}
