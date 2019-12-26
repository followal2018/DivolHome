package com.div.home.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.div.home.BuildConfig;
import com.div.home.R;
import com.div.home.databinding.ActivitySettingsBinding;
import com.div.home.ui.adapter.SettingsAdapter;
import com.div.home.ui.base.BaseActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends BaseActivity implements SettingsAdapter.ItemClickListener {

    private static final int POSITION_LOGOUT = 0;
    private static final int POSITION_WHETHER_SETTING = 1;
    private static final int POSITION_USER_PROFILE = 2;
    private static final int POSITION_DEVICE_LOGS = 3;
    private static final int POSITION_VOICE_ASSISTANCE = 4;
    private static final int POSITION_DEVICE_STATUS = 5;
    private static final int POSITION_ACCOUNT_SETTINGS = 6;
    private static final int POSITION_FEEDBACK = 7;
    private static final int POSITION_ABOUT = 8;

    ActivitySettingsBinding binding;

    public static Intent getIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                setTheme(R.style.DarkTheme);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                setTheme(R.style.LightTheme);
                break;
        }
       /* if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED || AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {

        } else {

        }*/
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);

        binding.rvSettings.setLayoutManager(new LinearLayoutManager(this));
        SettingsAdapter adapter = new SettingsAdapter(this, getContent());
        adapter.setClickListener(this);
        binding.rvSettings.setAdapter(adapter);
    }

    public List<String> getContent() {
        ArrayList<String> content = new ArrayList<>();

        content.add("Logout");
        content.add("Weather Settings");
        content.add("User Profile");
        content.add("Device Logs");
        content.add("Voice Assistant Automation");
        content.add("Device Status");
        content.add("Account Settings");
        content.add("Feedback");
        content.add("About Divol");
        String versionName = BuildConfig.VERSION_NAME;
        content.add("App Version\nVersion: " + versionName + " (CATERPILLAR)");

        return content;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case POSITION_LOGOUT:
                pref.clear();
                AuthUI.getInstance().signOut(SettingsActivity.this).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = AuthActivity.getIntent(SettingsActivity.this);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                        finishAffinity();
                        finish();
                    }
                });
                break;
            case POSITION_WHETHER_SETTING:
                Toast.makeText(this, "Weather Settings Will\nCome in Next Update..", Toast.LENGTH_SHORT).show();
                break;
            case POSITION_USER_PROFILE:
                startActivity(ProfileActivity.getIntent(this));
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;
            case POSITION_DEVICE_LOGS:
                Toast.makeText(this, "Device Log Settings Will\nCome Later This January", Toast.LENGTH_SHORT).show();
                break;
            case POSITION_VOICE_ASSISTANCE:
                startActivity(GoogleAlexaSetupActivity.getIntent(this));
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;
            case POSITION_DEVICE_STATUS:
                Toast.makeText(this, "Device Status Settings Will\nCome Later This January", Toast.LENGTH_SHORT).show();
                break;
            case POSITION_ACCOUNT_SETTINGS:
                Toast.makeText(this, "Account Settings Will\nCome Later This January", Toast.LENGTH_SHORT).show();
                break;
            case POSITION_FEEDBACK:
                startActivity(FeedbackActivity.getIntent(this));
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;
            case POSITION_ABOUT:
                startActivity(AboutActivity.getIntent(this));
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;
        }
    }
}
