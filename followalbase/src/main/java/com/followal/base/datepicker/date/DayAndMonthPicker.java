package com.followal.base.datepicker.date;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.followal.base.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Vishal on 2018/1/1.
 */
@SuppressWarnings("unused")
public class DayAndMonthPicker extends LinearLayout implements
        MonthPicker.OnMonthSelectedListener, DayPicker.OnDaySelectedListener {

    private MonthPicker mMonthPicker;
    private DayPicker mDayPicker;

    private Long mMaxDate;
    private Long mMinDate;
    private OnDateSelectedListener mOnDateSelectedListener;

    /**
     * Instantiates a new Date picker.
     *
     * @param context the context
     */
    public DayAndMonthPicker(Context context) {
        this(context, null);
    }

    /**
     * Instantiates a new Date picker.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public DayAndMonthPicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Instantiates a new Date picker.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public DayAndMonthPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_day_and_month, this);
        initChild();
        initAttrs(context, attrs);
        mMonthPicker.setBackground(getBackground());
        mDayPicker.setBackground(getBackground());
    }

    private void initAttrs(Context context, @Nullable AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DatePicker);
        int textSize = a.getDimensionPixelSize(R.styleable.DatePicker_itemTextSize,
                getResources().getDimensionPixelSize(R.dimen.WheelItemTextSize));
        int textColor = a.getColor(R.styleable.DatePicker_itemTextColor,
                Color.BLACK);
        boolean isTextGradual = a.getBoolean(R.styleable.DatePicker_textGradual, true);
        boolean isCyclic = a.getBoolean(R.styleable.DatePicker_wheelCyclic, false);
        int halfVisibleItemCount = a.getInteger(R.styleable.DatePicker_halfVisibleItemCount, 2);
        int selectedItemTextColor = a.getColor(R.styleable.DatePicker_selectedTextColor,
                getResources().getColor(R.color.colorPrimary));
        int selectedItemTextSize = a.getDimensionPixelSize(R.styleable.DatePicker_selectedTextSize,
                getResources().getDimensionPixelSize(R.dimen.WheelSelectedItemTextSize));
        int itemWidthSpace = a.getDimensionPixelSize(R.styleable.DatePicker_itemWidthSpace,
                getResources().getDimensionPixelOffset(R.dimen.WheelItemWidthSpace));
        int itemHeightSpace = a.getDimensionPixelSize(R.styleable.DatePicker_itemHeightSpace,
                getResources().getDimensionPixelOffset(R.dimen.WheelItemHeightSpace));
        boolean isZoomInSelectedItem = a.getBoolean(R.styleable.DatePicker_zoomInSelectedItem, true);
        boolean isShowCurtain = a.getBoolean(R.styleable.DatePicker_wheelCurtain, true);
        int curtainColor = a.getColor(R.styleable.DatePicker_wheelCurtainColor, Color.WHITE);
        boolean isShowCurtainBorder = a.getBoolean(R.styleable.DatePicker_wheelCurtainBorder, true);
        int curtainBorderColor = a.getColor(R.styleable.DatePicker_wheelCurtainBorderColor,
                getResources().getColor(R.color.com_ycuwq_datepicker_divider));
        a.recycle();

        setTextSize(textSize);
        setTextColor(textColor);
        setTextGradual(isTextGradual);
        setCyclic(isCyclic);
        setHalfVisibleItemCount(halfVisibleItemCount);
        setSelectedItemTextColor(selectedItemTextColor);
        setSelectedItemTextSize(selectedItemTextSize);
        setItemWidthSpace(itemWidthSpace);
        setItemHeightSpace(itemHeightSpace);
        setZoomInSelectedItem(isZoomInSelectedItem);
        setShowCurtain(isShowCurtain);
        setCurtainColor(curtainColor);
        setShowCurtainBorder(isShowCurtainBorder);
        setCurtainBorderColor(curtainBorderColor);
    }

    private void initChild() {
        mMonthPicker = findViewById(R.id.monthPicker_layout_date);
        mMonthPicker.setOnMonthSelectedListener(this);
        mDayPicker = findViewById(R.id.dayPicker_layout_date);
        mDayPicker.setOnDaySelectedListener(this);
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        if (mMonthPicker != null && mDayPicker != null) {
            mMonthPicker.setBackgroundColor(color);
            mDayPicker.setBackgroundColor(color);
        }
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
        if (mMonthPicker != null && mDayPicker != null) {
            mMonthPicker.setBackgroundResource(resid);
            mDayPicker.setBackgroundResource(resid);
        }
    }

    @Override
    public void setBackground(Drawable background) {
        super.setBackground(background);
        if (mMonthPicker != null && mDayPicker != null) {
            mMonthPicker.setBackground(background);
            mDayPicker.setBackground(background);
        }
    }

    private void onDateSelected() {
        if (mOnDateSelectedListener != null) {
            mOnDateSelectedListener.onDateSelected(0,
                    getMonth(), getDay());
        }
    }


    @Override
    public void onMonthSelected(int month) {
        mDayPicker.setMonth(0, month);
        onDateSelected();
    }

    @Override
    public void onDaySelected(int day) {
        onDateSelected();
    }


    /**
     * Sets date.
     *
     * @param month the month
     * @param day   the day
     */
    public void setDate( int month, int day) {
        setDate( month, day, true);
    }

    /**
     * Sets date.
     *
     * @param month        the month
     * @param day          the day
     * @param smoothScroll the smooth scroll
     */
    public void setDate( int month, int day, boolean smoothScroll) {
        mMonthPicker.setSelectedMonth(month, smoothScroll);
        mDayPicker.setSelectedDay(day, smoothScroll);
    }

    public void setMaxDate(long date) {
        setCyclic(false);
        mMaxDate = date;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        mMonthPicker.setMaxDate(date);
        mDayPicker.setMaxDate(date);
        mMonthPicker.setYear(0);
        mDayPicker.setMonth(0, mMonthPicker.getSelectedMonth());
    }

    public void setMinDate(long date) {
        setCyclic(false);
        mMinDate = date;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        mMonthPicker.setMinDate(date);
        mDayPicker.setMinDate(date);
        mMonthPicker.setYear(0);
        mDayPicker.setMonth(0, mMonthPicker.getSelectedMonth());
    }

    /**
     * Gets date.
     *
     * @return the date
     */
    public String getDate() {
        DateFormat format = SimpleDateFormat.getDateInstance();
        return getDate(format);
    }

    /**
     * Gets date.
     *
     * @param dateFormat the date format
     * @return the date
     */
    public String getDate(@NonNull DateFormat dateFormat) {
        int year, month, day;
        month = getMonth();
        day = getDay();
        Calendar calendar = Calendar.getInstance();
        calendar.set(0, month - 1, day);

        return dateFormat.format(calendar.getTime());
    }


    /**
     * Gets month.
     *
     * @return the month
     */
    public int getMonth() {
        return mMonthPicker.getSelectedMonth();
    }

    /**
     * Gets day.
     *
     * @return the day
     */
    public int getDay() {
        return mDayPicker.getSelectedDay();
    }


    /**
     * Gets month picker.
     *
     * @return the month picker
     */
    public MonthPicker getMonthPicker() {
        return mMonthPicker;
    }

    /**
     * Gets day picker.
     *
     * @return the day picker
     */
    public DayPicker getDayPicker() {
        return mDayPicker;
    }

    /**
     * 一般列表的文本颜色
     *
     * @param textColor 文本颜色
     */
    public void setTextColor(@ColorInt int textColor) {
        mDayPicker.setTextColor(textColor);
        mMonthPicker.setTextColor(textColor);
    }

    /**
     * 一般列表的文本大小
     *
     * @param textSize 文字大小
     */
    public void setTextSize(int textSize) {
        mDayPicker.setTextSize(textSize);
        mMonthPicker.setTextSize(textSize);
    }

    /**
     * 设置被选中时候的文本颜色
     *
     * @param selectedItemTextColor 文本颜色
     */
    public void setSelectedItemTextColor(@ColorInt int selectedItemTextColor) {
        mDayPicker.setSelectedItemTextColor(selectedItemTextColor);
        mMonthPicker.setSelectedItemTextColor(selectedItemTextColor);
    }

    /**
     * 设置被选中时候的文本大小
     *
     * @param selectedItemTextSize 文字大小
     */
    public void setSelectedItemTextSize(int selectedItemTextSize) {
        mDayPicker.setSelectedItemTextSize(selectedItemTextSize);
        mMonthPicker.setSelectedItemTextSize(selectedItemTextSize);
    }


    /**
     * 设置显示数据量的个数的一半。
     * 为保证总显示个数为奇数,这里将总数拆分，itemCount = mHalfVisibleItemCount * 2 + 1
     *
     * @param halfVisibleItemCount 总数量的一半
     */
    public void setHalfVisibleItemCount(int halfVisibleItemCount) {
        mDayPicker.setHalfVisibleItemCount(halfVisibleItemCount);
        mMonthPicker.setHalfVisibleItemCount(halfVisibleItemCount);
    }

    /**
     * Sets item width space.
     *
     * @param itemWidthSpace the item width space
     */
    public void setItemWidthSpace(int itemWidthSpace) {
        mDayPicker.setItemWidthSpace(itemWidthSpace);
        mMonthPicker.setItemWidthSpace(itemWidthSpace);
    }

    /**
     * 设置两个Item之间的间隔
     *
     * @param itemHeightSpace 间隔值
     */
    public void setItemHeightSpace(int itemHeightSpace) {
        mDayPicker.setItemHeightSpace(itemHeightSpace);
        mMonthPicker.setItemHeightSpace(itemHeightSpace);
    }


    /**
     * Set zoom in center item.
     *
     * @param zoomInSelectedItem the zoom in center item
     */
    public void setZoomInSelectedItem(boolean zoomInSelectedItem) {
        mDayPicker.setZoomInSelectedItem(zoomInSelectedItem);
        mMonthPicker.setZoomInSelectedItem(zoomInSelectedItem);
    }

    /**
     * 设置是否循环滚动。
     * set wheel cyclic
     *
     * @param cyclic 上下边界是否相邻
     */
    public void setCyclic(boolean cyclic) {
        mDayPicker.setCyclic(cyclic);
        mMonthPicker.setCyclic(cyclic);
    }

    /**
     * 设置文字渐变，离中心越远越淡。
     * Set the text color gradient
     *
     * @param textGradual 是否渐变
     */
    public void setTextGradual(boolean textGradual) {
        mDayPicker.setTextGradual(textGradual);
        mMonthPicker.setTextGradual(textGradual);
    }


    /**
     * 设置中心Item是否有幕布遮盖
     * set the center item curtain cover
     *
     * @param showCurtain 是否有幕布
     */
    public void setShowCurtain(boolean showCurtain) {
        mDayPicker.setShowCurtain(showCurtain);
        mMonthPicker.setShowCurtain(showCurtain);
    }

    /**
     * 设置幕布颜色
     * set curtain color
     *
     * @param curtainColor 幕布颜色
     */
    public void setCurtainColor(@ColorInt int curtainColor) {
        mDayPicker.setCurtainColor(curtainColor);
        mMonthPicker.setCurtainColor(curtainColor);
    }

    /**
     * 设置幕布是否显示边框
     * set curtain border
     *
     * @param showCurtainBorder 是否有幕布边框
     */
    public void setShowCurtainBorder(boolean showCurtainBorder) {
        mDayPicker.setShowCurtainBorder(showCurtainBorder);
        mMonthPicker.setShowCurtainBorder(showCurtainBorder);
    }

    /**
     * 幕布边框的颜色
     * curtain border color
     *
     * @param curtainBorderColor 幕布边框颜色
     */
    public void setCurtainBorderColor(@ColorInt int curtainBorderColor) {
        mDayPicker.setCurtainBorderColor(curtainBorderColor);
        mMonthPicker.setCurtainBorderColor(curtainBorderColor);
    }

    /**
     * 设置选择器的指示器文本
     * set indicator text
     *
     * @param yearText  年指示器文本
     * @param monthText 月指示器文本
     * @param dayText   日指示器文本
     */
    public void setIndicatorText(String yearText, String monthText, String dayText) {
        mMonthPicker.setIndicatorText(monthText);
        mDayPicker.setIndicatorText(dayText);
    }

    /**
     * 设置指示器文字的颜色
     * set indicator text color
     *
     * @param textColor 文本颜色
     */
    public void setIndicatorTextColor(@ColorInt int textColor) {
        mMonthPicker.setIndicatorTextColor(textColor);
        mDayPicker.setIndicatorTextColor(textColor);
    }

    /**
     * 设置指示器文字的大小
     * indicator text size
     *
     * @param textSize 文本大小
     */
    public void setIndicatorTextSize(int textSize) {
        mMonthPicker.setTextSize(textSize);
        mDayPicker.setTextSize(textSize);
    }

    /**
     * Sets on date selected listener.
     *
     * @param onDateSelectedListener the on date selected listener
     */
    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        mOnDateSelectedListener = onDateSelectedListener;
    }

    /**
     * The interface On date selected listener.
     */
    public interface OnDateSelectedListener {
        /**
         * On date selected.
         *
         * @param year  the year
         * @param month the month
         * @param day   the day
         */
        void onDateSelected(int year, int month, int day);
    }
}