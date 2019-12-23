package com.div.home.ui.activity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.div.home.R;
import com.div.home.databinding.ActivityProfileBinding;
import com.div.home.ui.base.BaseActivity;
import com.div.home.util.Constants;
import com.div.home.util.GpsUtils;
import com.div.home.util.Utils;
import com.followal.base.utils.PermissionUtils;
import com.followal.base.widget.DTextView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

import timber.log.Timber;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static com.div.home.util.Constants.Pref.PREF_USER_ADDRESS;
import static com.div.home.util.Constants.Pref.PREF_USER_CITY;
import static com.div.home.util.Constants.Pref.PREF_USER_FIRST_NAME;
import static com.div.home.util.Constants.Pref.PREF_USER_LAST_NAME;
import static com.div.home.util.Constants.Pref.PREF_USER_PHONE_NUMBER;
import static com.div.home.util.Constants.Pref.PREF_USER_STATE;

public class ProfileActivity extends BaseActivity {

    private static final int REQUEST_LOCATION_SETTINGS = 1115;
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL = 60000; // Every 60 seconds.
    /**
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value, but they may be less frequent.
     */
    private static final long FASTEST_UPDATE_INTERVAL = 30000; // Every 30 seconds
    /**
     * The max time before batched results are delivered by location services. Results may be
     * delivered sooner than this interval.
     */
    private static final long MAX_WAIT_TIME = UPDATE_INTERVAL * 5; // Every 5 minutes.
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().trim();
    ActivityProfileBinding binding;
    PermissionUtils permissionUtils;
    boolean isGPS = false;
    private DatabaseReference firstNameRef;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    LocationCallback locationForCityCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            double longitude = locationResult.getLastLocation().getLongitude();
            double latitude = locationResult.getLastLocation().getLatitude();
            String city = Utils.getCityFromLatLong(ProfileActivity.this, latitude, longitude);
            Timber.e("locationForCityCallback :-> " + latitude + ", " + longitude + "   " + city);
            super.onLocationResult(locationResult);
            Utils.setRequestingLocationUpdates(ProfileActivity.this, false);
            mFusedLocationClient.removeLocationUpdates(locationForCityCallback);
        }
    };

    public static Intent getIntent(Context context) {
        return new Intent(context, ProfileActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        binding.setActivity(this);
        permissionUtils = new PermissionUtils(this);
        checkPermission();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();
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

    public void checkPermission() {
        permissionUtils.setListener(new PermissionUtils.PermissionSettingsListener() {
            @Override
            public void onPermissionGranted(int permission) {
                if (permission == PermissionUtils.PERMISSION_LOCATION) {
                    new GpsUtils(ProfileActivity.this).turnGPSOn(new GpsUtils.onGpsListener() {
                        @Override
                        public void gpsStatus(boolean isGPSEnable) {
                            Timber.e("gpsStatus " + isGPSEnable);
                            // turn on GPS
                            isGPS = isGPSEnable;
                            if (isGPS) {
                                getLocation();
                            }
                        }
                    });
                }
            }

            @Override
            public void onPermissionDenied(int permission) {

            }
        });
        permissionUtils.requestPermission(PermissionUtils.PERMISSION_LOCATION);
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setMaxWaitTime(MAX_WAIT_TIME);
    }

    public void requestLocationUpdates(View view) {
        try {
            Utils.setRequestingLocationUpdates(this, true);
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, locationForCityCallback, null);
        } catch (SecurityException e) {
            Utils.setRequestingLocationUpdates(this, false);
            e.printStackTrace();
        }
    }

    public void removeLocationUpdates(View view) {
        Utils.setRequestingLocationUpdates(this, false);
        mFusedLocationClient.removeLocationUpdates(getPendingIntent());
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, LocationUpdatesBroadcastReceiver.class);
        intent.setAction(LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        permissionUtils.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case Constants.GPS_REQUEST:
                if (resultCode == RESULT_OK) {
                    isGPS = true;
                    if (isGPS) {
                        checkPermission();
                    }
                }
                break;

            case REQUEST_LOCATION_SETTINGS:
                if (getLocationMode() == 3) {
                    initGoogleApiClientForCityLocation();
                }
                break;
        }
    }

    public synchronized void initGoogleApiClientForCityLocation() {
        requestLocationUpdates(null);
        Timber.e("connect ");
    }

    private void getLocation() {
        if (getLocationMode() == 3) {
            initGoogleApiClientForCityLocation();
        } else {
            runOnUiThread(() -> {
                showChangeSettingsDialog();
            });
        }
    }

    public int getLocationMode() {
        try {
            return Settings.Secure.getInt(this.getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void showChangeSettingsDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_location_settings);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        DTextView txtMessage = dialog.findViewById(R.id.txtMessage);
        txtMessage.setText("For Complete login you have to change your location settings. \nPlease Select high accuracy in location settings...");
        DTextView txtPositive = dialog.findViewById(R.id.txtPositive);
        txtPositive.setText("Go to settings");

        DTextView txtNegative = dialog.findViewById(R.id.txtNegative);
        txtNegative.setVisibility(View.GONE);
        txtPositive.setOnClickListener(v -> {
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_LOCATION_SETTINGS);
            dialog.dismiss();
        });

        txtNegative.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    public class LocationUpdatesBroadcastReceiver extends BroadcastReceiver {
        static final String ACTION_PROCESS_UPDATES =
                "com.google.android.gms.location.sample.locationupdatespendingintent.action" +
                        ".PROCESS_UPDATES";
        private static final String TAG = "LUBroadcastReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                final String action = intent.getAction();
                if (ACTION_PROCESS_UPDATES.equals(action)) {
                    LocationResult result = LocationResult.extractResult(intent);
                    if (result != null) {
                        List<Location> locations = result.getLocations();
                        Utils.setLocationUpdatesResult(context, locations);
                        Utils.sendNotification(context, Utils.getLocationResultTitle(context, locations));
                        Log.i(TAG, Utils.getLocationUpdatesResult(context));
                    }
                }
            }
        }
    }
}
