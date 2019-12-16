package com.div.home.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.div.home.R;
import com.div.home.databinding.ActivityRestoreBinding;
import com.div.home.ui.base.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static com.div.home.util.Constants.Pref.PREF_USER_ADDRESS;
import static com.div.home.util.Constants.Pref.PREF_USER_CITY;
import static com.div.home.util.Constants.Pref.PREF_USER_FIRST_NAME;
import static com.div.home.util.Constants.Pref.PREF_USER_LAST_NAME;
import static com.div.home.util.Constants.Pref.PREF_USER_PHONE_NUMBER;
import static com.div.home.util.Constants.Pref.PREF_USER_STATE;

public class RestoreActivity extends BaseActivity {

    private final int SPLASH_DISPLAY_LENGTH = 7000;
    ActivityRestoreBinding binding;
    String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().trim();

    public static Intent getIntent(Context context) {
        return new Intent(context, RestoreActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_restore);
        binding.setActivity(this);
        restoreFinder();
    }

    private void restoreFinder() {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(userID).child("userProfile");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("first name").exists()) {

                    binding.txtLabel.setText("Restoring Files");

                    String firstNameFB = Objects.requireNonNull(dataSnapshot.child("first name").getValue()).toString().trim();
                    String lastNameFB = Objects.requireNonNull(dataSnapshot.child("last name").getValue()).toString().trim();
                    String phoneNumberFB = Objects.requireNonNull(dataSnapshot.child("phone number").getValue()).toString().trim();
                    String addressFB = Objects.requireNonNull(dataSnapshot.child("address").getValue()).toString().trim();
                    String cityNameFB = Objects.requireNonNull(dataSnapshot.child("city name").getValue()).toString().trim();
                    String stateNameFB = Objects.requireNonNull(dataSnapshot.child("state name").getValue()).toString().trim();

                    pref.putString(PREF_USER_FIRST_NAME, firstNameFB);
                    pref.putString(PREF_USER_LAST_NAME, lastNameFB);
                    pref.putString(PREF_USER_PHONE_NUMBER, phoneNumberFB);
                    pref.putString(PREF_USER_ADDRESS, addressFB);
                    pref.putString(PREF_USER_CITY, cityNameFB);
                    pref.putString(PREF_USER_STATE, stateNameFB);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = HomeScreenActivity.getIntent(RestoreActivity.this).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        }
                    }, SPLASH_DISPLAY_LENGTH);
                } else {

                    Intent intent = ProfileActivity.getIntent(RestoreActivity.this).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
