package com.div.home.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;

import androidx.databinding.DataBindingUtil;

import com.div.home.R;
import com.div.home.databinding.ActivityAddScheduleBinding;
import com.div.home.databinding.ItemCustomDaysBinding;
import com.div.home.ui.base.BaseActivity;
import com.div.home.util.AppContext;
import com.div.home.util.Constants;

import java.util.HashMap;
import java.util.Locale;

public class AddScheduleActivity extends BaseActivity {


    ActivityAddScheduleBinding binding;

    View[] dayViews;
    HashMap<String, LinearLayout> weekList = new HashMap<>();


    public static Intent getIntent(Context context) {
        return new Intent(context, AddScheduleActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_schedule);
        binding.setActivity(this);

        displayWeeksView();
    }

    private void displayWeeksView() {
        dayViews = new View[Constants.customWeekDays.length];
        binding.llDaysView.removeAllViews();
        TableRow tableRow = new TableRow(AppContext.getInstance().getContext());
        binding.llDaysView.addView(tableRow);
        weekList.clear();

        for (int i = 0; i < Constants.customWeekDays.length; i++) {
            String title = Constants.customWeekDays[i].substring(0, 1);
            String tag = String.format(Locale.US, "%d", i);
            LayoutInflater mInflater = LayoutInflater.from(AppContext.getInstance().getContext());
            ItemCustomDaysBinding itemBinding = DataBindingUtil.inflate(mInflater, R.layout.item_custom_days, null, false);
            itemBinding.txtTitle.setText(title);
            itemBinding.llBackground.setTag(false);

            /*if (repeatReminder != null && repeatReminder.getIntervalType() != null && repeatReminder.getIntervalType().equals("Week")) {
                for (int j = 0; j < repeatReminder.getIntervalDays().size(); j++) {
                    if (Constants.customWeekDays[repeatReminder.getIntervalDays().get(j) - 1].equals(Constants.customWeekDays[i])) {
                        itemBinding.llBackground.setSelected(true);
                        itemBinding.llBackground.setBackgroundResource(R.drawable.ic_back_day_selected);
                    }
                }
            }*/

            weekList.put(tag, itemBinding.llBackground);
            itemBinding.getRoot().setTag(tag);
            itemBinding.getRoot().setOnClickListener(view -> {
                LinearLayout layout = weekList.get(view.getTag());
                if (layout != null) {
                    layout.setBackgroundResource(layout.isSelected() ? R.drawable.ic_back_day_unselected : R.drawable.ic_back_day_selected);
                    layout.setSelected(!layout.isSelected());
                }
            });
            tableRow.addView(itemBinding.getRoot());
            dayViews[i] = itemBinding.getRoot();
        }

        binding.llDaysView.getViewTreeObserver().addOnGlobalLayoutListener(
                () -> {
                    int pWidth = binding.llDaysView.getWidth();
                    int w = pWidth / 7;
                    for (View view : dayViews) {
                        ViewGroup.LayoutParams params = view.getLayoutParams();
                        params.width = w;
                        view.setLayoutParams(params);
                    }
                });
    }
}
