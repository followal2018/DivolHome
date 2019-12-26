package com.div.home.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.div.home.R;
import com.div.home.databinding.ActivitySharedRoomsBinding;
import com.div.home.model.SharedRoom;
import com.div.home.ui.adapter.SharedRoomsAdapter;
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

public class SharedRoomsActivity extends BaseActivity implements SharedRoomsAdapter.ItemClickListener {
    private static final String EXTRA_SHARED_FROM_USER_ID = "extra_shared_from_user_id";

    private String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().trim();
    String sharedFromUserId;
    ActivitySharedRoomsBinding binding;
    SharedRoomsAdapter adapter;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    List<SharedRoom> sharedRooms = new ArrayList<>();

    public static Intent getIntent(Context context, String sharedFromUserId) {
        Intent intent = new Intent(context, SharedRoomsActivity.class);
        intent.putExtra(EXTRA_SHARED_FROM_USER_ID, sharedFromUserId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shared_rooms);
        binding.setActivity(this);
        sharedFromUserId = getIntent().getStringExtra(EXTRA_SHARED_FROM_USER_ID);
        adapter = new SharedRoomsAdapter(this);
        adapter.setListener(this);
        binding.rvRooms.setLayoutManager(new LinearLayoutManager(this));
        binding.rvRooms.setAdapter(adapter);
        fetchSharedRooms(sharedFromUserId);
    }

    /*public void fetchSharedFromUsers() {
        DatabaseReference mGetReference = mDatabase.getReference("users").child(userId).child("sharedFrom");
        mGetReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                fetchSharedRooms(String.valueOf(dataSnapshot.getKey()));
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

            }
        });
    }*/

    public void fetchSharedRooms(final String sharedFromUserId) {
        DatabaseReference mGetReference = mDatabase.getReference("users").child(sharedFromUserId).child("sharedTo").child(userId);
        mGetReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey() != null) {
                    SharedRoom sharedRoom = new SharedRoom();
                    sharedRoom.setRoomName(dataSnapshot.getKey().toString());
                    sharedRoom.setUserId(sharedFromUserId);
                    sharedRooms.add(sharedRoom);
                    adapter.setAppliances(sharedRooms);
                }
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

            }
        });
    }

    @Override
    public void onRoomClicked(SharedRoom sharedRoom) {
        Intent intent1 = MagicRoomActivity.getIntent(this, sharedRoom.getRoomName(), sharedRoom.getUserId());
        startActivity(intent1);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }
}
