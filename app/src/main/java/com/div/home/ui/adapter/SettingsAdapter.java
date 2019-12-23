package com.div.home.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.div.home.R;
import com.div.home.databinding.ListItemSettingBinding;

import java.util.List;

/**
 * Created by Nirav Mandani on 17-12-2019.
 * Followal Solutions
 */
public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {

    Context context;
    private final List<String> contents;
    private ItemClickListener mClickListener;

    public SettingsAdapter(Context context, List<String> contents) {
        this.context = context;
        this.contents = contents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemSettingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.list_item_setting, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.setText(contents.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ListItemSettingBinding binding;

        ViewHolder(ListItemSettingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(getAdapterPosition());
        }
    }
}
