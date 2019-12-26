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
import com.div.home.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nirav Mandani on 25-12-2019.
 * Followal Solutions
 */
public class SharedFromUsersAdapter extends RecyclerView.Adapter<SharedFromUsersAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    ItemClickListener listener;
    private List<User> mData = new ArrayList<>();

    public SharedFromUsersAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    public void setmData(List<User> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_select_room, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final User user = mData.get(position);
        holder.myTextView.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));

        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onRoomClicked(user);
            }
        });

        holder.imageView.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface ItemClickListener {
        void onRoomClicked(User user);
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
