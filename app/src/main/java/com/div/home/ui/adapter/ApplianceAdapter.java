package com.div.home.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.div.home.R;
import com.div.home.databinding.ListItemApplianceBinding;

import java.util.List;

/**
 * Created by Nirav Mandani on 17-12-2019.
 * Followal Solutions
 */
public class ApplianceAdapter extends RecyclerView.Adapter<ApplianceAdapter.ViewHolder> {

    Context context;
    private final List<String> mData;
    private ApplianceAdapter.ItemClickListener itemClicker;

    public ApplianceAdapter(Context context, List<String> data){
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
        String appliance = mData.get(position);
        holder.binding.applianceNameRVAPPLIANCE.setText(appliance);
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
        void onItemClick();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ListItemApplianceBinding binding;

        ViewHolder(ListItemApplianceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (itemClicker != null) itemClicker.onItemClick();

        }
    }
}
