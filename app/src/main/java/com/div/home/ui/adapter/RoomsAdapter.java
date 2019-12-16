package com.div.home.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.div.home.R;

import java.util.List;

/**
 * Created by Nirav Mandani on 16-12-2019.
 * Followal Solutions
 */
public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.ViewHolder> {


    private final List<String> mData;
    private final LayoutInflater mInflater;
    private ItemClickListener mClickListener;


    public RoomsAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_room, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String room = mData.get(position);
        holder.myTextView.setText(room);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {

        this.mClickListener = itemClickListener;


    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(int position);

        void longClickListener(View view, int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        final TextView myTextView;
        final ImageButton imageButton;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.roomNameHomeScreenTV);
            imageButton = itemView.findViewById(R.id.deleteIV);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            imageButton.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            if (mClickListener != null) mClickListener.longClickListener(v, getAdapterPosition());
            return true;
        }
    }

}
