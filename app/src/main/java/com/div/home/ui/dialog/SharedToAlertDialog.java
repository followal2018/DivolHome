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
import com.div.home.databinding.DialogSharedToAlertBinding;

/**
 * Created by Nirav Mandani on 24-12-2019.
 * Followal Solutions
 */
public class SharedToAlertDialog extends DialogFragment {
    private static final String TAG = SharedToAlertDialog.class.getSimpleName();
    private static final String EXTRA_SHARE_TO_USER_ID = "extra_share_from_user_id";
    private static final String EXTRA_OLD_SHARE_TO_USER_ID = "extra_old_share_from_user_id";
    private static final String EXTRA_CURRENT_USER_ID = "extra_current_user_id";

    Context context;
    String shareToUserId;
    String oldShareToUserId;
    String currentUserId;
    ActionListener listener;
    DialogSharedToAlertBinding binding;

    public static SharedToAlertDialog getInstance(String shareToUserId, String oldShareToUserId, String currentUserId) {
        SharedToAlertDialog dialog = new SharedToAlertDialog();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_SHARE_TO_USER_ID, shareToUserId);
        bundle.putString(EXTRA_OLD_SHARE_TO_USER_ID, oldShareToUserId);
        bundle.putString(EXTRA_CURRENT_USER_ID, currentUserId);
        dialog.setArguments(bundle);
        return dialog;
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            shareToUserId = getArguments().getString(EXTRA_SHARE_TO_USER_ID);
            oldShareToUserId = getArguments().getString(EXTRA_OLD_SHARE_TO_USER_ID);
            currentUserId = getArguments().getString(EXTRA_CURRENT_USER_ID);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_shared_to_alert, null, false);
        binding.setDialog(this);

        binding.btnOk.setOnClickListener(view -> {
            if (listener != null) {
                listener.onPositiveClicked(shareToUserId, oldShareToUserId, currentUserId);
            }
            dismiss();
        });

        binding.btnCancel.setOnClickListener(view -> {
            if (listener != null) {
                listener.onCancelSelectAppliance();
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

    public interface ActionListener {

        void onPositiveClicked(String sharedToUserId, String oldSharedToUserId, String currentUserId);

        void onCancelSelectAppliance();
    }
}
