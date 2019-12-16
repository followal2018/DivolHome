package com.div.home.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.div.home.R;
import com.div.home.databinding.ActivityProfileBinding;
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

public class ProfileActivity extends BaseActivity {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().trim();
    ActivityProfileBinding binding;
    private DatabaseReference firstNameRef;

    public static Intent getIntent(Context context) {
        return new Intent(context, ProfileActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
    }

    public void onSaveClicked() {

        String firstNameST = binding.edtFirstName.getText().toString().toLowerCase().trim();
        String lastNameST = binding.edtLastName.getText().toString().toLowerCase().trim();
        String phoneNumberST = binding.edtPhoneNumber.getText().toString().toLowerCase().trim();
        String addressST = binding.edtAddress.getText().toString().toLowerCase().trim();
        String cityST = binding.edtCity.getText().toString().toLowerCase().trim();
        String stateST = binding.edtState.getText().toString().toLowerCase().trim();

        if (firstNameST.isEmpty()) {
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .playOn(binding.edtFirstName);
            binding.edtFirstName.setError("Please Enter First Name");
            binding.edtFirstName.requestFocus();
        } else if (lastNameST.isEmpty()) {
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .playOn(binding.edtLastName);
            binding.edtLastName.setError("Please Enter Last Name");
            binding.edtLastName.requestFocus();
        } else if (phoneNumberST.isEmpty()) {
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .playOn(binding.edtPhoneNumber);
            binding.edtPhoneNumber.setError("Please Enter Phone Number");
            binding.edtPhoneNumber.requestFocus();
        } else if (addressST.isEmpty()) {
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .playOn(binding.edtAddress);
            binding.edtAddress.setError("Please Enter Address");
            binding.edtAddress.requestFocus();
        } else if (cityST.isEmpty()) {
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .playOn(binding.edtCity);
            binding.edtCity.setError("Please Enter City Name");
            binding.edtCity.requestFocus();
        } else if (stateST.isEmpty()) {
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .playOn(binding.edtState);
            binding.edtState.setError("Please Enter State Name");
            binding.edtState.requestFocus();
        } else {

            firstNameRef = database.getReference("users").child(userID).child("userProfile").child("first name");
            DatabaseReference lastNameRef = database.getReference("users").child(userID).child("userProfile").child("last name");
            DatabaseReference phoneNumberRef = database.getReference("users").child(userID).child("userProfile").child("phone number");
            DatabaseReference addressRef = database.getReference("users").child(userID).child("userProfile").child("address");
            DatabaseReference cityRef = database.getReference("users").child(userID).child("userProfile").child("city name");
            DatabaseReference stateRef = database.getReference("users").child(userID).child("userProfile").child("state name");

            //  WRITE TO FIREBASE
            firstNameRef.setValue(firstNameST);
            lastNameRef.setValue(lastNameST);
            phoneNumberRef.setValue(phoneNumberST);
            addressRef.setValue(addressST);
            cityRef.setValue(cityST);
            stateRef.setValue(stateST);

            //  LAUNCH OTHER ACTIVITY WITH CLEAR TASK
            Intent intent = RestoreActivity.getIntent(this)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            finish();

        }
    }

    @Override
    public void onStart() {
        super.onStart();

        firstNameRef = database.getReference("users").child(userID).child("userProfile");
        firstNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("first name")) {

                    String firstNameST = pref.getString(PREF_USER_FIRST_NAME, "error loading").toUpperCase();
                    String lastNameST = pref.getString(PREF_USER_LAST_NAME, "error loading").toUpperCase();
                    String phoneNumberST = pref.getString(PREF_USER_PHONE_NUMBER, "error loading").toUpperCase();
                    String addressST = pref.getString(PREF_USER_ADDRESS, "error loading").toUpperCase();
                    String cityNameST = pref.getString(PREF_USER_CITY, "error loading").toUpperCase();
                    String stateNameST = pref.getString(PREF_USER_STATE, "error loading").toUpperCase();

                    binding.edtFirstName.setText(firstNameST);
                    binding.edtLastName.setText(lastNameST);
                    binding.edtPhoneNumber.setText(phoneNumberST);
                    binding.edtAddress.setText(addressST);
                    binding.edtCity.setText(cityNameST);
                    binding.edtState.setText(stateNameST);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }
}
