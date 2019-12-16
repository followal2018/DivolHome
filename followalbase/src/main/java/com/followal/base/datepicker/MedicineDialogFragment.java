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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MedicineDialogFragment extends DialogFragment implements CustomWheel.OnItemSelectedListener {

    public static final String[] repeatIntervalArr = {"Pill", "Drop", "Syrup", "Injection"};
    protected CustomWheel customWheel, customDay;
    protected Button mCancelButton, mDecideButton;
    String selectedItem;
    String selectedDayItem;
    List<String> items = new ArrayList<>();
    List<String> dayItems = new ArrayList<>();
    int selectedPosition = 0;
    int selectedDayPosition = 0;
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
        View view = inflater.inflate(R.layout.dialog_remind_before, container);

        customWheel = view.findViewById(R.id.customWheel);
        customDay = view.findViewById(R.id.customDay);
        mCancelButton = view.findViewById(R.id.btn_dialog_date_cancel);
        mDecideButton = view.findViewById(R.id.btn_dialog_date_decide);
        mCancelButton.setOnClickListener(v -> dismiss());
        mDecideButton.setOnClickListener(v -> {
            if (onItemSelectListener != null) {
                onItemSelectListener.onItemSelect(selectedItem, selectedDayItem);
            }
            dismiss();
        });
        customWheel.setOnItemSelectedListener(this);
        customDay.setOnItemSelectedListener(hour -> {
            selectedDayItem = hour;
        });
        items = Arrays.asList(repeatIntervalArr);
        setData(items);
        dayItems = getData(items.get(0));
        setDayData(dayItems);
        initChild();
        setSelectedPosition();
        setDaySelectedPosition();

        return view;
    }

    private List<String> getData(String s) {
        List<String> dayItems = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            if (s.equalsIgnoreCase(repeatIntervalArr[0]) || s.equalsIgnoreCase(repeatIntervalArr[2])) {
                double d = i * 0.5;
                NumberFormat format = new DecimalFormat("0.#");
                dayItems.add(String.format(Locale.US, "%s", format.format(d)));
            } else if (s.equalsIgnoreCase(repeatIntervalArr[1]) || s.equalsIgnoreCase(repeatIntervalArr[3])) {
                dayItems.add(String.format(Locale.US, "%s", (i)));
            }
        }
        return dayItems;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        setSelectedPosition();
    }

    public void setSelectedDayPosition(int selectedPosition) {
        this.selectedDayPosition = selectedPosition;
        setDaySelectedPosition();
    }

    private void setSelectedPosition() {
        if (customWheel != null) {
            selectedItem = items.get(selectedPosition);
            customWheel.setSelectedPosition(selectedPosition);
        }
    }

    private void setDaySelectedPosition() {
        if (customDay != null) {
            selectedDayItem = dayItems.get(selectedDayPosition);
            customDay.setSelectedPosition(selectedDayPosition);
        }
    }

    public void setItems(List<String> items) {
        this.items = items;
        setData(items);
    }

    public void setDayItems(List<String> items) {
        this.dayItems = items;
        setDayData(items);
    }

    private void setData(List<String> strings) {
        if (customWheel != null) {
            customWheel.updateList(strings);
        }
    }

    private void setDayData(List<String> strings) {
        if (customDay != null) {
            customDay.updateList(strings);
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
        setDayItems(getData(items.get(items.indexOf(selectedItem))));
        customDay.setSelectedPosition(0);
    }


    public interface OnItemSelectListener {
        void onItemSelect(String item, String value);
    }
}