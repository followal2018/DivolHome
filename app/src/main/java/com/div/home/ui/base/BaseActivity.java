package com.div.home.ui.base;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.div.home.ApplicationComponent;
import com.div.home.Injector;
import com.div.home.util.Preferences;
import com.followal.base.permissions.Permissions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.disposables.CompositeDisposable;


public class BaseActivity extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public Toolbar toolbar;
    public TextView txtTitle;
    public TextView txtDone;
    public Preferences pref;
    public ImageView imgRightIcon;
    public File mFileTemp;
    protected CompositeDisposable subscriptions = new CompositeDisposable();
    Dialog dialog;
    Uri mImageCaptureUri = null;
    private Toast toast;

    public static void copyStream(InputStream input, OutputStream output) throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resolveDependencies();
        toast = Toast.makeText(getActivity(), "", Toast.LENGTH_LONG);
        pref = new Preferences(getApplicationContext());
    }

    protected ApplicationComponent appComponent() {
        return Injector.INSTANCE.appComponent();
    }

//    protected UserComponent userComponent() {
//        return Injector.INSTANCE.userComponent();
//    }

    public void initState() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        } else {
            mFileTemp = new File(getFilesDir(), System.currentTimeMillis() + ".jpg");
        }
    }

    /**
     * Callback for Subclass to resolve dependencies, called on super.onCreate();
     */
    protected void resolveDependencies() {

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public BaseActivity getActivity() {
        return this;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Permissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        subscriptions.clear();
        super.onDestroy();
    }

}
