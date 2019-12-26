package com.div.home.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.div.home.R;
import com.div.home.databinding.ActivitySelectRoomsBinding;
import com.div.home.ui.adapter.SelectRoomsAdapter;
import com.div.home.ui.base.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SelectRoomsActivity extends BaseActivity {

    private static final String TAG = SelectRoomsActivity.class.getSimpleName();
    private static final String EXTRA_SHARE_TO_USER_ID = "extra_share_to_user_id";

    private final ArrayList<String> contain = new ArrayList<>();
    private final String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().trim();
    SelectRoomsAdapter adapter;
    ActivitySelectRoomsBinding binding;
    String shareToUserId;

    public static Intent getIntent(Context context, String shareToUserId) {
        Intent intent = new Intent(context, SelectRoomsActivity.class);
        intent.putExtra(EXTRA_SHARE_TO_USER_ID, shareToUserId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_rooms);
        binding.setActivity(this);
        shareToUserId = getIntent().getStringExtra(EXTRA_SHARE_TO_USER_ID);
        binding.rvRooms.setLayoutManager(new LinearLayoutManager(SelectRoomsActivity.this));
        adapter = new SelectRoomsAdapter(SelectRoomsActivity.this);
        binding.rvRooms.setAdapter(adapter);
        fetchRooms();
    }

    public void onClickProceed() {
        if (adapter != null && !adapter.getSelectedRooms().isEmpty()) {
            List<String> roomsToShare = adapter.getSelectedRooms();
            for (String room : roomsToShare) {
                shareRoom(room);
            }
            addShareFrom();
        } else {
            Toast.makeText(this, "Please Select Room To Share", Toast.LENGTH_LONG).show();
        }
    }

    private void fetchRooms() {

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mGetReference = mDatabase.getReference("users").child(userId).child("rooms");
        mGetReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                contain.add(String.valueOf(dataSnapshot.getKey()));
                adapter.setmData(contain);
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

    public void shareRoom(String room) {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference shareToReference = mDatabase.getReference("users").child(userId).child("rooms").child(room.toLowerCase()).child("sharedTo").child(shareToUserId);
        shareToReference.setValue("");

        DatabaseReference shareToUserReference = mDatabase.getReference("users").child(userId).child("sharedTo").child(shareToUserId).child(room.toLowerCase());
        shareToUserReference.setValue("");
    }

    public void addShareFrom() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference shareFromUserReference = mDatabase.getReference("users").child(shareToUserId).child("sharedFrom").child(userId);
        shareFromUserReference.setValue("");

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }
}
