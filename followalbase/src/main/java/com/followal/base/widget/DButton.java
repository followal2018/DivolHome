package com.followal.base.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;


/**
 * Created by root on 19/6/17.
 */

public class DButton extends AppCompatButton {

    public DButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TextViewHelper.setTypeface(context, this, attrs);
    }
}