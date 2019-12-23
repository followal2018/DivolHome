package com.div.home.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.div.home.R;
import com.div.home.databinding.ListItemSelectApplianceBinding;
import com.div.home.model.Appliance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nirav Mandani on 21-12-2019.
 * Followal Solutions
 */
public class SelectApplianceAdapter extends RecyclerView.Adapter<SelectApplianceAdapter.ViewHolder>  {

    Context context;
    ItemClickListener listener;
    List<Appliance> appliances = new ArrayList<>();

    public SelectApplianceAdapter(Context context) {
        this.context = context;
    }

    public void setAppliances(List<Appliance> appliances){
        this.appliances = appliances;
        notifyDataSetChanged();
    }

    public void setListener(ItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemSelectApplianceBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.list_item_select_appliance, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Appliance appliance = appliances.get(position);
        holder.binding.txtDisplayName.setText(appliance.getDisplayName());
        holder.binding.imgApplianceIcon.setImageDrawable(ContextCompat.getDrawable(context, appliance.getIcon()));
        holder.binding.imgApplianceIcon.setOnClickListener(view -> {
            if(listener != null){
                listener.onApplianceClicked(appliance);
            }
        });

        holder.binding.txtDisplayName.setOnClickListener(view -> {
            if(listener != null){
                listener.onApplianceClicked(appliance);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appliances.size();
    }

    public interface ItemClickListener{
        void onApplianceClicked(Appliance appliance);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ListItemSelectApplianceBinding binding;

        public ViewHolder(@NonNull ListItemSelectApplianceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
