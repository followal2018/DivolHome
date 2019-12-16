package com.div.home.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.div.home.R;
import com.div.home.databinding.ActivityHomeScreenBinding;
import com.div.home.databinding.DialogDeleteBinding;
import com.div.home.ui.adapter.RoomsAdapter;
import com.div.home.ui.base.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class HomeScreenActivity extends BaseActivity implements RoomsAdapter.ItemClickListener {
    private static final String TAG = HomeScreenActivity.class.getSimpleName();

    private final ArrayList<String> contain = new ArrayList<>();
    private final String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().trim();

    ActivityHomeScreenBinding binding;
    RoomsAdapter adapter;
    private Boolean check = false;

    public static Intent getIntent(Context context) {
        return new Intent(context, HomeScreenActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen);
        binding.setActivity(this);

        String firstName = pref.getString("firstName", "user name").toUpperCase();
        if (firstName.equals("USER NAME")) {
            Intent intent = new Intent(HomeScreenActivity.this, RestoreActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            binding.txtNotification.setText("WELCOME " + firstName);
//            new weatherTask().execute();
        }
    }

    public void onClickSetting() {
        startActivity(SettingsActivity.getIntent(this));
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    public void onClickAddRoom() {
        startActivity(BuyPageActivity.getIntent(this));
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    public void onClickAddFamily() {
        startActivity(FamilyActivity.getIntent(this));
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    public void onClickTimer() {
        startActivity(SetTimerActivity.getIntent(this));
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    public void onClickAI() {
        startActivity(WebUrlViewActivity.getIntent(this));
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!check) {
            fetchRooms();
            check = true;
        }
    }

    private void fetchRooms() {

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mGetReference = mDatabase.getReference("users").child(userId).child("rooms");
        mGetReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                contain.add(String.valueOf(dataSnapshot.getKey()).toUpperCase());
                binding.rvRooms.setLayoutManager(new LinearLayoutManager(HomeScreenActivity.this));
                adapter = new RoomsAdapter(HomeScreenActivity.this, contain);
                adapter.setClickListener(HomeScreenActivity.this);
                binding.rvRooms.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Exception: " + databaseError);
            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    @Override
    public void onItemClick(int position) {
        String title1 = ((TextView) Objects.requireNonNull(binding.rvRooms.findViewHolderForAdapterPosition(position))
                .itemView.findViewById(R.id.roomNameHomeScreenTV)).getText().toString().toLowerCase();

        Intent intent1 = MagicRoomActivity.getIntent(this, title1)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent1);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);

    }

    @Override
    public void longClickListener(View view, int position) {

        final String title1 = ((TextView) Objects.requireNonNull(binding.rvRooms.findViewHolderForAdapterPosition(position))
                .itemView.findViewById(R.id.roomNameHomeScreenTV)).getText().toString().toUpperCase();

        DialogDeleteBinding dialogDeleteBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_delete, null, false);
        dialogDeleteBinding.txtRoomName.setText(title1 + " ?");
        final PopupWindow popupWindow = new PopupWindow(dialogDeleteBinding.getRoot(), LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        dialogDeleteBinding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String adder = title1.toLowerCase();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(userId).child("rooms").child(adder);
                ref.removeValue();
                finish();
                startActivity(getIntent());
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            }
        });
        dialogDeleteBinding.btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(dialogDeleteBinding.getRoot(), Gravity.CENTER, 0, 0);
        popupWindow.setOutsideTouchable(false);
        YoYo.with(Techniques.Landing)
                .duration(200)
                .playOn(dialogDeleteBinding.getRoot());
    }
}
