package com.example.gabbygiordano.marketplace;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.example.gabbygiordano.marketplace.R.layout.item;

/**
 * Created by tanvigupta on 7/12/17.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<Item> mItems;

    static Context context;
    static Context mContext;

    // pass Items array into constructor
    public ItemAdapter(List<Item> items, Context context) {
        mItems = items;
        mContext = context;
    }


    // inflate layout and cache references into ViewHolder for each row
    // only called when entirely new row is to be created
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    // bind values based on position of the element
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // get the data according to position
        final Item item = mItems.get(position);

        if (item == null) {
            Log.e("ItemAdapter", "ITEM NULL");
        } else {
            Log.e("ItemAdapter", "Item not null");
            if (item.getOwner() == null) {
                Log.e("ItemAdapter", "USER NULL");
            } else {
                Log.e("ItemAdapter", "Nothing is nullllll");
            }
        }

        // populate the views according to item data
        holder.tvItemName.setText(item.getItemName());
        holder.tvPrice.setText(item.getPrice());
        holder.tvSeller.setText(item.getOwner().getString("name"));
        holder.tvTimeAgo.setText(item.getOwner().getString("_created_at"));

        String type = item.getType();
        if (type.equals("Books")) {
            holder.ivType.setImageResource(R.drawable.ic_action_book);
        } else if (type.equals("Clothes")) {
            holder.ivType.setImageResource(R.drawable.ic_action_shirt);
        } else if (type.equals("Electronics")) {
            holder.ivType.setImageResource(R.drawable.ic_action_laptop_mac);
        } else {
            holder.ivType.setImageResource(R.drawable.ic_action_more);
        }

        ParseUser user = ParseUser.getCurrentUser();
        ArrayList<String> favoriteItems = (ArrayList) user.get("favoriteItems");
        if (favoriteItems.contains(item.getObjectId())) {
            // favorited
            holder.likeButton.setLiked(true);
        } else {
            // unfavorited
            holder.likeButton.setLiked(false);
        }

        holder.likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                // save in favorites list
                Toast.makeText(getContext(), "Favorited", Toast.LENGTH_LONG).show();

                ParseUser user = ParseUser.getCurrentUser();
                ArrayList<String> favoriteItems = (ArrayList) user.get("favoriteItems");

                favoriteItems.add(item.getObjectId());
                user.put("favoriteItems", favoriteItems);
                user.saveInBackground();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                // remove from favorites list
                Toast.makeText(getContext(), "Unfavorited", Toast.LENGTH_LONG).show();

                ParseUser user = ParseUser.getCurrentUser();
                ArrayList<String> favoriteItems = (ArrayList) user.get("favoriteItems");

                favoriteItems.remove(item.getObjectId());
                user.put("favoriteItems", favoriteItems);
                user.saveInBackground();
            }
        });

        holder.tvTimeAgo.setText(getRelativeTimeAgo(item.getCreatedAt()));

        if(item.getImage() != null)
        {
            String imageUri = item.getImage().getUrl();

            Glide
                    .with(context)
                    .load(imageUri)
                    .bitmapTransform(new CenterCrop(context), new RoundedCornersTransformation(context, 20, 0))
                    .placeholder(R.drawable.ic_camera)
                    .error(R.drawable.ic_camera)
                    .into(holder.ivItemImage);
        }
        else
        {
            Glide
                    .with(context)
                    .load(R.drawable.ic_camera)
                    .placeholder(R.drawable.ic_camera)
                    .error(R.drawable.ic_camera)
                    .into(holder.ivItemImage);
        }
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(Date date) {
        String relativeDate = "";

        long createdDate = date.getTime();
        relativeDate = DateUtils.getRelativeTimeSpanString(createdDate,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL).toString();

        relativeDate = relativeDate.replace(" ago", "");
        relativeDate = relativeDate.replace(" sec.", "s");
        relativeDate = relativeDate.replace(" min.", "m");
        relativeDate = relativeDate.replace(" hr.", "h");
        relativeDate = relativeDate.replace(" days", "d");

        if (relativeDate.equals("Yesterday")) {
            relativeDate = "1d";
        }

        return relativeDate;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Item> list) {
        mItems.addAll(list);
        notifyDataSetChanged();
    }

    public void add(Item item) {
        mItems.add(item);
        notifyItemInserted(mItems.size() - 1);
    }

    public static Context getContext() {
        return mContext;
    }


    // create ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public ImageView ivItemImage;
        public TextView tvItemName;
        public TextView tvSeller;
        public TextView tvPrice;
        public TextView tvTimeAgo;
        public LikeButton likeButton;
        public ImageView ivType;
        Item thisItem;

        boolean flag = true;

        // constructor
        public ViewHolder(View itemView) {
            super(itemView);

            ivItemImage = itemView.findViewById(R.id.ivItemImage);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvSeller = itemView.findViewById(R.id.tvSeller);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTimeAgo = itemView.findViewById(R.id.tvTimeAgo);

            likeButton = itemView.findViewById(R.id.likeBtn);

            ivType = itemView.findViewById(R.id.ivType);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if( position != RecyclerView.NO_POSITION){
                thisItem = mItems.get(position);
            }
            String id = thisItem.getObjectId();
            Intent i = new Intent(context, DetailsActivity.class);
            i.putExtra("ID", id);

            String name = "sharedActivityTransition";
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, ivItemImage, name);

            context.startActivity(i);
        }

        @Override
        public boolean onLongClick(View view){
            final int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION){
                thisItem = mItems.get(position);
            }
            if (thisItem.getOwner().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
                mItems.remove(position);
                notifyItemRemoved(position);

                // Define the click listener as a member
                View.OnClickListener myOnClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Restore item to list
                        flag = false;
                        mItems.add(position, thisItem);
                        notifyItemInserted(position);
                    }
                };

                Snackbar.make(view, "Item Deleted!", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", myOnClickListener)
                        .setActionTextColor(Color.rgb(255, 87, 34))
                        .addCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                if (flag) {
                                    // remove corresponding notification if exists
                                    ParseQuery<AppNotification> query = ParseQuery.getQuery(AppNotification.class);
                                    query.include("owner");
                                    query.include("image");
                                    query.whereEqualTo("item", thisItem);
                                    query.findInBackground(new FindCallback<AppNotification>() {
                                        @Override
                                        public void done(List<AppNotification> objects, ParseException e) {
                                            if (objects != null && !objects.isEmpty()) {
                                                objects.get(0).deleteInBackground();
                                            }
                                        }
                                    });

                                    thisItem.deleteInBackground();
                                    // Toast.makeText(context, "Item deleted from database!", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .show();
            }
            return true;
        }


    }
}
