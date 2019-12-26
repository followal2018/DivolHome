package com.div.home.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.div.home.R;
import com.div.home.databinding.ListItemSharedRoomBinding;
import com.div.home.model.SharedRoom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nirav Mandani on 25-12-2019.
 * Followal Solutions
 */
public class SharedRoomsAdapter extends RecyclerView.Adapter<SharedRoomsAdapter.ViewHolder>  {

    Context context;
    ItemClickListener listener;
    List<SharedRoom> data = new ArrayList<>();

    public SharedRoomsAdapter(Context context) {
        this.context = context;
    }

    public void setAppliances(List<SharedRoom> data){
        this.data = data;
        notifyDataSetChanged();
    }

    public void setListener(ItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemSharedRoomBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.list_item_shared_room, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SharedRoom sharedRoom = data.get(position);
        holder.binding.txtRoomName.setText(sharedRoom.getRoomName());
        holder.binding.imgRoom.setOnClickListener(view -> {
            if(listener != null){
                listener.onRoomClicked(sharedRoom);
            }
        });

        holder.binding.txtRoomName.setOnClickListener(view -> {
            if(listener != null){
                listener.onRoomClicked(sharedRoom);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface ItemClickListener{
        void onRoomClicked(SharedRoom sharedRoom);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ListItemSharedRoomBinding binding;

        public ViewHolder(@NonNull ListItemSharedRoomBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}