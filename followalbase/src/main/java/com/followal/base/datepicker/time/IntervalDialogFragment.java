package com.followal.base.datepicker.time;

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
import com.followal.base.datepicker.CustomWheel;

import java.util.ArrayList;
import java.util.List;


/**
 * 时间选择器，弹出框
 * Created by ycuwq on 2018/1/6.
 */
public class IntervalDialogFragment extends DialogFragment implements CustomWheel.OnItemSelectedListener {

    protected HourAndMinutePicker mHourAndMinutePicker;
    protected Button mCancelButton, mDecideButton;
    List<String> items = new ArrayList<>();
    String selectedItem;
    private int mSelectedHour = -1, mSelectedMinute = -1;
    private OnTimeChooseListener mOnTimeChooseListener;
    private boolean mIsShowAnimation = true;


    public void setOnTimeChooseListener(OnTimeChooseListener onTimeChooseListener) {
        mOnTimeChooseListener = onTimeChooseListener;
    }

    public void showAnimation(boolean show) {
        mIsShowAnimation = show;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_interval, container);
        mHourAndMinutePicker = view.findViewById(R.id.timePicker);
        mCancelButton = view.findViewById(R.id.btn_dialog_date_cancel);
        mDecideButton = view.findViewById(R.id.btn_dialog_date_decide);
        mCancelButton.setOnClickListener(v -> dismiss());
        mDecideButton.setOnClickListener(v -> {
            if (mOnTimeChooseListener != null) {
                mOnTimeChooseListener.onTimeChoose(
                        mHourAndMinutePicker.getHour(), mHourAndMinutePicker.getMinute());

            }
            dismiss();
        });

        setSelectedTime();

        initChild();
        return view;
    }


    protected void initChild() {
        mHourAndMinutePicker.getHourPicker().setMinHour(0);
        mHourAndMinutePicker.getHourPicker().setMaxHour(24);
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

    private void setSelectedTime() {
        if (mHourAndMinutePicker != null) {
            mHourAndMinutePicker.setTime(mSelectedHour, mSelectedMinute);
        }
    }


    @Override
    public void onItemSelected(String selectedItem) {
        this.selectedItem = selectedItem;
    }

    public interface OnTimeChooseListener {
        void onTimeChoose(int hour, int minute);
    }
}