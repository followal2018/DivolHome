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
public class SolarTypeDialogFragment extends DialogFragment implements CustomWheel.OnItemSelectedListener {

    protected HourAndMinutePicker mHourAndMinutePicker;
    protected CustomWheel solarType;
    protected CustomWheel beforeAfter;
    protected Button mCancelButton, mDecideButton;
    List<String> itemsSolar = new ArrayList<>();
    List<String> itemsAfterBefore = new ArrayList<>();
    String selectedItem;
    String selectedAfterBeforeItem;
    int selectedSolarPosition = 0;
    int selectedAfterBeforePosition = 0;
    private int mSelectedHour = -1, mSelectedMinute = -1;
    private OnSolarChooseListener mOnSolarChooseListener;
    private boolean mIsShowAnimation = true;

    public void setOnSolarChooseListener(OnSolarChooseListener onSolarChooseListener) {
        mOnSolarChooseListener = onSolarChooseListener;
    }

    public void showAnimation(boolean show) {
        mIsShowAnimation = show;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_solar_time, container);
        mHourAndMinutePicker = view.findViewById(R.id.timePicker);
        beforeAfter = view.findViewById(R.id.beforeAfter);
        solarType = view.findViewById(R.id.solarType);
        mCancelButton = view.findViewById(R.id.btn_dialog_date_cancel);
        mDecideButton = view.findViewById(R.id.btn_dialog_date_decide);
        mCancelButton.setOnClickListener(v -> dismiss());
        mDecideButton.setOnClickListener(v -> {
            if (mOnSolarChooseListener != null) {
                mOnSolarChooseListener.onTimeChoose(selectedItem, selectedAfterBeforeItem,
                        mHourAndMinutePicker.getHour(), mHourAndMinutePicker.getMinute());

            }
            dismiss();
        });

        setSelectedTime();

        initChild();
        setSolarData(itemsSolar);
        setSelectedSolarPosition();

        setAfterBeforeData(itemsAfterBefore);
        setSelectedAfterBeforePosition();
        return view;
    }


    protected void initChild() {
        mHourAndMinutePicker.getHourPicker().setMinHour(0);
        mHourAndMinutePicker.getHourPicker().setMaxHour(12);
        solarType.setOnItemSelectedListener(this);
        beforeAfter.setOnItemSelectedListener(hour -> {
            selectedAfterBeforeItem = hour;
        });
    }

    public void setSelectedSolarTypePosition(int selectedSolarPosition) {
        this.selectedSolarPosition = selectedSolarPosition;
        setSelectedSolarPosition();
    }

    private void setSelectedSolarPosition() {
        if (solarType != null) {
            selectedItem = itemsSolar.get(selectedSolarPosition);
            solarType.setSelectedPosition(selectedSolarPosition);
        }
    }

    public void setSolarItems(List<String> items) {
        this.itemsSolar = items;
        setSolarData(items);
    }

    private void setSolarData(List<String> strings) {
        if (solarType != null) {
            solarType.updateList(strings);
        }
    }

    //////////////
    public void setSelectedAfterBeforePosition(int selectedAfterBeforePosition) {
        this.selectedAfterBeforePosition = selectedAfterBeforePosition;
        setSelectedAfterBeforePosition();
    }

    private void setSelectedAfterBeforePosition() {
        if (beforeAfter != null) {
            selectedAfterBeforeItem = itemsAfterBefore.get(selectedAfterBeforePosition);
            beforeAfter.setSelectedPosition(selectedAfterBeforePosition);
        }
    }

    public void setAfterBeforeItems(List<String> items) {
        this.itemsAfterBefore = items;
        setAfterBeforeData(items);
    }

    private void setAfterBeforeData(List<String> strings) {
        if (beforeAfter != null) {
            beforeAfter.updateList(strings);
        }
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

    public interface OnSolarChooseListener {
        void onTimeChoose(String solarType, String afterBefore, int hour, int minute);
    }
}