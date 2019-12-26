package com.div.home.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.div.home.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nirav Mandani on 25-12-2019.
 * Followal Solutions
 */
public class SelectRoomsAdapter extends RecyclerView.Adapter<SelectRoomsAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private List<String> mData = new ArrayList<>();
    private List<String> selectedRooms = new ArrayList<>();

    public SelectRoomsAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);

    }

    public void setmData(List<String> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    public List<String> getSelectedRooms() {
        return selectedRooms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_select_room, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String room = mData.get(position);
        holder.myTextView.setText(room.toUpperCase());

        holder.itemView.setOnClickListener(view -> {

            if (selectedRooms.contains(room)) {
                selectedRooms.remove(room);
            } else {
                selectedRooms.add(room);
            }

            notifyItemChanged(position);
        });

        holder.imageView.setVisibility(selectedRooms.contains(room) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    String getItem(int id) {
        return mData.get(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView myTextView;
        final ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.txtRoomName);
            imageView = itemView.findViewById(R.id.imgSelection);
        }
    }

}