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
import com.followal.base.datepicker.date.YearPicker;

import java.util.ArrayList;
import java.util.List;

public class YearPickerDialogFragment extends DialogFragment implements YearPicker.OnYearSelectedListener {

    private YearPicker customWheel;
    private int selectedYear;
    private OnItemSelectListener onItemSelectListener;
    private boolean mIsShowAnimation = true;
    private List<Integer> items = new ArrayList<>();

    public YearPicker getCustomWheel() {
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
        View view = inflater.inflate(R.layout.dialog_year, container);

        customWheel = view.findViewById(R.id.customWheel);
        Button mCancelButton = view.findViewById(R.id.btn_dialog_date_cancel);
        Button mDecideButton = view.findViewById(R.id.btn_dialog_date_decide);
        mCancelButton.setOnClickListener(v -> dismiss());
        mDecideButton.setOnClickListener(v -> {
            if (onItemSelectListener != null) {
                onItemSelectListener.onYearSelect(selectedYear);

            }
            dismiss();
        });
        customWheel.setOnYearSelectedListener(this);
        setData(items);
        initChild();
        setSelectedPosition();
        return view;
    }

    public void setSelectedYear(int selectedYear) {
        this.selectedYear = selectedYear;
        setSelectedPosition();
    }

    private void setSelectedPosition() {
        if (customWheel != null) {
            customWheel.setSelectedYear(selectedYear);
        }
    }

    public void setItems(List<Integer> items) {
        this.items = items;
        setData(items);
    }

    private void setData(List<Integer> strings) {
        if (customWheel != null) {
            customWheel.setDataList(strings);
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
    public void onYearSelected(int year) {
        this.selectedYear = year;
    }


    public interface OnItemSelectListener {
        void onYearSelect(int item);
    }
}