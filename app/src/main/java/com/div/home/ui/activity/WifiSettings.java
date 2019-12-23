package com.div.home.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.div.home.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;


@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class WifiSettings extends AppCompatActivity {



    private final int LOCATION_PERMISSION = 1;


    private TextView wifiNames;
    private EditText wifiPass;
    private ProgressBar progressBar;
    private static final String TAG = WifiSettings.class.getName();

    private final String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().trim();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_settings);

        wifiNames = findViewById(R.id.ssidTV);

        ImageButton proceed = findViewById(R.id.configWifiIB);
        progressBar = findViewById(R.id.wifiProgressBar);
        wifiPass = findViewById(R.id.wifiPassET);
        Button back = findViewById(R.id.goBackBT);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WifiSettings.this, HomeScreenActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });

            proceed.setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("ResultOfMethodCallIgnored")
                @Override
                public void onClick(View v) {


                    wifiNames.getText().toString();
                    String pass = wifiPass.getText().toString();

                    if (pass.isEmpty()){

                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(findViewById(R.id.wifiPassET));

                        wifiPass.setError("Enter Wifi name please");
                        wifiPass.requestFocus();

                    }
                    else {

                        sendAndRequestResponse();
                        progressBar.setVisibility(View.VISIBLE);


                    }

                }
            });



    }


    private static String getCurrentSSID(Context context){

        String ssid =null;


        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null){

            return  null;

        }

        if (networkInfo.isConnected()){
            final WifiManager wifiManager = (WifiManager)
                    context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            assert wifiManager != null;
            final WifiInfo wifiInfo = wifiManager.getConnectionInfo();

            if (wifiInfo!=null && !TextUtils.isEmpty(wifiInfo.getSSID())){
               ssid = wifiInfo.getSSID();
            }

        }

        assert ssid != null;
        ssid = ssid.substring(1, ssid.length()-1);
        return  ssid;
    }



    private void sendAndRequestResponse() {



        String ssid = getCurrentSSID(this);
        String pass = wifiPass.getText().toString();

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String roomname = Objects.requireNonNull(bundle.getString("roomName")).toLowerCase();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef =  database.getReference("users").child(uid).child("rooms")
                .child(roomname).child("appliances").child("wifi");


        String url = "http://192.168.4.1/spur?spur="+ssid+"-"+pass+"-"+uid+"-"+roomname+"-"; // http://192.168.4.1/spur?spur="+ssid+"-"+pass+"-"uid-roomname-

        //RequestQueue initialized
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        //display the response on screen
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("ok")){



                    myRef.setValue("ok");


                }

                else {

                    Toast.makeText(WifiSettings.this, "Error while Configurating WIFI", Toast.LENGTH_SHORT).show();
                }


                Toast.makeText(getApplicationContext(), "Response :" + response, Toast.LENGTH_LONG).show();   //display the response on screen



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i(TAG, "Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();

        TextView heading = findViewById(R.id.wifiTextHeadingTV);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String roomname = Objects.requireNonNull(Objects.requireNonNull(bundle.getString("roomName")).toUpperCase());
        heading.setText("Wifi Configuration For "+roomname);

        ImageView wifiImage = findViewById(R.id.wifiIV);

        if(ContextCompat.checkSelfPermission
                (WifiSettings.this, Manifest.permission.
                        ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){

            if (Objects.equals(getCurrentSSID(this), "unknown ssid") || getCurrentSSID(this) == null){

                wifiImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_signal_wifi_off_black_24dp));
                wifiNames.setText("No Wifi Connected");



            }
            else {

                wifiNames.setText(getCurrentSSID(this));


            }

        }


        else {
            requestLocationPermission();

        }

    }


    private void requestLocationPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)){

                new AlertDialog.Builder(this).setTitle("Permission Needed!").setMessage("Divol Home needs Location Permission to Work Properly")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ActivityCompat.requestPermissions(WifiSettings.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},LOCATION_PERMISSION);


                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                }).create().show();

        }
        else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},LOCATION_PERMISSION);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        ImageView wifiImage = findViewById(R.id.wifiIV);

        if(requestCode==LOCATION_PERMISSION){

            if (grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED) {

                if (Objects.equals(getCurrentSSID(this), "unknown ssid") || getCurrentSSID(this) == null){
                    wifiImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_signal_wifi_off_black_24dp));

                    wifiNames.setText("No Wifi Connected");



                }
                else {

                    wifiNames.setText(getCurrentSSID(this));


                }

            }

        }
        else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }

    }
}
