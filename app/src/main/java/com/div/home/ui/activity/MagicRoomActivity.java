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
import com.div.home.databinding.ActivityMagicRoomBinding;
import com.div.home.model.Appliance;
import com.div.home.ui.adapter.ApplianceAdapter;
import com.div.home.ui.base.BaseActivity;
import com.div.home.ui.dialog.SelectApplianceDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class MagicRoomActivity extends BaseActivity implements ApplianceAdapter.ItemClickListener {
    private static final String TAG = MagicRoomActivity.class.getSimpleName();
    private final String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    private final ArrayList<String> containts = new ArrayList<>();
    ActivityMagicRoomBinding binding;
    private ApplianceAdapter adapter;

    public static Intent getIntent(Context context, String message) {
        Intent intent = new Intent(context, MagicRoomActivity.class);
        intent.putExtra("roomName", message);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_magic_room);
        binding.setActivity(this);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String RoomNameSTR = Objects.requireNonNull(bundle.getString("roomName")).toUpperCase();
        binding.txtRoomName.setText(RoomNameSTR);
        fetchAppliances();
    }

    private void fetchAppliances() {

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        final String RoomNameSTR = Objects.requireNonNull(bundle.getString("roomName")).toLowerCase();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mGetReference = mDatabase.getReference("users/" + userId + "/rooms/" + RoomNameSTR);
        mGetReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                containts.add(String.valueOf(dataSnapshot.getValue()).toUpperCase());
                binding.rvAppliances.setLayoutManager(new LinearLayoutManager(MagicRoomActivity.this));
                adapter = new ApplianceAdapter(MagicRoomActivity.this, containts);
                adapter.setClickListener(MagicRoomActivity.this);
                binding.rvAppliances.setAdapter(adapter);


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
                Log.e(TAG, "Exception: " + databaseError.getDetails());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        wifiChecker();
    }

    private void wifiChecker() {

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String roomName = Objects.requireNonNull(bundle.getString("roomName")).toLowerCase();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("users").child(userId).child("rooms")
                .child(roomName).child("static");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("wifi")) {
                    String wifiState = Objects.requireNonNull(dataSnapshot.child("wifi").getValue()).toString();

                    if (wifiState.equals("null") || wifiState.isEmpty()) {
                        startActivity(WifiSettingsActivity.getIntent(MagicRoomActivity.this, roomName));
                    } else {
                        if (dataSnapshot.hasChild("appliance one")) {
                            Toast.makeText(MagicRoomActivity.this, "Ready!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MagicRoomActivity.this, "Select Appliance", Toast.LENGTH_SHORT).show();
                            openSelectApplianceDialog();
                        }
                    }
                } else {
                    myRef.child("wifi").setValue("null");
                    startActivity(WifiSettingsActivity.getIntent(MagicRoomActivity.this, roomName));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void openSelectApplianceDialog() {

        runOnUiThread(() -> {
            Bundle bundle = getIntent().getExtras();
            assert bundle != null;
            String roomName = Objects.requireNonNull(bundle.getString("roomName")).toLowerCase();
            SelectApplianceDialog dialog = SelectApplianceDialog.getInstance(roomName, userId);
            dialog.setListener(new SelectApplianceDialog.SelectApplinceActionListener() {

                @Override
                public void onApplianceSelect(Appliance appliance) {
                    Toast.makeText(MagicRoomActivity.this, "Added :-> " + appliance.getDisplayName(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelSelectAppliance() {

                }
            });
            dialog.show(getSupportFragmentManager(), null);
        });

    }

    @Override
    public void onItemClick() {

    }

    public void onClickAddAppliance() {

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String roomName = Objects.requireNonNull(bundle.getString("roomName")).toLowerCase();
        SelectApplianceDialog dialog = SelectApplianceDialog.getInstance(roomName, userId);
        dialog.setListener(new SelectApplianceDialog.SelectApplinceActionListener() {

            @Override
            public void onApplianceSelect(Appliance appliance) {
                Toast.makeText(MagicRoomActivity.this, "Added :-> " + appliance.getDisplayName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelSelectAppliance() {

            }
        });
        dialog.show(getSupportFragmentManager(), null);

        /*Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String RoomNameSTR = bundle.getString("roomName");
        Intent k = AddApplianceActivity.getIntent(this)
                .setFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_CLEAR_TOP);
        k.putExtra("RoomNameSTR", RoomNameSTR);
        startActivity(k);*/
    }

    public void onClickSyncDevice() {
        Intent l = new Intent(this, WebUrlViewActivity.class)
                .setFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(l);
    }

    public void onClickTimer() {
        Toast.makeText(this, "Comming Soon!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }
}

