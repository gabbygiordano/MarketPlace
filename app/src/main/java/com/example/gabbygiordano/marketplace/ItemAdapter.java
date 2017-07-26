package com.example.gabbygiordano.marketplace;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

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

        //set up favorites
        ParseUser user = ParseUser.getCurrentUser();
        ArrayList<Item> tempList = new ArrayList<Item>();
        tempList = (ArrayList<Item>) user.get("favoritesList");

        try {
            ParseObject.fetchAllIfNeeded(tempList);
            item.fetchIfNeeded();
            item.getOwner().fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for(int i=0; i<tempList.size(); i++){
            Item newItem = tempList.get(i);
            try {
                ParseObject.fetchAllIfNeeded(tempList);
                newItem.fetchIfNeeded();
                newItem.getOwner().fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(newItem.getItemName() == item.getItemName()){
                holder.ibFavoriteOn.bringToFront();
                // notifyDataSetChanged();
            }
        }

        // Log.e(item.getOwner().getString("_created_at"), "printed");
        // returns 07-18 15:04:16.993
        if(item.getImage() != null)
        {
            String imageUri = item.getImage().getUrl();

            Glide
                    .with(context)
                    .load(imageUri)
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
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView ivItemImage;
        public TextView tvItemName;
        public TextView tvSeller;
        public TextView tvPrice;
        public TextView tvTimeAgo;
        Item thisItem;

        ImageButton ibFavoriteOn;
        ImageButton ibFavoriteOff;

        // constructor
        public ViewHolder(View itemView) {
            super(itemView);

            ivItemImage = itemView.findViewById(R.id.ivItemImage);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvSeller = itemView.findViewById(R.id.tvSeller);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTimeAgo = itemView.findViewById(R.id.tvTimeAgo);

            ibFavoriteOff = itemView.findViewById(R.id.ibFavoriteOff);
            ibFavoriteOn = itemView.findViewById(R.id.ibFavoriteOn);


            ibFavoriteOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        thisItem = mItems.get(position);
                    }
                    ParseUser user = ParseUser.getCurrentUser();
                    ArrayList<ParseObject> tempList = new ArrayList<ParseObject>();
                    tempList = (ArrayList) user.get("favoritesList");
                    try {
                        ParseObject.fetchAllIfNeeded(tempList);
                        thisItem.fetchIfNeeded();
                        thisItem.getOwner().fetchIfNeeded();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    tempList.add(thisItem);
                    user.put("favoritesList", tempList);
                    user.saveInBackground();
                    notifyDataSetChanged();
                    ParseUser.saveAllInBackground(tempList, new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(getContext(), "saved", Toast.LENGTH_LONG).show();
                                // Log.i("msg", ParseUser.getCurrentUser().getString("favoritesList"));
                                notifyDataSetChanged();
                            } else {
                                Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    notifyDataSetChanged();
                    ibFavoriteOn.bringToFront();

                }
            });

            ibFavoriteOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ibFavoriteOff.bringToFront();
                }
            });


            itemView.setOnClickListener(this);
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
            context.startActivity(i);

        }

    }
}
