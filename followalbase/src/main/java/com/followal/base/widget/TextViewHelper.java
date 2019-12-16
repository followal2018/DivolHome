package com.followal.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import com.followal.base.R;


public class TextViewHelper {

    static void setTypeface(Context context, TextView textView, AttributeSet attrs) {

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DTextView);
        Drawable leftDrawable = null;
        Drawable topDrawable = null;
        Typeface typeface;
        Drawable rightDrawable = null;
        Drawable bottomDrawable = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            leftDrawable = ta.getDrawable(R.styleable.DTextView_leftDrawable);
            rightDrawable = ta.getDrawable(R.styleable.DTextView_rightDrawable);
            bottomDrawable = ta.getDrawable(R.styleable.DTextView_bottomDrawable);
            topDrawable = ta.getDrawable(R.styleable.DTextView_topDrawable);
        } else {
            final int drawableLeftId = ta.getResourceId(R.styleable.DTextView_leftDrawable, -1);
            final int drawableRightId = ta.getResourceId(R.styleable.DTextView_rightDrawable, -1);
            final int drawableBottomId = ta.getResourceId(R.styleable.DTextView_bottomDrawable, -1);
            final int drawableTopId = ta.getResourceId(R.styleable.DTextView_topDrawable, -1);

            if (drawableLeftId != -1)
                leftDrawable = AppCompatResources.getDrawable(context, drawableLeftId);
            if (drawableRightId != -1)
                rightDrawable = AppCompatResources.getDrawable(context, drawableRightId);
            if (drawableBottomId != -1)
                bottomDrawable = AppCompatResources.getDrawable(context, drawableBottomId);
            if (drawableTopId != -1)
                topDrawable = AppCompatResources.getDrawable(context, drawableTopId);
        }

        int type = ta.getInt(R.styleable.DTextView_textFontFace, 1);
        if (leftDrawable != null || topDrawable != null || rightDrawable != null || bottomDrawable != null)
            textView.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, topDrawable, rightDrawable, bottomDrawable);
        if (type != 1)
            textView.setTypeface(FontUtils.fontName(context, type));
        ta.recycle();
    }
}
