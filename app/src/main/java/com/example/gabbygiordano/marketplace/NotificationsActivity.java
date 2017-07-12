package com.example.gabbygiordano.marketplace;

import android.app.Notification;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class NotificationsActivity extends AppCompatActivity {


    ItemAdapter itemAdapter;
    ArrayList<Notification> notifications;
    RecyclerView rvNotifications;
    int REQUEST_CODE = 20;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

    }


}
