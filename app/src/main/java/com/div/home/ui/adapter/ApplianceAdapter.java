package com.div.home.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.div.home.R;
import com.div.home.databinding.ListItemApplianceBinding;
import com.div.home.model.Appliance;

import java.util.List;

/**
 * Created by Nirav Mandani on 17-12-2019.
 * Followal Solutions
 */
public class ApplianceAdapter extends RecyclerView.Adapter<ApplianceAdapter.ViewHolder> {

    Context context;
    private final List<Appliance> mData;
    private ApplianceAdapter.ItemClickListener itemClicker;

    public ApplianceAdapter(Context context, List<Appliance> data){
        this.context = context;
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        ListItemApplianceBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.list_item_appliance, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ApplianceAdapter.ViewHolder holder, int position){
        Appliance appliance = mData.get(position);
        holder.binding.applianceNameRVAPPLIANCE.setText(appliance.getDisplayName());
        holder.binding.card.setCardBackgroundColor(ContextCompat.getColor(context, appliance.getStatus() == 1 ? R.color.appliance_on : R.color.appliance_off));
        holder.binding.getRoot().setOnClickListener(view -> {
            if (itemClicker != null) itemClicker.onItemClick(appliance);
        });

    }

    @Override
    public  int getItemCount(){
        return  mData.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClicker = itemClickListener;
    }

    @SuppressWarnings("EmptyMethod")
    public interface ItemClickListener {
        void onItemClick(Appliance appliance);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ListItemApplianceBinding binding;

        ViewHolder(ListItemApplianceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }
    }
}
