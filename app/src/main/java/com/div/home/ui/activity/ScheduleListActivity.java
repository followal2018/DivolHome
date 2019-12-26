package com.div.home.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.div.home.R;
import com.div.home.databinding.ActivityScheduleListBinding;
import com.div.home.model.Schedule;
import com.div.home.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class ScheduleListActivity extends BaseActivity {

    ActivityScheduleListBinding binding;

    List<Schedule> scheduleList = new ArrayList<>();

    public static Intent getIntent(Context context) {
        return new Intent(context, ScheduleListActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_list);
        binding.setActivity(this);

        binding.rvSchedules.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && binding.btnAddSchedule.getVisibility() == View.VISIBLE) {
                    binding.btnAddSchedule.hide();
                } else if (dy < 0 && binding.btnAddSchedule.getVisibility() != View.VISIBLE) {
                    binding.btnAddSchedule.show();
                }
            }
        });
    }

    public void onClickAddSchedule(){

    }
}
