package com.followal.base.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.followal.base.R;
import com.followal.base.widget.DTextView;
import com.tbruyelle.rxpermissions2.RxPermissions;


/**
 * Created by Nirav Mandani on 20-07-2019.
 * Followal Solutions
 */
public class PermissionUtils {

    public static final int PERMISSION_SMS = 1;
    public static final int PERMISSION_CAMERA_PICTURE = 2;
    public static final int PERMISSION_CAMERA_VIDEO = 3;
    public static final int PERMISSION_GALLERY = 4;
    public static final int PERMISSION_LOCATION = 5;
    public static final int PERMISSION_READ_CONTACTS = 6;
    public static final int PERMISSION_MICROPHONE = 7;
    public static final int PERMISSION_BASIC = 8;
    public static final int PERMISSION_CAMERA_QR_CODE_SCAN = 9;
    public static final int PERMISSION_CALENDER = 10;
    public static final int PERMISSION_READ_PHONE_STATE = 12;
    public static final int PERMISSION_CALL = 13;

    private static final int REQUEST_CAMERA_PICTURE_PERMISSION_SETTING = 1501;
    private static final int REQUEST_CAMERA_VIDEO_PERMISSION_SETTING = 1502;
    private static final int REQUEST_LOCATION_PERMISSION_SETTING = 1503;
    private static final int REQUEST_READ_CONTACTS_PERMISSION_SETTING = 1504;
    private static final int REQUEST_MICROPHONE_PERMISSION_SETTING = 1505;
    private static final int REQUEST_BASIC_PERMISSIONS_SETTING = 1506;
    private static final int REQUEST_GALLERY_PERMISSION_SETTING = 1507;
    private static final int REQUEST_CAMERA_QR_CODE_SCAN_PERMISSION_SETTING = 1508;
    private static final int REQUEST_CALENDER_PERMISSION_SETTING = 1509;
    private static final int REQUEST_READ_PHONE_STATE_PERMISSION_SETTING = 1511;
    private static final int REQUEST_CALL_PERMISSION_SETTING = 1511;

    PermissionSettingsListener listener;
    private Context context;
    private FragmentActivity activity;
    private Fragment fragment;

    public PermissionUtils(FragmentActivity activity) {
        this.activity = activity;
        this.context = activity;
        listener = PermissionSettingsListener.EMPTY;
    }

    public PermissionUtils(Fragment fragment) {
        this.fragment = fragment;
        this.activity = fragment.getActivity();
        this.context = fragment.getActivity();
        listener = PermissionSettingsListener.EMPTY;
    }

    public PermissionUtils(Application context) {
        this.context = context;
        listener = PermissionSettingsListener.EMPTY;
    }

    public static boolean checkStoragePermission(Context context) {
        return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED);
    }

    public void setListener(PermissionSettingsListener listener) {
        this.listener = listener;
    }

    @SuppressLint("NewApi")
    public void requestSMSPermission() {
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(Manifest.permission.RECEIVE_SMS)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M
                        listener.onPermissionGranted(PERMISSION_SMS);
                    } else {
                        if (activity == null && fragment == null) return;
                        Activity activity = this.activity != null ? this.activity : fragment.getActivity();
                        boolean showRationale = activity.shouldShowRequestPermissionRationale(Manifest.permission.RECEIVE_SMS);
                        if (!showRationale) {
                            AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                                    .setMessage(R.string.msg_otp_detection_disabled)
                                    .setPositiveButton(R.string.ok, (dialog, which) -> {
                                        listener.onPermissionGranted(PERMISSION_SMS);
                                    })
                                    .create();
                            alertDialog.setOnCancelListener(dialog -> {
                                listener.onPermissionDenied(PERMISSION_SMS);
                            });
                            alertDialog.show();
                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                                    .setTitle(R.string.title_permission_denied)
                                    .setMessage(R.string.msg_sms_permission_denied)
                                    .setNegativeButton(R.string.btn_i_am_sure, null)
                                    .setPositiveButton(R.string.btn_retry, (dialog1, which1) -> {
                                        rxPermissions.request(Manifest.permission.RECEIVE_SMS)
                                                .subscribe(granted1 -> {
                                                    if (granted1) {
                                                        listener.onPermissionGranted(PERMISSION_SMS);
                                                    } else {
                                                        listener.onPermissionDenied(PERMISSION_SMS);
                                                    }
                                                });
                                    })
                                    .create();
                            alertDialog.setOnCancelListener(dialog -> {
                                listener.onPermissionDenied(PERMISSION_SMS);
                            });
                            alertDialog.show();
                        }
                    }
                });
    }

    public void requestPermission(int permission) {
        switch (permission) {
            case PERMISSION_BASIC:
                requestPermissions(PERMISSION_BASIC, R.string.msg_basic_permissions,
                        R.string.msg_basic_permissions_denied,
                        REQUEST_BASIC_PERMISSIONS_SETTING, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;
            case PERMISSION_CAMERA_PICTURE:
                requestPermissions(PERMISSION_CAMERA_PICTURE, R.string.msg_camera_picture_permission,
                        R.string.msg_camera_picture_permission_denied,
                        REQUEST_CAMERA_PICTURE_PERMISSION_SETTING, Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;
            case PERMISSION_CAMERA_VIDEO:
                requestPermissions(PERMISSION_CAMERA_VIDEO, R.string.msg_camera_video_permission,
                        R.string.msg_camera_video_permission_denied,
                        REQUEST_CAMERA_VIDEO_PERMISSION_SETTING, Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO);
                break;
            case PERMISSION_GALLERY:
                requestPermissions(PERMISSION_GALLERY, R.string.msg_gallery_permission,
                        R.string.msg_gallery_permission_denied,
                        REQUEST_GALLERY_PERMISSION_SETTING, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;
            case PERMISSION_LOCATION:
                requestPermissions(PERMISSION_LOCATION, R.string.msg_location_permission,
                        R.string.msg_location_permission_denied,
                        REQUEST_LOCATION_PERMISSION_SETTING, Manifest.permission.ACCESS_FINE_LOCATION);
                break;
            case PERMISSION_READ_CONTACTS:
                requestPermissions(PERMISSION_READ_CONTACTS, R.string.msg_contacts_permission,
                        R.string.msg_contacts_permission_denied,
                        REQUEST_READ_CONTACTS_PERMISSION_SETTING, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS);
                break;
            case PERMISSION_MICROPHONE:
                requestPermissions(PERMISSION_MICROPHONE, R.string.msg_microphone_permission,
                        R.string.msg_microphone_permission_denied,
                        REQUEST_MICROPHONE_PERMISSION_SETTING, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO);
                break;
            case PERMISSION_CAMERA_QR_CODE_SCAN:
                requestPermissions(PERMISSION_CAMERA_QR_CODE_SCAN, R.string.msg_qr_scan_permission,
                        R.string.msg_qr_scan_permission_denied,
                        REQUEST_CAMERA_QR_CODE_SCAN_PERMISSION_SETTING, Manifest.permission.CAMERA);
                break;
            case PERMISSION_CALENDER:
                requestPermissions(PERMISSION_CALENDER, R.string.msg_calender_permission,
                        R.string.msg_calender_permission_denied, REQUEST_CALENDER_PERMISSION_SETTING,
                        Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR);
                break;
            case PERMISSION_READ_PHONE_STATE:
                requestPermissions(PERMISSION_READ_PHONE_STATE, R.string.msg_calender_permission,
                        R.string.msg_calender_permission_denied, REQUEST_READ_PHONE_STATE_PERMISSION_SETTING,
                        Manifest.permission.READ_PHONE_STATE);
                break;
            case PERMISSION_CALL:
                requestPermissions(PERMISSION_CALL, R.string.msg_calender_permission,
                        R.string.msg_calender_permission_denied, REQUEST_CALL_PERMISSION_SETTING,
                        Manifest.permission.CALL_PHONE);
                break;
        }
    }

    @SuppressLint({"NewApi", "CheckResult"})
    private void requestPermissions(int permission, int messageRequest, int messageOnDenied,
                                    int requestCode, String... permissions) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(permissions)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M
                        listener.onPermissionGranted(permission);
                    } else {
                        if (activity == null && fragment == null) return;
                        Activity activity = this.activity != null ? this.activity : fragment.getActivity();
                        boolean showRationale = false;
                        for (String p : permissions) {
                            showRationale = activity.shouldShowRequestPermissionRationale(p);
                            if (showRationale) break;
                        }
                        if (!showRationale) {
                            final boolean[] openSettings = {false};

                            Dialog dialog = new Dialog(context);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.layout_alert_dialog);

                            DTextView txtTitle = dialog.findViewById(R.id.txtTitle);
                            txtTitle.setVisibility(View.GONE);
                            DTextView txtMessage = dialog.findViewById(R.id.txtMessage);
                            DTextView txtPositive = dialog.findViewById(R.id.txtPositive);
                            DTextView txtNegative = dialog.findViewById(R.id.txtNegative);
                            txtTitle.setText(context.getString(R.string.title_permission_denied));
                            txtMessage.setText(messageRequest);
                            txtPositive.setText(context.getString(R.string.action_yes));
                            txtPositive.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    openSettings[0] = true;
                                    openAppPermissionSettings(requestCode);
                                }
                            });

                            txtNegative.setText(context.getString(R.string.action_no));
                            txtNegative.setOnClickListener(view -> {
                                dialog.dismiss();
                                if (!openSettings[0]) {
                                    listener.onPermissionDenied(permission);
                                }
                            });

                            dialog.show();
                        } else {

                            final boolean[] retry = {false};
                            Dialog dialog = new Dialog(context);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.layout_alert_dialog);

                            DTextView txtTitle = dialog.findViewById(R.id.txtTitle);
                            DTextView txtMessage = dialog.findViewById(R.id.txtMessage);
                            DTextView txtPositive = dialog.findViewById(R.id.txtPositive);
                            DTextView txtNegative = dialog.findViewById(R.id.txtNegative);
                            txtTitle.setText(context.getString(R.string.title_permission_denied));
                            txtMessage.setText(messageOnDenied);
                            txtPositive.setText(context.getString(R.string.btn_retry));
                            txtPositive.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    retry[0] = true;
                                    rxPermissions.request(permissions)
                                            .subscribe(granted1 -> {
                                                if (granted1) {
                                                    listener.onPermissionGranted(permission);
                                                } else {
                                                    listener.onPermissionDenied(permission);
                                                }
                                            });
                                }
                            });

                            txtNegative.setText(context.getString(R.string.btn_i_am_sure));
                            txtNegative.setOnClickListener(view -> {
                                dialog.dismiss();
                                if (!retry[0]) {
                                    listener.onPermissionDenied(permission);
                                }
                            });

                            dialog.show();
                        }
                    }
                }, Throwable::printStackTrace);
    }

    private void openAppPermissionSettings(int requestCode) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        if (activity != null) {
            activity.startActivityForResult(intent, requestCode);
        } else if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_BASIC_PERMISSIONS_SETTING && checkWriteStoragePermission()) {
            listener.onPermissionGranted(PERMISSION_BASIC);
        } else if (requestCode == REQUEST_CAMERA_PICTURE_PERMISSION_SETTING && checkCameraPermission() &&
                checkWriteStoragePermission()) {
            listener.onPermissionGranted(PERMISSION_CAMERA_PICTURE);

        } else if (requestCode == REQUEST_CAMERA_VIDEO_PERMISSION_SETTING && checkCameraPermission() &&
                checkWriteStoragePermission() && checkMicrophonePermission()) {
            listener.onPermissionGranted(PERMISSION_CAMERA_VIDEO);

        } else if (requestCode == REQUEST_GALLERY_PERMISSION_SETTING && checkWriteStoragePermission()) {
            listener.onPermissionGranted(PERMISSION_GALLERY);

        } else if (requestCode == REQUEST_LOCATION_PERMISSION_SETTING && checkLocationPermission()) {
            listener.onPermissionGranted(PERMISSION_LOCATION);

        } else if (requestCode == REQUEST_READ_CONTACTS_PERMISSION_SETTING && checkReadContactsPermission()) {
            listener.onPermissionGranted(PERMISSION_READ_CONTACTS);

        } else if (requestCode == REQUEST_MICROPHONE_PERMISSION_SETTING && checkMicrophonePermission() && checkWriteStoragePermission()) {
            listener.onPermissionGranted(PERMISSION_MICROPHONE);

        } else if (requestCode == REQUEST_CAMERA_QR_CODE_SCAN_PERMISSION_SETTING && checkCameraPermission()) {
            listener.onPermissionGranted(PERMISSION_CAMERA_QR_CODE_SCAN);

        } else if (requestCode == REQUEST_CALENDER_PERMISSION_SETTING && checkCelenderReadPermission() && checkCelenderPermission()) {
            listener.onPermissionGranted(PERMISSION_CALENDER);

        } else if (requestCode == REQUEST_READ_PHONE_STATE_PERMISSION_SETTING && checkReadPhoneStatePermission()) {
            listener.onPermissionGranted(PERMISSION_READ_PHONE_STATE);
        } else if (requestCode == REQUEST_CALL_PERMISSION_SETTING && checkCallPermission()) {
            listener.onPermissionGranted(PERMISSION_CALL);
        }
    }

    public boolean checkCameraPermission() {
        return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(context,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED);
    }

    public boolean checkWriteStoragePermission() {
        return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED);
    }

    public boolean checkReadStoragePermission() {
        return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(context,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED);
    }

    public boolean checkLocationPermission() {
        return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED);
    }

    public boolean checkReadContactsPermission() {
        return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(context,
                        Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED);
    }

    public boolean checkMicrophonePermission() {
        return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(context,
                        Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED);
    }

    public boolean checkCelenderReadPermission() {
        return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(context,
                        Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED);
    }

    public boolean checkCelenderPermission() {
        return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED);
    }

    public boolean checkReadPhoneStatePermission() {
        return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(context,
                        Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED);
    }

    public boolean checkCallPermission() {
        return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(context,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED);
    }

    public interface PermissionSettingsListener {
        PermissionSettingsListener EMPTY = new PermissionSettingsListener() {
            @Override
            public void onPermissionGranted(int permission) {

            }

            @Override
            public void onPermissionDenied(int permission) {

            }
        };

        void onPermissionGranted(int permission);

        void onPermissionDenied(int permission);
    }
}
