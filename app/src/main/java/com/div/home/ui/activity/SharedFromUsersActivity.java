package com.div.home.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.div.home.R;
import com.div.home.databinding.ActivitySharedFromUsersBinding;
import com.div.home.model.User;
import com.div.home.ui.adapter.SharedFromUsersAdapter;
import com.div.home.ui.base.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SharedFromUsersActivity extends BaseActivity implements SharedFromUsersAdapter.ItemClickListener {

    private final String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().trim();
    ActivitySharedFromUsersBinding binding;
    List<User> users = new ArrayList<>();
    SharedFromUsersAdapter adapter;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    public static Intent getIntent(Context context) {
        return new Intent(context, SharedFromUsersActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shared_from_users);
        binding.setActivity(this);

        binding.rvSchedules.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SharedFromUsersAdapter(this);
        adapter.setListener(this);
        binding.rvSchedules.setAdapter(adapter);

        fetchSharedFromUsers();
    }

    public void fetchSharedFromUsers() {
        DatabaseReference mGetReference = mDatabase.getReference("users").child(userId).child("sharedFrom");
        mGetReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                fetchUserProfile(String.valueOf(dataSnapshot.getKey()));
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

    public void fetchUserProfile(final String userId) {

        DatabaseReference mGetReference = mDatabase.getReference("users").child(userId).child("userProfile");
        mGetReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("first name")) {
                    User user = new User();
                    user.setUserId(userId);
                    if (dataSnapshot.child("first name").getValue() != null)
                        user.setFirstName(dataSnapshot.child("first name").getValue().toString());
                    if (dataSnapshot.child("last name").getValue() != null)
                        user.setLastName(dataSnapshot.child("last name").getValue().toString());
                    users.add(user);
                    adapter.setmData(users);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onRoomClicked(User user) {
        startActivity(SharedRoomsActivity.getIntent(this, user.getUserId()));
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }
}
