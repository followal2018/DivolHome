package com.followal.base.datepicker;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.followal.base.R;

import java.util.ArrayList;
import java.util.List;

public class CustomPickerDialogFragment extends DialogFragment implements CustomWheel.OnItemSelectedListener {

    protected CustomWheel customWheel;
    protected Button mCancelButton, mDecideButton;
    String selectedItem;
    List<String> items = new ArrayList<>();
    int selectedPosition = 0;
    private OnItemSelectListener onItemSelectListener;
    private boolean mIsShowAnimation = true;

    public CustomWheel getCustomWheel() {
        return customWheel;
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }


    public void showAnimation(boolean show) {
        mIsShowAnimation = show;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_custom, container);

        customWheel = view.findViewById(R.id.customWheel);
        mCancelButton = view.findViewById(R.id.btn_dialog_date_cancel);
        mDecideButton = view.findViewById(R.id.btn_dialog_date_decide);
        mCancelButton.setOnClickListener(v -> dismiss());
        mDecideButton.setOnClickListener(v -> {
            if (onItemSelectListener != null) {
                onItemSelectListener.onItemSelect(selectedItem);
            }
            dismiss();
        });
        customWheel.setOnItemSelectedListener(this);
        setData(items);
        initChild();
        setSelectedPosition();
        return view;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        setSelectedPosition();
    }

    private void setSelectedPosition() {
        if (customWheel != null) {
            selectedItem = items.get(selectedPosition);
            customWheel.setSelectedPosition(selectedPosition);
        }
    }

    public void setItems(List<String> items) {
        this.items = items;
        setData(items);
    }

    private void setData(List<String> strings) {
        if (customWheel != null) {
            customWheel.updateList(strings);
        }
    }

    protected void initChild() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.DatePickerBottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定

        dialog.setContentView(R.layout.dialog_date);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消

        Window window = dialog.getWindow();
        if (window != null) {
            if (mIsShowAnimation) {
                window.getAttributes().windowAnimations = R.style.DatePickerDialogAnim;
            }
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.BOTTOM; // 紧贴底部
            lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
            lp.dimAmount = 0.35f;
            window.setAttributes(lp);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

        return dialog;
    }

    @Override
    public void onItemSelected(String selectedItem) {
        this.selectedItem = selectedItem;
    }


    public interface OnItemSelectListener {
        void onItemSelect(String item);
    }
}