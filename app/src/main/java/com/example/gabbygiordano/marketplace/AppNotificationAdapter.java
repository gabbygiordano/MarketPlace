package com.example.gabbygiordano.marketplace;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.example.gabbygiordano.marketplace.R.layout.notification;

/**
 * Created by tanvigupta on 7/20/17.
 */

public class AppNotificationAdapter extends RecyclerView.Adapter<AppNotificationAdapter.ViewHolder> {


    private List<AppNotification> mAppNotifications;
    AppNotification appNotification;

    Context context;
    static Context mContext;

    // pass Items array into constructor
    public AppNotificationAdapter(List<AppNotification> appNotifications, Context context) {
        mAppNotifications = appNotifications;
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
        appNotification = mAppNotifications.get(position);

        // populate the views according to item data
        holder.tvBuyerName.setText(appNotification.getBuyer().getString("name"));
        holder.tvInterestItem.setText(appNotification.getItem().getString("item_name"));

        if (appNotification.getBuyer().getParseFile("image") != null) {
            String imgUri = appNotification.getBuyer().getParseFile("image").getUrl();
            Glide
                    .with(context)
                    .load(imgUri)
                    .bitmapTransform(new CenterCrop(context), new RoundedCornersTransformation(context, 20, 0))
                    .into(holder.ivProfileImage);
        } else {
            Glide
                    .with(context)
                    .load(R.drawable.ic_profile_tab)
                    .error(R.drawable.ic_profile_tab)
                    .placeholder(R.drawable.ic_profile_tab)
                    .into(holder.ivProfileImage);
        }

        holder.ibReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replyToBuyer(holder);
            }
        });

        }


    private void replyToBuyer(RecyclerView.ViewHolder holder)
    {
        final AppNotification notif = mAppNotifications.get(holder.getAdapterPosition());
        final CharSequence[] items = {"Message", "Email", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Reply to " + notif.getBuyer().getString("name"));
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(items[i].equals("Message"))
                {
                    String buyerPhone = notif.getBuyer().get("phone").toString();

                    Uri smsUri = Uri.parse("tel:" + buyerPhone);
                    Intent intent = new Intent(Intent.ACTION_VIEW, smsUri);
                    intent.putExtra("address", buyerPhone);
                    intent.putExtra("sms_body", "");
                    intent.setType("vnd.android-dir/mms-sms");
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    }
                }
                else if(items[i].equals("Email"))
                {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("plain/text");

                    String buyerEmail = notif.getBuyer().getString("publicEmail");
                    String itemName = notif.getItem().getItemName();

                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] {buyerEmail});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "MarketPlace request for " + itemName);
                    intent.putExtra(Intent.EXTRA_TEXT, "");
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(Intent.createChooser(intent, ""));
                    }
                }
                else if (items[i].equals("Cancel"))
                {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public int getItemCount() {
        return mAppNotifications.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        mAppNotifications.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<AppNotification> list) {
        mAppNotifications.addAll(list);
        notifyDataSetChanged();
    }

    public void add(AppNotification appNotification) {
        mAppNotifications.add(appNotification);
        notifyItemInserted(mAppNotifications.size() - 1);
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
        public ImageButton ibReply;
        AppNotification thisItem;

        boolean flag = true;

        // constructor
        public ViewHolder(View itemView) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBuyerName = itemView.findViewById(R.id.tvBuyerName);
            tvInterested = itemView.findViewById(R.id.tvInterested);
            tvInterestItem = itemView.findViewById(R.id.tvInterestItem);
            ibReply = itemView.findViewById(R.id.ibReply);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        thisItem = mAppNotifications.get(position);
                    }
                    if (thisItem.getOwner().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
                        mAppNotifications.remove(position);
                        notifyItemRemoved(position);

                        // Define the click listener as a member
                        View.OnClickListener myOnClickListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Restore item to list
                                flag = false;
                                mAppNotifications.add(position, thisItem);
                                notifyItemInserted(position);
                            }
                        };

                        Snackbar.make(view, "Notification Deleted!", Snackbar.LENGTH_LONG)
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
            });
        }

    }

}

