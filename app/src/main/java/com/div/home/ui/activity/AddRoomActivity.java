package com.div.home.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.div.home.R;
import com.div.home.databinding.ActivityAddRoomBinding;
import com.div.home.ui.base.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class AddRoomActivity extends BaseActivity {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().trim();

    ActivityAddRoomBinding binding;

    private DatabaseReference roomNameRef, staticAppliance;

    public static Intent getIntent(Context context) {
        return new Intent(context, AddRoomActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_room);
        binding.setActivity(this);
    }

    public void onClickProceed() {
        if (binding.edtRoomName.getText().toString().isEmpty()) {

            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .playOn(binding.edtRoomName);

            binding.edtRoomName.setError("Enter a Name!");
            binding.edtRoomName.requestFocus();

        } else {

            roomNameRef = database.getReference("users").child(userID).child("rooms");
            roomNameRef.child(binding.edtRoomName.getText().toString().toLowerCase().trim()).setValue("");
            staticAppliance = database.getReference("users").child(userID).child("rooms")
                    .child(binding.edtRoomName.getText().toString().toLowerCase().trim()).child("appliances");
          /*  staticAppliance.child("appliance one").setValue(0);
            staticAppliance.child("appliance two").setValue(0);
            staticAppliance.child("appliance three").setValue(0);
            staticAppliance.child("appliance four").setValue(0);
            staticAppliance.child("fan speed").setValue(1);*/
            staticAppliance.child("wifi").setValue("null");

            Intent intent = HomeScreenActivity.getIntent(this)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            finish();
            Toast.makeText(AddRoomActivity.this, "Room Added Successfully.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }
}
