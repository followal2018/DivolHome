package com.div.home.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.div.home.R;
import com.div.home.databinding.ActivityConnectDeviceBinding;
import com.div.home.model.Appliance;
import com.div.home.ui.base.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ConnectDeviceActivity extends BaseActivity {

    private static final String TAG = ConnectDeviceActivity.class.getSimpleName();

    private static final String EXTRA_ROOM_NAME = "extra_room_name";
    private static final String EXTRA_STORED_APPLIANCES = "extra_stored_appliances";
    private static final String EXTRA_SELECTED_APPLIANCE = "extra_selected_appliance";

    private final String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    ActivityConnectDeviceBinding binding;
    String roomName;
    int storedAppliances;
    Appliance appliance;


    public static Intent getIntent(Context context, String roomName, int storedAppliances, Appliance appliance) {
        Intent intent = new Intent(context, ConnectDeviceActivity.class);
        intent.putExtra(EXTRA_ROOM_NAME, roomName);
        intent.putExtra(EXTRA_STORED_APPLIANCES, storedAppliances);
        intent.putExtra(EXTRA_SELECTED_APPLIANCE, Parcels.wrap(appliance));
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_connect_device);
        binding.setActivity(this);
        getData();

        String[] applianceKeys = getResources().getStringArray(R.array.appliances);
        List<String> applianceKeysList = new ArrayList<String>(Arrays.asList(applianceKeys));

        for (String key : applianceKeysList) {
            Log.e(TAG, "Key " + key);
        }

        if (storedAppliances >= 0 && storedAppliances < applianceKeysList.size()) {
            Log.e(TAG, "Next Key " + applianceKeysList.get(storedAppliances));
            addAppliance(appliance, applianceKeysList.get(storedAppliances));
        } else {
            Toast.makeText(this, "You Cant Add new device limit is over", Toast.LENGTH_SHORT).show();
        }
    }

    public void getData() {
        roomName = getIntent().getStringExtra(EXTRA_ROOM_NAME);
        storedAppliances = getIntent().getIntExtra(EXTRA_STORED_APPLIANCES, 0);
        appliance = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_SELECTED_APPLIANCE));
    }

    public void addAppliance(Appliance appliance, String key) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference staticAppliance = database.getReference("users").child(userId).child("rooms")
                .child(roomName).child("static");
        staticAppliance.child(key).setValue(0);

        /**
         * Change Appliance Structure when change database structure
         */
    }
}
