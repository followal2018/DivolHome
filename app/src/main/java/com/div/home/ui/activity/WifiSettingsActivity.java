package com.div.home.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;

import androidx.databinding.DataBindingUtil;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.div.home.R;
import com.div.home.databinding.ActivityWifiSettings2Binding;
import com.div.home.ui.base.BaseActivity;
import com.div.home.util.Constants;
import com.div.home.util.GpsUtils;
import com.div.home.util.Utils;
import com.followal.base.utils.PermissionUtils;
import com.followal.base.widget.DTextView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import timber.log.Timber;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class WifiSettingsActivity extends BaseActivity {
    private static final String TAG = WifiSettingsActivity.class.getSimpleName();

    private static final int REQUEST_LOCATION_SETTINGS = 1115;
    private final String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().trim();
    ActivityWifiSettings2Binding binding;
    PermissionUtils permissionUtils;
    boolean isGPS = false;

    public static Intent getIntent(Context context, String roomName){
        Intent intent = new Intent(context, WifiSettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("roomName", roomName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wifi_settings2);
        binding.setActivity(this);
        permissionUtils = new PermissionUtils(this);
        checkLocationPermission();

    }

    public void checkLocationPermission() {
        permissionUtils.setListener(new PermissionUtils.PermissionSettingsListener() {
            @Override
            public void onPermissionGranted(int permission) {
                if (permission == PermissionUtils.PERMISSION_LOCATION) {
                    new GpsUtils(WifiSettingsActivity.this).turnGPSOn(new GpsUtils.onGpsListener() {
                        @Override
                        public void gpsStatus(boolean isGPSEnable) {
                            Timber.e("gpsStatus " + isGPSEnable);
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

    private void getLocation() {
        if (getLocationMode() == 3) {
            displayConnectedWifi();
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
        txtMessage.setText("For Wifi Settings you have to change your location settings. \nPlease Select high accuracy in location settings...");
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

    public void displayConnectedWifi() {
        if (Objects.equals(Utils.getCurrentSSID(this), "unknown ssid") || Utils.getCurrentSSID(this) == null) {
            binding.imgWifi.setImageDrawable(getResources().getDrawable(R.drawable.ic_signal_wifi_off_black_24dp));
            binding.txtSsid.setText("No Wifi Connected");
        } else {
            binding.imgWifi.setImageDrawable(getResources().getDrawable(R.drawable.ic_network_wifi_black_24dp));
            binding.txtSsid.setText(Utils.getCurrentSSID(this));
        }
    }

    public void onClickGoBack() {
        Intent intent = new Intent(WifiSettingsActivity.this, HomeScreenActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void onClickConfigWifi() {
        binding.txtSsid.getText().toString();
        String pass = binding.edtWifiPass.getText().toString();
        if (pass.isEmpty()) {
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .playOn(findViewById(R.id.wifiPassET));

            binding.edtWifiPass.setError("Enter Wifi name please");
            binding.edtWifiPass.requestFocus();
        } else {
            sendAndRequestResponse();
            binding.wifiProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void sendAndRequestResponse() {

        String ssid = Utils.getCurrentSSID(this);
        String pass = binding.edtWifiPass.getText().toString();

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String roomname = Objects.requireNonNull(bundle.getString("roomName")).toLowerCase();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("users").child(uid).child("rooms")
                .child(roomname).child("static").child("wifi");

        myRef.setValue("ok");
        finish();

        /*String url = "http://192.168.4.1/spur?spur=" + ssid + "-" + pass + "-" + uid + "-" + roomname + "-"; // http://192.168.4.1/spur?spur="+ssid+"-"+pass+"-"uid-roomname-

        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("ok")) {
                    myRef.setValue("ok");
                } else {
                    Toast.makeText(WifiSettingsActivity.this, "Error while Configurating WIFI", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getApplicationContext(), "Response :" + response, Toast.LENGTH_LONG).show();   //display the response on screen
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        permissionUtils.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case Constants.GPS_REQUEST:
                if (resultCode == RESULT_OK) {
                    isGPS = true;
                    checkLocationPermission();
                }
                break;

            case REQUEST_LOCATION_SETTINGS:
                if (getLocationMode() == 3) {
                    displayConnectedWifi();
                }
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }
}
