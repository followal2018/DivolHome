package com.div.home.ui.base;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.div.home.ApplicationComponent;
import com.div.home.Injector;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.disposables.CompositeDisposable;

public class BaseFragment extends Fragment {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    protected CompositeDisposable subscriptions = new CompositeDisposable();
    private Toast toast;


    public static void copyStream(InputStream input, OutputStream output) throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    protected ApplicationComponent appComponent() {
        return Injector.INSTANCE.appComponent();
    }
//
//    protected UserComponent userComponent() {
//        return Injector.INSTANCE.userComponent();
//    }

    /**
     * Callback for Subclass to resolve dependencies, called on super.onCreate();
     */
    protected void resolveDependencies() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toast = Toast.makeText(getActivity(), "", Toast.LENGTH_LONG);
        resolveDependencies();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void showToast(final int text, final boolean isShort) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                toast.setText(getString(text).toString());
                toast.setDuration(isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    public void showToast(final String text, final boolean isShort) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                toast.setText(text);
                toast.setDuration(isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onDestroy() {
        subscriptions.clear();
        super.onDestroy();
    }
}
