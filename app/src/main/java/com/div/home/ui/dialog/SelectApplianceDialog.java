package com.div.home.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.div.home.R;
import com.div.home.databinding.DialogSelectApplianceBinding;
import com.div.home.model.Appliance;
import com.div.home.ui.activity.ConnectDeviceActivity;
import com.div.home.ui.adapter.SelectApplianceAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Nirav Mandani on 21-12-2019.
 * Followal Solutions
 */
public class SelectApplianceDialog extends DialogFragment implements SelectApplianceAdapter.ItemClickListener {

    private static final String TAG = SelectApplianceDialog.class.getSimpleName();
    private static final String EXTRA_ROOM_NAME = "roomName";
    private static final String EXTRA_USER_ID = "userId";

    Context context;
    ArrayList<Appliance> appliances = getAppliances();
    SelectApplianceAdapter adapter;
    SelectApplinceActionListener listener;
    DialogSelectApplianceBinding binding;
    String roomName, userId;
    int storedAppliances;

    public static SelectApplianceDialog getInstance(String roomName, String userId) {
        SelectApplianceDialog dialog = new SelectApplianceDialog();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_ROOM_NAME, roomName);
        bundle.putString(EXTRA_USER_ID, userId);
        dialog.setArguments(bundle);
        return dialog;
    }

    public void setListener(SelectApplinceActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            roomName = getArguments().getString(EXTRA_ROOM_NAME);
            userId = getArguments().getString(EXTRA_USER_ID);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_select_appliance, null, false);
        binding.setDialog(this);
        adapter = new SelectApplianceAdapter(context);
        GridLayoutManager manager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
        binding.rvAppliances.setLayoutManager(manager);
        binding.rvAppliances.setAdapter(adapter);
        adapter.setListener(this);
        adapter.setAppliances(appliances);

        binding.txtNegative.setOnClickListener(view -> {
            if (listener != null) {
                listener.onCancelSelectAppliance();
            }
            dismiss();
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("users").child(userId).child("rooms")
                .child(roomName).child("static");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                storedAppliances = (int) dataSnapshot.getChildrenCount() - 1;
                Log.e(TAG, "Key Count " + storedAppliances);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                .setView(binding.getRoot())
                .setCancelable(false);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onApplianceClicked(Appliance appliance) {
        startActivity(ConnectDeviceActivity.getIntent(context, roomName, storedAppliances, appliance));
        dismiss();
    }

    public ArrayList<Appliance> getAppliances() {
        ArrayList<Appliance> appliances = new ArrayList<>();
        appliances.add(new Appliance("Fan", 0, R.drawable.ic_home_icon));
        appliances.add(new Appliance("AC", 0, R.drawable.ic_moon));
        appliances.add(new Appliance("Light", 0, R.drawable.ic_rainbow));
        appliances.add(new Appliance("Tubelight", 0, R.drawable.ic_star));
        appliances.add(new Appliance("Computer", 0, R.drawable.ic_yellow_house));
        appliances.add(new Appliance("TV", 0, R.drawable.ic_timer_black_24dp));
        appliances.add(new Appliance("Fan", 0, R.drawable.ic_home_icon));
        appliances.add(new Appliance("AC", 0, R.drawable.ic_moon));
        appliances.add(new Appliance("Light", 0, R.drawable.ic_rainbow));
        appliances.add(new Appliance("Tubelight", 0, R.drawable.ic_star));
        appliances.add(new Appliance("Computer", 0, R.drawable.ic_yellow_house));
        appliances.add(new Appliance("TV", 0, R.drawable.ic_timer_black_24dp));
        return appliances;
    }

    public interface SelectApplinceActionListener {

        void onApplianceSelect(Appliance appliance);

        void onCancelSelectAppliance();
    }
}
