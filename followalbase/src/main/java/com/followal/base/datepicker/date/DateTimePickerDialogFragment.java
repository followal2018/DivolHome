package com.followal.base.datepicker.date;

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
import com.followal.base.datepicker.time.HourAndMinutePicker;

import java.util.ArrayList;
import java.util.List;


/**
 * 时间选择器，弹出框
 * Created by ycuwq on 2018/1/6.
 */
public class DateTimePickerDialogFragment extends DialogFragment implements CustomWheel.OnItemSelectedListener {

    protected DatePicker mDatePicker;
    protected HourAndMinutePicker mHourAndMinutePicker;
    protected Button mCancelButton, mDecideButton;
    protected CustomWheel customWheel;
    List<String> items = new ArrayList<>();
    String selectedItem;
    int selectedPosition = 0;
    private int mSelectedYear = -1, mSelectedMonth = -1, mSelectedDay = -1;
    private int mSelectedHour = -1, mSelectedMinute = -1;
    private OnDateChooseListener mOnDateChooseListener;
    private boolean mIsShowAnimation = true;
    private boolean mIsShowWithTime = true;
    private long maxDate, minDate;

    public DatePicker getDatePicker() {
        return mDatePicker;
    }

    public void setOnDateChooseListener(OnDateChooseListener onDateChooseListener) {
        mOnDateChooseListener = onDateChooseListener;
    }

    public void showAnimation(boolean show) {
        mIsShowAnimation = show;
    }

    public void showWithTime(boolean show) {
        mIsShowWithTime = show;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_date_time, container);
        customWheel = view.findViewById(R.id.customWheel);
        mDatePicker = view.findViewById(R.id.dayPicker_dialog);
        mHourAndMinutePicker = view.findViewById(R.id.timePicker);
        mCancelButton = view.findViewById(R.id.btn_dialog_date_cancel);
        mDecideButton = view.findViewById(R.id.btn_dialog_date_decide);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mDecideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDateChooseListener != null) {
                    mOnDateChooseListener.onDateChoose(mDatePicker.getYear(),
                            mDatePicker.getMonth(), mDatePicker.getDay(),
                            mHourAndMinutePicker.getHour() + 1, mHourAndMinutePicker.getMinute(), selectedItem);

                }
                dismiss();
            }
        });

        if (mSelectedYear > 0) {
            setSelectedDate();
        }
        setSelectedTime();

        if (minDate > 0) {
            setMinDate();
        }
        if (maxDate > 0) {
            setMaxDate();
        }
        initChild();
        setData(items);
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
        customWheel.setOnItemSelectedListener(this);
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

    public void setSelectedDate(int year, int month, int day) {
        mSelectedYear = year;
        mSelectedMonth = month;
        mSelectedDay = day;
        setSelectedDate();
    }

    public void setSelectedTime(int hour, int minute) {
        if (hour == 0)
            hour = 12;
        else if (hour == 12)
            setSelectedPosition(1);
        else if (hour >= 13) {
            hour = hour - 12;
            setSelectedPosition(1);
        }
        mSelectedHour = hour - 1;
        mSelectedMinute = minute;
        setSelectedTime();
    }

    private void setSelectedTime() {
        if (mHourAndMinutePicker != null) {
            mHourAndMinutePicker.setTime(mSelectedHour, mSelectedMinute);
        }
    }

    private void setSelectedDate() {
        if (mDatePicker != null) {
            mDatePicker.setDate(mSelectedYear, mSelectedMonth, mSelectedDay, false);
        }
    }

    public void setMinDate(long date) {
        this.minDate = date;
        setMinDate();
    }

    private void setMinDate() {
        if (mDatePicker != null) {
            mDatePicker.setMinDate(minDate);
        }
    }

    private void setMaxDate() {
        if (mDatePicker != null) {
            mDatePicker.setMaxDate(maxDate);
        }
    }

    public void setMaxDate(long date) {
        this.maxDate = date;
        setMaxDate();
    }

    @Override
    public void onItemSelected(String selectedItem) {
        this.selectedItem = selectedItem;
    }

    public interface OnDateChooseListener {
        void onDateChoose(int year, int month, int day, int hour, int minute, String ampm);
    }


}
