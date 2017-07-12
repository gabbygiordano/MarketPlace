package com.example.gabbygiordano.marketplace;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;

public class NotificationsActivity extends AppCompatActivity {

    // TODO: item adapter is commented out until the adapter is created

    // ItemAdapter itemAdapter;
    ArrayList<Notification> notifications;
    RecyclerView rvNotifications;
    int REQUEST_CODE = 20;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        // find the RecyclerView
        rvNotifications = (RecyclerView) findViewById(R.id.rvNotifications);
        // init the arraylist (notification data)
        notifications = new ArrayList<>();
        // construct the adapter from this datasource
        // itemAdapter = new ItemAdapter(notifications, this);

        // RecyclerView setup
        rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
        //set the adapter

        //rvNotifications.setAdapter(itemAdapter);
    }


}
