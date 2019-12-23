package com.div.home.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import com.div.home.R;
import com.div.home.databinding.ActivityAuthBinding;
import com.div.home.ui.base.BaseActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class AuthActivity extends BaseActivity {

    private static final int LOGIN_REQUEST = 7117;

    ActivityAuthBinding binding;

    public static Intent getIntent(Context context) {
        return new Intent(context, AuthActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth);
        binding.setActivity(this);

        /*YoYo.with(Techniques.RotateIn)
                .duration(700)
                .playOn(binding.imgMoon);

        YoYo.with(Techniques.RotateInDownLeft)
                .duration(700)
                .playOn(binding.imgStart);

        YoYo.with(Techniques.FlipInX)
                .duration(700)
                .playOn(binding.imgWifi);

        YoYo.with(Techniques.RotateInDownRight)
                .duration(700)
                .playOn(binding.imgHome);


        YoYo.with(Techniques.RotateIn)
                .duration(700)
                .playOn(binding.imgRainbow);*/

    }

    public void showSignInOptions() {
        List<AuthUI.IdpConfig> providers = Collections.singletonList(
                new AuthUI.IdpConfig.GoogleBuilder().build());

        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build(), LOGIN_REQUEST);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOGIN_REQUEST) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

                Intent intent = RestoreActivity.getIntent(this).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                finish();

            } else if (response == null) {

                Toast.makeText(this, "Error while signing in.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "" + Objects.requireNonNull(response.getError()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
