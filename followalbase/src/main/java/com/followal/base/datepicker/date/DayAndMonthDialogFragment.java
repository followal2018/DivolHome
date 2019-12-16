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

import java.util.ArrayList;
import java.util.List;


/**
 * 时间选择器，弹出框
 * Created by ycuwq on 2018/1/6.
 */
public class DayAndMonthDialogFragment extends DialogFragment implements CustomWheel.OnItemSelectedListener {

    protected DayAndMonthPicker mDatePicker;
    protected Button mCancelButton, mDecideButton;
    List<String> items = new ArrayList<>();
    String selectedItem;
    int selectedPosition = 0;
    private int mSelectedMonth = -1, mSelectedDay = -1;
    private OnDateChooseListener mOnDateChooseListener;
    private boolean mIsShowAnimation = true;
    private boolean mIsShowWithTime = true;
    private long maxDate, minDate;

    public DayAndMonthPicker getDatePicker() {
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
        View view = inflater.inflate(R.layout.dialog_day_and_month, container);
        mDatePicker = view.findViewById(R.id.dayPicker_dialog);
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
                    mOnDateChooseListener.onDateChoose(mDatePicker.getMonth(), mDatePicker.getDay());

                }
                dismiss();
            }
        });

        if (mSelectedMonth > 0) {
            setSelectedDate();
        }

        if (minDate > 0) {
            setMinDate();
        }
        if (maxDate > 0) {
            setMaxDate();
        }
        initChild();
        return view;
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

    public void setSelectedDate(int month, int day) {
        mSelectedMonth = month;
        mSelectedDay = day;
        setSelectedDate();
    }


    private void setSelectedDate() {
        if (mDatePicker != null) {
            mDatePicker.setDate(mSelectedMonth, mSelectedDay, false);
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
        void onDateChoose(int month, int day);
    }
}