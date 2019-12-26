package com.div.home.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.div.home.R;
import com.div.home.databinding.ActivityHomeScreenBinding;
import com.div.home.databinding.DialogDeleteBinding;
import com.div.home.model.WeatherResponse;
import com.div.home.ui.adapter.RoomsAdapter;
import com.div.home.ui.base.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import timber.log.Timber;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static com.div.home.util.Constants.Pref.PREF_USER_CITY;
import static com.div.home.util.Constants.Pref.PREF_USER_FIRST_NAME;
import static com.div.home.util.Constants.Pref.PREF_USER_STATE;

public class HomeScreenActivity extends BaseActivity implements RoomsAdapter.ItemClickListener {
    private static final String TAG = HomeScreenActivity.class.getSimpleName();

    private final ArrayList<String> contain = new ArrayList<>();
    private final String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().trim();

    ActivityHomeScreenBinding binding;
    RoomsAdapter adapter;
    private Boolean check = false;

    public static Intent getIntent(Context context) {
        return new Intent(context, HomeScreenActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen);
        binding.setActivity(this);

        String firstName = pref.getString(PREF_USER_FIRST_NAME, "USER NAME").toUpperCase();
        if (firstName.equals("USER NAME")) {
            Intent intent = new Intent(HomeScreenActivity.this, RestoreActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            binding.txtNotification.setText(String.format("WELCOME %s", firstName));
            getWhetherInfo();
        }
    }

    public void onClickSetting() {
        startActivity(SettingsActivity.getIntent(this));
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    public void onClickAddRoom() {
        startActivity(BuyPageActivity.getIntent(this));
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    public void onClickAddFamily() {
        startActivity(FamilyActivity.getIntent(this));
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    public void onClickTimer() {
        startActivity(ScheduleListActivity.getIntent(this));
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    public void onClickAI() {
        startActivity(WebUrlViewActivity.getIntent(this));
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    public void onClickSharedRooms() {
        startActivity(SharedFromUsersActivity.getIntent(this));
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!check) {
            fetchRooms();
            check = true;
        }
    }

    private void fetchRooms() {

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mGetReference = mDatabase.getReference("users").child(userId).child("rooms");
        mGetReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                binding.rvRooms.setVisibility(View.VISIBLE);
                binding.flProgressLayout.setVisibility(View.GONE);
                contain.add(String.valueOf(dataSnapshot.getKey()).toUpperCase());
                binding.rvRooms.setLayoutManager(new LinearLayoutManager(HomeScreenActivity.this));
                adapter = new RoomsAdapter(HomeScreenActivity.this, contain);
                adapter.setClickListener(HomeScreenActivity.this);
                binding.rvRooms.setAdapter(adapter);
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
                Log.e(TAG, "Exception: " + databaseError);
            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    @Override
    public void onItemClick(int position) {
        String title1 = ((TextView) Objects.requireNonNull(binding.rvRooms.findViewHolderForAdapterPosition(position))
                .itemView.findViewById(R.id.roomNameHomeScreenTV)).getText().toString().toLowerCase();

        Intent intent1 = MagicRoomActivity.getIntent(this, title1, userId)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent1);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);

    }

    @Override
    public void longClickListener(View view, int position) {

        final String title1 = ((TextView) Objects.requireNonNull(binding.rvRooms.findViewHolderForAdapterPosition(position))
                .itemView.findViewById(R.id.roomNameHomeScreenTV)).getText().toString().toUpperCase();

        DialogDeleteBinding dialogDeleteBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_delete, null, false);
        dialogDeleteBinding.txtRoomName.setText(String.format("%s ?", title1));
        final PopupWindow popupWindow = new PopupWindow(dialogDeleteBinding.getRoot(), LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        dialogDeleteBinding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roomName = title1.toLowerCase();
                checkRoomIsShared(roomName);
            }
        });
        dialogDeleteBinding.btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(dialogDeleteBinding.getRoot(), Gravity.CENTER, 0, 0);
        popupWindow.setOutsideTouchable(false);
        YoYo.with(Techniques.Landing)
                .duration(200)
                .playOn(dialogDeleteBinding.getRoot());
    }


    public void checkRoomIsShared(String roomName) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("rooms")
                .child(roomName).child("sharedTo");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    Map<String, Object> userMap = (Map<String, Object>) dataSnapshot.getValue();
                    for (String shareToUserId : userMap.keySet()) {
                        removeRoomFromShareToUser(shareToUserId, roomName);
                    }
                    deleteRoom(roomName);
                } else {
                    deleteRoom(roomName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void removeRoomFromShareToUser(String userToId, String roomName) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("sharedTo").child(userToId);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> roomsMap = (Map<String, Object>) dataSnapshot.getValue();
                if (roomsMap.size() == 1) {
                    //delete user and Delete from Other User's ShareFrom
                    deleteUserFromShareToUser(userToId, roomName);
                    deleteUserFromShareFromUser(userToId, roomName);
                } else {
                    // remove only selected Room
                    removeRoomFromShareTo(userToId, roomName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void deleteRoom(String roomName) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(userId).child("rooms").child(roomName);
        ref.removeValue();
        finish();
        startActivity(getIntent());
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    public void deleteUserFromShareToUser(String userToId, String roomName) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("sharedTo").child(userToId);
        myRef.removeValue();
    }

    public void deleteUserFromShareFromUser(String userToId, String roomName) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users").child(userToId).child("sharedFrom").child(userId);
        myRef.removeValue();
    }

    public void removeRoomFromShareTo(String userToId, String roomName) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("sharedTo").child(userToId).child(roomName);
        myRef.removeValue();
    }

    public void getWhetherInfo() {
        RequestAPI requestAPI = ApiClient.getClient().create(RequestAPI.class);
        requestAPI.getWeather(String.format("%s,%s", pref.getString(PREF_USER_CITY, "null"), pref.getString(PREF_USER_STATE, "null")), "metric", "a640d4ae4d22ad515ac39bf60854c64d").enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NotNull Call<WeatherResponse> call, @NotNull Response<WeatherResponse> serverResponse) {
                WeatherResponse response = serverResponse.body();
                binding.txtDisplayTemp.setText(String.format("%s°C", response != null ? response.getMain().getTemp() : ""));
                binding.txtCityState.setText(String.format("%s,%s", pref.getString(PREF_USER_CITY, "null"), pref.getString(PREF_USER_STATE, "null")));
            }

            @Override
            public void onFailure(@NotNull Call<WeatherResponse> call, @NotNull Throwable t) {
                Timber.e("onServiceFailure: %s", t.getLocalizedMessage());
            }
        });
    }

    interface RequestAPI {
        @GET("weather")
        Call<WeatherResponse> getWeather(@Query("q") String city, @Query("units") String units, @Query("appid") String appId);
    }

    static class ApiClient {
        static Retrofit retrofit = null;

        static Retrofit getClient() {
            final String BaseUrl = "https://api.openweathermap.org/data/2.5/";
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES).addInterceptor(logging)
                    .build();
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BaseUrl)
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }
    }
}
