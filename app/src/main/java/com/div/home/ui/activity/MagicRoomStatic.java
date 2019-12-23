package com.div.home.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.div.home.R;
import com.div.home.ui.adapter.ApplianceAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MagicRoomStatic extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, ApplianceAdapter.ItemClickListener {

    private TextView roomName;
    private Button applianceOne;
    private Button applianceTwo;
    private Button applianceThree;
    private Button applianceFour;
    private Boolean app1;
    private Boolean app2;
    private Boolean app3;
    private Boolean app4;
    private static final String TAG = MagicRoomStatic.class.getName();
    private String ExistingSpeed;
    private int workingSpeed ;
    private final String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

    public static Intent getIntent(Context context, String message) {
        Intent intent = new Intent(context, MagicRoomStatic.class);
        intent.putExtra("message", message);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magic_room_static);

        roomName = findViewById(R.id.roomNameStatic);
        applianceOne = findViewById(R.id.applianceOneBT);
        applianceTwo = findViewById(R.id.applianceTwoBT);
        applianceThree = findViewById(R.id.applianceThreeBT);
        applianceFour = findViewById(R.id.applianceFourBT);

        applianceOne.setOnClickListener(this);
        applianceTwo.setOnClickListener(this);
        applianceThree.setOnClickListener(this);
        applianceFour.setOnClickListener(this);
        applianceFour.setOnLongClickListener(this);

    }

    public void onClickAddAppliance() {
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String RoomNameSTR = bundle.getString("message");
        Intent k = AddApplianceActivity.getIntent(this)
                .setFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_CLEAR_TOP);
        k.putExtra("RoomNameSTR", RoomNameSTR);
        startActivity(k);
    }

    public void onClickSyncDevice(){
        Intent l = new Intent(this, WebUrlViewActivity.class)
                .setFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(l);
    }

    public void onClickTimer(){
        Toast.makeText(this, "Comming Soon!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        wifiChecker();
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String roomname = Objects.requireNonNull(bundle.getString("message")).toUpperCase();
        roomName.setText(roomname);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(userId).child("rooms").child(roomname.toLowerCase()).child("appliances");

        myRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String appOne, appTwo, appThree, appFour;

                appOne = Objects.requireNonNull(dataSnapshot.child("appliance one").getValue()).toString();
                appTwo = Objects.requireNonNull(dataSnapshot.child("appliance two").getValue()).toString();
                appThree = Objects.requireNonNull(dataSnapshot.child("appliance three").getValue()).toString();
                appFour = Objects.requireNonNull(dataSnapshot.child("appliance four").getValue()).toString();

                ExistingSpeed =  Objects.requireNonNull(Objects.requireNonNull(dataSnapshot.child("fan speed").getValue()).toString());
                try {
                    workingSpeed = Integer.parseInt(ExistingSpeed);
                } catch(NumberFormatException ignored) {

                }

                if (appOne.equals("0")) {

                    app1 = false;

                } else if (appOne.equals("1")) {

                    app1 = true;

                }

                if (appTwo.equals("0")) {

                    app2 = false;

                } else if (appTwo.equals("1")) {

                    app2 = true;

                }

                if (appThree.equals("0")) {

                    app3 = false;

                } else if (appThree.equals("1")) {

                    app3 = true;

                }
                if (appFour.equals("0")) {

                    app4 = false;

                } else if (appFour.equals("1")) {

                    app4 = true;

                }

                buttonSetter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void buttonSetter() {

        if (app1) {

            applianceOne.setBackgroundTintList(getResources().getColorStateList(R.color.appliance_on));

        } else {

            applianceOne.setBackgroundTintList(getResources().getColorStateList(R.color.appliance_off));
        }

        if (app2) {

            applianceTwo.setBackgroundTintList(getResources().getColorStateList(R.color.appliance_on));

        } else {

            applianceTwo.setBackgroundTintList(getResources().getColorStateList(R.color.appliance_off));
        }
        if (app3) {

            applianceThree.setBackgroundTintList(getResources().getColorStateList(R.color.appliance_on));

        } else {

            applianceThree.setBackgroundTintList(getResources().getColorStateList(R.color.appliance_off));
        }

        if (app4) {

            applianceFour.setBackgroundTintList(getResources().getColorStateList(R.color.appliance_on));

        } else {

            applianceFour.setBackgroundTintList(getResources().getColorStateList(R.color.appliance_off));
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;

        String roomname = Objects.requireNonNull(bundle.getString("message")).toLowerCase();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(userId).child("rooms").child(roomname).child("appliances");

        switch (v.getId()) {

            case R.id.applianceOneBT:

                if (app1) {

                    myRef.child("appliance one").setValue(0);
                    applianceOne.setBackgroundTintList(getResources().getColorStateList(R.color.appliance_on));

                } else {

                    myRef.child("appliance one").setValue(1);
                    applianceOne.setBackgroundTintList(getResources().getColorStateList(R.color.appliance_off));

                }
                break;

            case R.id.applianceTwoBT:

                if (app2) {

                    myRef.child("appliance two").setValue(0);
                    applianceTwo.setBackgroundTintList(getResources().getColorStateList(R.color.appliance_on));

                } else {

                    myRef.child("appliance two").setValue(1);
                    applianceTwo.setBackgroundTintList(getResources().getColorStateList(R.color.appliance_off));

                }
                break;

            case R.id.applianceThreeBT:

                if (app3) {

                    myRef.child("appliance three").setValue(0);
                    applianceThree.setBackgroundTintList(getResources().getColorStateList(R.color.appliance_on));

                } else {

                    myRef.child("appliance three").setValue(1);
                    applianceThree.setBackgroundTintList(getResources().getColorStateList(R.color.appliance_off));

                }
                break;
            case R.id.applianceFourBT:

                if (app4) {

                    myRef.child("appliance four").setValue(0);
                    applianceFour.setBackgroundTintList(getResources().getColorStateList(R.color.appliance_on));

                } else {

                    myRef.child("appliance four").setValue(1);
                    applianceFour.setBackgroundTintList(getResources().getColorStateList(R.color.appliance_off));

                }
                break;

        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onLongClick(View longView) {

        if (longView.getId() == R.id.applianceFourBT) {
            if (app4) {
                // DEFINE FIREBASE DATABASE
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference flash;
                String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().trim();

                Bundle bundle = getIntent().getExtras();
                assert bundle != null;
                String roomname = Objects.requireNonNull(bundle.getString("message")).toLowerCase();

                flash = database.getReference("users").child(userID).child("rooms").child(roomname).child("appliances").child("fan speed");

                final TextView speedText;
                Button plus, minus;

                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                assert inflater != null;
                @SuppressLint("InflateParams") View popupView = inflater.inflate(R.layout.popup_appliance_settings, null);

                speedText = popupView.findViewById(R.id.showSpeedTV);
                plus = popupView.findViewById(R.id.plusSpeedBT);
                minus = popupView.findViewById(R.id.minusSpeedBT);

                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                popupWindow.showAtLocation(longView, Gravity.CENTER, 0, 0);
                popupWindow.setOutsideTouchable(false);

                speedText.setText("0" + ExistingSpeed);

                minus.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {

                        int changer = workingSpeed;

                        if (workingSpeed == 1) {
                            Toast.makeText(MagicRoomStatic.this, "Already at lowest speed", Toast.LENGTH_SHORT).show();
                        } else {
                            speedText.setText("0" + workingSpeed);
                            flash.setValue(workingSpeed - 1);
                            workingSpeed = changer - 1;
                            String setter = String.valueOf(workingSpeed);
                            speedText.setText("0" + setter);
                        }

                    }
                });

                plus.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {

                        int changer = workingSpeed;

                        if (workingSpeed == 5) {
                            Toast.makeText(MagicRoomStatic.this, "Already at max speed", Toast.LENGTH_SHORT).show();
                        } else {
                            speedText.setText("0" + workingSpeed);
                            flash.setValue(workingSpeed + 1);
                            workingSpeed = changer + 1;
                            String setter = String.valueOf(workingSpeed);
                            speedText.setText("0" + setter);
                        }

                    }
                });

            } else {
                Toast.makeText(this, "Turn on Appliance 4 first!", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    private void wifiChecker(){

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String roomname = Objects.requireNonNull(bundle.getString("message")).toLowerCase();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef =  database.getReference("users").child(userId).child("rooms")
                .child(roomname).child("appliances");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                if (dataSnapshot.hasChild("wifi")){

                    String wifiState = Objects.requireNonNull(dataSnapshot.child("wifi").getValue()).toString();

                    if(wifiState.equals("null") || wifiState.isEmpty()){

                        Intent intent = new Intent(MagicRoomStatic.this, WifiSettings.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("roomName", roomName.getText().toString());
                        startActivity(intent);

                    }
                    else{

                        if(dataSnapshot.hasChild("appliance one")){
                            Toast.makeText(MagicRoomStatic.this, "Ready!", Toast.LENGTH_SHORT).show();

                        } else {
                            //Open Add Appliance Screen
                        }
                    }

                }
                else{

                    myRef.child("wifi").setValue("null");
                    Intent intent = new Intent(MagicRoomStatic.this, WifiSettings.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("roomName", roomName.getText().toString());
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value

                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    @Override
    public void onItemClick() {

    }
}
