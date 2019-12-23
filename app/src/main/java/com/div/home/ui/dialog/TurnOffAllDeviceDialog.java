package com.div.home.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.div.home.R;
import com.div.home.databinding.DialogTurnOffAllDevicesBinding;

/**
 * Created by Nirav Mandani on 23-12-2019.
 * Followal Solutions
 */
public class TurnOffAllDeviceDialog extends DialogFragment {

    Context context;
    ActionListener listener;
    DialogTurnOffAllDevicesBinding binding;

    public static TurnOffAllDeviceDialog getInstance() {
        return new TurnOffAllDeviceDialog();
    }

    public void setListener(ActionListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_turn_off_all_devices, null, false);

        binding.btnBackAlexa.setOnClickListener(view -> {
            if (listener != null) {
                listener.onDoneClicked();
            }
            dismiss();
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                .setView(binding.getRoot())
                .setCancelable(false);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    public interface ActionListener{
        void onDoneClicked();
    }
}
