package com.followal.base.datepicker;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CustomWheel extends WheelPicker<String> {
    private OnItemSelectedListener onItemSelectedListener;

    public CustomWheel(Context context) {
        this(context, null);
    }

    public CustomWheel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomWheel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOnWheelChangeListener((item, position) -> {
            if (onItemSelectedListener != null) {
                onItemSelectedListener.onItemSelected(item);
            }
        });
    }

    public void updateList(@NonNull List<String> dataList) {
        setDataList(dataList);
    }

    public void setSelectedPosition(int position) {
        setSelectedPosition(position, true);
    }

    public void setSelectedPosition(int position, boolean smoothScroll) {
        setCurrentPosition(position, smoothScroll);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public interface OnItemSelectedListener {
        void onItemSelected(String hour);
    }
}
