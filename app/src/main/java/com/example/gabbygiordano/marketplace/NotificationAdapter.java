package com.example.gabbygiordano.marketplace;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static com.example.gabbygiordano.marketplace.R.layout.notification;

/**
 * Created by tanvigupta on 7/20/17.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<Notification> mNotifications;

    Context context;
    static Context mContext;

    // pass Items array into constructor
    public NotificationAdapter(List<Notification> notifications, Context context) {
        mNotifications = notifications;
        mContext = context;
    }

    // inflate layout and cache references into ViewHolder for each row
    // only called when entirely new row is to be created
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(notification, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    // bind values based on position of the element
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // get the data according to position
        final Notification notification = mNotifications.get(position);

        // populate the views according to item data
        holder.tvBuyerName.setText(notification.getBuyer().getString("name"));
        holder.tvInterestItem.setText(notification.getItem().getString("item_name"));
    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        mNotifications.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Notification> list) {
        mNotifications.addAll(list);
        notifyDataSetChanged();
    }

    public void add(Notification notification) {
        mNotifications.add(notification);
        notifyItemInserted(mNotifications.size() - 1);
    }

    public static Context getContext() {
        return mContext;
    }

    // create ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView ivProfileImage;
        public TextView tvBuyerName;
        public TextView tvInterested;
        public TextView tvInterestItem;

        // constructor
        public ViewHolder(View itemView) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBuyerName = itemView.findViewById(R.id.tvBuyerName);
            tvInterested = itemView.findViewById(R.id.tvInterested);
            tvInterestItem = itemView.findViewById(R.id.tvInterestItem);
        }
    }
}

